package com.cws.shop.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cws.shop.exception.UserNotFoundException;
import com.cws.shop.model.Cart;
import com.cws.shop.model.CartItem;
import com.cws.shop.model.NotificationType;
import com.cws.shop.model.OrderStatus;
import com.cws.shop.model.Payment;
import com.cws.shop.model.PaymentStatus;
import com.cws.shop.model.Product;
import com.cws.shop.model.PurchasedProduct;
import com.cws.shop.model.Role;
import com.cws.shop.model.User;
import com.cws.shop.repository.CartItemRepository;
import com.cws.shop.repository.CartRepository;
import com.cws.shop.repository.OrderRepository;
import com.cws.shop.repository.PaymentRepository;
import com.cws.shop.repository.ProductRepository;
import com.cws.shop.repository.PurchasedProductRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.NotificationService;
import com.cws.shop.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private PurchasedProductRepository purchasedProductRepository;

    @Value("${razorpay.key-secret}")
    private String razorpaySecret;

    // =========================
    // CREATE ORDER
    // =========================
    @Override
    public JSONObject createOrder(Double amount, Long userId) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // convert to paise
        options.put("currency", "INR");
        options.put("receipt", "order_rcptid_" + System.currentTimeMillis());
        options.put("payment_capture", 1);

        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(options);

        // Save payment in DB
        Payment payment = new Payment();
        payment.setOrderId(razorpayOrder.get("id"));
        payment.setBuyerId(userId);
        payment.setAmount(amount);
        payment.setCurrency("INR");
        payment.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);

        return new JSONObject(razorpayOrder.toString());
    }

    // =========================
    // VERIFY PAYMENT (synchronous fast-path, called from frontend redirect)
    // =========================
    @Override
    public boolean verifyPayment(String razorpayOrderId,
                                  String razorpayPaymentId,
                                  String razorpaySignature) throws Exception {

        Payment payment = paymentRepository.findByOrderId(razorpayOrderId);

        if (payment == null) {
            return false;
        }

        JSONObject params = new JSONObject();
        params.put("razorpay_order_id", razorpayOrderId);
        params.put("razorpay_payment_id", razorpayPaymentId);
        params.put("razorpay_signature", razorpaySignature);

        boolean isValid = Utils.verifyPaymentSignature(params, razorpaySecret);

        if (isValid) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaymentId(razorpayPaymentId);
            payment.setSignature(razorpaySignature);

            // Fetch the real payment method from Razorpay — never trust the
            // frontend and never hardcode it. This is a server-to-server call,
            // so the value is trustworthy.
            String paymentMethod = "razorpay";
            try {
                com.razorpay.Payment razorpayPayment =
                        razorpayClient.payments.fetch(razorpayPaymentId);
                String fetchedMethod = razorpayPayment.get("method");
                if (fetchedMethod != null && !fetchedMethod.isBlank()) {
                    paymentMethod = fetchedMethod;
                }
            } catch (Exception ex) {
                // Don't fail the whole verification just because the method
                // lookup failed — log and fall back to a safe default.
                System.err.println("Could not fetch payment method for "
                        + razorpayPaymentId + ": " + ex.getMessage());
            }

            // Idempotent: only creates the order if it doesn't already exist
            // for this payment (e.g. the webhook may have already processed it).
            processSuccessfulPayment(payment, razorpayPaymentId, paymentMethod);

        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        paymentRepository.save(payment);

        return isValid;
    }

    // =========================
    // WEBHOOK (authoritative source of truth for payment success)
    // =========================
    @Override
    public boolean handleWebhook(String payload, String signature) throws Exception {

        boolean isValid = Utils.verifyWebhookSignature(
                payload,
                signature,
                razorpaySecret
        );

        if (isValid) {

            JSONObject json = new JSONObject(payload);

            JSONObject paymentEntity = json
                    .getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity");

            String razorpayOrderId = paymentEntity.getString("order_id");
            String razorpayPaymentId = paymentEntity.getString("id");
            String paymentStatus = paymentEntity.getString("status");

            // The payment method is already in the webhook payload —
            // no extra API call needed for this path.
            String paymentMethod = paymentEntity.optString("method", "razorpay");

            Payment payment = paymentRepository.findByOrderId(razorpayOrderId);

            if (payment != null) {

                if ("captured".equals(paymentStatus)) {
                    payment.setStatus(PaymentStatus.SUCCESS);
                    payment.setPaymentId(razorpayPaymentId);

                    // Idempotent: if verifyPayment() already created this order
                    // (fast-path beat the webhook), this is a safe no-op.
                    processSuccessfulPayment(payment, razorpayPaymentId, paymentMethod);

                } else {
                    payment.setStatus(PaymentStatus.FAILED);
                }

                paymentRepository.save(payment);
            }
        }

        return isValid;
    }

    // =========================
    // SHARED ORDER-CREATION LOGIC (idempotent)
    // =========================
    // Called by both verifyPayment() and handleWebhook(). Whichever path
    // reaches this first "wins"; the other is a safe no-op because of the
    // razorpayPaymentId uniqueness check below.
    private void processSuccessfulPayment(Payment payment,
                                           String razorpayPaymentId,
                                           String paymentMethod) {

        // --- Idempotency guard ---
        // If an order already exists for this payment ID, another path
        // (webhook or verify-call) already processed it. Don't double-create
        // the order, double-notify, or double-count product sales.
        if (orderRepository.findByRazorpayPaymentId(razorpayPaymentId) != null) {
            System.out.println("Order already processed for payment "
                    + razorpayPaymentId + " — skipping duplicate processing.");
            return;
        }

        Long userId = payment.getBuyerId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            // Cart already cleared — most likely the other path (webhook vs
            // verify-call) already ran and cleared it. Nothing more to do.
            System.out.println("Cart already empty for user " + userId
                    + " — payment " + razorpayPaymentId + " likely already processed.");
            return;
        }

        // Calculate Subtotal / Discount
        double subtotal = 0;
        double discount = 0;

        for (CartItem item : cart.getItems()) {

            Product product = item.getProduct();

            double originalPrice = product.getPrice();

            double discountedPrice =
                    (product.getDiscountedPrice() != null
                            && product.getDiscountedPrice() > 0)
                    ? product.getDiscountedPrice()
                    : originalPrice;

            subtotal += originalPrice * item.getQuantity();

            discount += (originalPrice - discountedPrice) * item.getQuantity();
        }

        double netAmount = subtotal - discount;
        double gst = Math.round(netAmount * 18.0 / 100.0);
        double totalAmount = netAmount + gst;

        // Product Names
        String products = cart.getItems()
                .stream()
                .map(item -> item.getProduct().getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        // Create Ecommerce Order
        com.cws.shop.model.Order order = new com.cws.shop.model.Order();

        order.setUser(user);
        order.setOrderId("ORD-" + System.currentTimeMillis());
        order.setAmount(totalAmount);
        order.setCustomerName(user.getName());
        order.setProductPurchased(products);
        order.setPaymentMethod(paymentMethod);
        order.setRazorpayPaymentId(razorpayPaymentId);
        order.setTransactionStatus("SUCCESS");
        order.setLastUpdated(java.time.LocalDateTime.now());
        order.setStatus(OrderStatus.COMPLETED);

        orderRepository.save(order);
        
        for (CartItem item : cart.getItems()) {

            Product product = item.getProduct();

            PurchasedProduct purchasedProduct = new PurchasedProduct();

            purchasedProduct.setUser(user);
            purchasedProduct.setOrder(order);
            purchasedProduct.setProduct(product);

            double purchasePrice = (product.getDiscountedPrice() != null
                    && product.getDiscountedPrice() > 0)
                    ? product.getDiscountedPrice()
                    : product.getPrice();

            purchasedProduct.setPurchasePrice(purchasePrice);

            purchasedProduct.setPurchaseDate(LocalDateTime.now());

            purchasedProductRepository.save(purchasedProduct);
        }

        // User Notification
        notificationService.createNotification(
                user,
                "Purchase Successful",
                "Your order " + order.getOrderId() + " has been placed successfully.",
                NotificationType.ORDER
        );

        // Admin Notifications
        List<User> admins = userRepository.findByRole(Role.ADMIN);

        for (User admin : admins) {
            notificationService.createNotification(
                    admin,
                    "New Order Received",
                    user.getName() + " placed order " + order.getOrderId()
                            + " worth ₹" + totalAmount,
                    NotificationType.ORDER
            );
        }

        // Update product sales stats
        for (CartItem item : cart.getItems()) {

            Product product = item.getProduct();
            int quantity = item.getQuantity();

            double sellingPrice =
                    (product.getDiscountedPrice() != null
                            && product.getDiscountedPrice() > 0)
                    ? product.getDiscountedPrice()
                    : product.getPrice();

            product.setSalesCount(product.getSalesCount() + quantity);
            product.setRevenue(product.getRevenue() + (sellingPrice * quantity));

            productRepository.save(product);
        }

        // Clear Cart
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
