package com.cws.shop.serviceImpl;

import com.cws.shop.dto.request.CartItemRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.CartItemResponse;
import com.cws.shop.dto.response.CartResponse;
import com.cws.shop.exception.ProductNotFoundException;
import com.cws.shop.exception.UnauthorizedException;
import com.cws.shop.exception.UserNotFoundException;
import com.cws.shop.model.Cart;
import com.cws.shop.model.CartItem;
import com.cws.shop.model.Product;
import com.cws.shop.model.User;
import com.cws.shop.repository.CartItemRepository;
import com.cws.shop.repository.CartRepository;
import com.cws.shop.repository.ProductRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }
    
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    @Override
    public ApiResponse<CartResponse> addToCart(CartItemRequest request, Long userId) {
    	
    	if (request.getQuantity() <= 0) {
    	    throw new IllegalArgumentException(
    	            "Quantity must be greater than zero");
    	}
        User user = getUser(userId);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + request.getProductId()));

        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existing = cartItemRepository.findByCartAndProductId(cart, product.getId());
        if (existing.isPresent()) {

            return new ApiResponse<>(
                    true,
                    "Product already exists in cart",
                    buildResponse(cart));
        }

        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(1); // Always add as 1

        cartItemRepository.save(newItem);

        return new ApiResponse<>(
                true,
                "Product added to cart",
                buildResponse(cart));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<CartResponse> getCart(Long userId) {
        User user = getUser(userId);

        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null || cart.getItems().isEmpty()) {
            CartResponse empty = new CartResponse();
            empty.setItems(List.of());
            empty.setTotalItems(0);
            empty.setSubtotal(0);
            empty.setTax(0);
            empty.setDiscount(0);
            empty.setTotal(0);
            return new ApiResponse<>(true, "Cart is empty", empty);
        }
        return new ApiResponse<>(true, "Cart fetched", buildResponse(cart));
    }

    @Override
    public ApiResponse<CartResponse> removeFromCart(Long cartItemId, Long userId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ProductNotFoundException("Cart item not found: " + cartItemId));
        
        if (!item.getCart().getUser().getId().equals(userId)) {
            throw new UnauthorizedException(
                    "You are not authorized to remove this cart item");
        }

        Cart cart = item.getCart();
        cart.getItems().removeIf(i -> i.getId().equals(cartItemId));
        Cart saved = cartRepository.save(cart);

        return new ApiResponse<>(true, "Item removed", buildResponse(saved));
    }

    private CartResponse buildResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());

        // subtotal = sum of unit prices (consistent with what UI displays per item)
        double subtotal = 0;
        double discount = 0;

        for (CartItem item : cart.getItems()) {

            Product p = item.getProduct();

            double originalPrice = p.getPrice();

            double discountedPrice =
                    (p.getDiscountedPrice() != null &&
                     p.getDiscountedPrice() > 0)
                    ? p.getDiscountedPrice()
                    : originalPrice;

            subtotal += originalPrice * item.getQuantity();

            discount += (originalPrice - discountedPrice)
                    * item.getQuantity();
        }

        double netAmount = subtotal - discount;

        double tax = Math.round(netAmount * 18.0 / 100.0);
        double total = netAmount + tax;
        CartResponse res = new CartResponse();
        res.setCartId(cart.getId());
        res.setItems(items);
        res.setTotalItems(items.stream().mapToInt(CartItemResponse::getQuantity).sum());
        res.setSubtotal(subtotal);
        res.setTax(tax);
        res.setDiscount(discount);
        res.setTotal(total);
        return res;
    }

    @Override
    public ApiResponse<CartResponse> mergeGuestCart(List<Long> productIds, Long userId) {

        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "Guest cart products are required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found: " + userId));

        Cart cart = getOrCreateCart(user);

        Set<Long> uniqueProductIds = new HashSet<>(productIds);

        for (Long productId : uniqueProductIds) {

            Optional<CartItem> existing =
                    cartItemRepository.findByCartAndProductId(
                            cart,
                            productId);

            if (existing.isEmpty()) {

                productRepository.findById(productId)
                        .ifPresent(product -> {

                            CartItem newItem = new CartItem();
                            newItem.setCart(cart);
                            newItem.setProduct(product);
                            newItem.setQuantity(1);

                            cartItemRepository.save(newItem);
                        });
            }
        }

        return new ApiResponse<>(
                true,
                "Guest cart merged successfully",
                buildResponse(cart));
    }

    private CartItemResponse toItemResponse(CartItem item) {
        Product p = item.getProduct();
        double effectivePrice = (p.getDiscountedPrice() != null && p.getDiscountedPrice() > 0)
                ? p.getDiscountedPrice() : p.getPrice();

        CartItemResponse r = new CartItemResponse();
        r.setCartItemId(item.getId());
        r.setProductId(p.getId());
        r.setProductName(p.getName());
        r.setThumbnailImage(p.getThumbnailImage());
        r.setPrice(effectivePrice);
        r.setQuantity(item.getQuantity());
        r.setItemTotal(effectivePrice * item.getQuantity());
        return r;
    }
    
    
    @Override
    public ApiResponse<CartResponse> updateQuantity(
            Long cartItemId,
            int quantity,
            Long userId) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Cart item not found: " + cartItemId));

        // Security Check
        if (!item.getCart().getUser().getId().equals(userId)) {
            throw new UnauthorizedException(
                    "You are not authorized to update this cart item");
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        Cart cart = item.getCart();

        return new ApiResponse<>(
                true,
                "Cart quantity updated successfully",
                buildResponse(cart));
    }
}
