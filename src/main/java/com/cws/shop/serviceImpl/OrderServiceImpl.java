package com.cws.shop.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



import org.springframework.stereotype.Service;

import com.cws.shop.model.Order;
import com.cws.shop.model.OrderStatus;
import com.cws.shop.repository.OrderRepository;
import com.cws.shop.service.NotificationService;
import com.cws.shop.service.OrderService;

//Rahul's Code
import com.cws.shop.model.NotificationType;
import com.cws.shop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    
    //Rahul's code
    private final NotificationService notificationService;
    
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new com.cws.shop.exception.ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    public List<Order> searchOrders(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return orderRepository.findAll();
        }
        return orderRepository.searchOrders(keyword);
    }

    @Override
    public Double getRevenueByStatus(String status) {
        return orderRepository.getTotalRevenue(OrderStatus.valueOf(status));
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
    	Order savedOrder = orderRepository.save(order);  // save first, store in savedOrder

        if (savedOrder.getUser() != null) {
            try {
                User user = savedOrder.getUser();

                String title = "Order Placed Successfully";

                String message = buildOrderPlacedMessage(savedOrder);

                notificationService.createNotification(
                        user,
                        title,
                        message,
                        NotificationType.ORDER
                );

            } catch (Exception e) {
            	log.error("[OrderServiceImpl] Failed to send ORDER_PLACED notification: {}", e.getMessage(), e);
               
            }
        }

        return savedOrder;
    }

    @Override
    @Transactional 
    public Order updateOrder(Long id, Order updatedOrder) {
        Order existing = getOrderById(id);
        
        
        OrderStatus oldStatus = existing.getStatus();
        
        existing.setOrderId(updatedOrder.getOrderId());
        existing.setAmount(updatedOrder.getAmount());
        existing.setStatus(updatedOrder.getStatus());
        existing.setCustomerName(updatedOrder.getCustomerName());
        existing.setProductPurchased(updatedOrder.getProductPurchased());
        existing.setPaymentMethod(updatedOrder.getPaymentMethod());
        existing.setTransactionStatus(updatedOrder.getTransactionStatus());

        Order savedOrder = orderRepository.save(existing);
        
        //Rahul's code update
        if (savedOrder.getUser() != null) {
            try {
                User user = savedOrder.getUser();

                boolean statusChanged = (oldStatus != savedOrder.getStatus());

                String title;
                String message;

                if (statusChanged) {
                    title = "Order Status Updated";
                    message = buildOrderStatusChangedMessage(savedOrder, oldStatus);
                } else {
                    title = "Order Updated";
                    message = buildOrderUpdatedMessage(savedOrder);
                }

                notificationService.createNotification(
                        user,
                        title,
                        message,
                        NotificationType.ORDER
                );

            } catch (Exception e) {
            	log.error("[OrderServiceImpl] Failed to send ORDER_UPDATED notification: {}", e.getMessage(), e);
            }
        }

        return savedOrder;
    }

    @Override
    @Transactional 
    public void deleteOrder(Long id) {
    	
    	//Rahul's Code
    	Order order = getOrderById(id);

        if (order.getUser() != null) {
            try {
                User user = order.getUser();

                String title = "Order Cancelled";

                String message = buildOrderCancelledMessage(order);

                notificationService.createNotification(
                        user,
                        title,
                        message,
                        NotificationType.ORDER
                );

            } catch (Exception e) {
            	log.error("[OrderServiceImpl] Failed to send ORDER_CANCELLED notification: {}", e.getMessage(), e);
            }
        }
    	orderRepository.deleteById(id);
    }

    
    //Added by Harshada 23-05-2026
	@Override
	public Order saveOrder(Order order) {
		  order.setLastUpdated(LocalDateTime.now());
		  return orderRepository.save(order);
	}
	//Rahul's code
	private String buildOrderPlacedMessage(Order order) {

	    String orderId = order.getOrderId() != null ? order.getOrderId() : "N/A";
	    String product = order.getProductPurchased() != null
	                     ? order.getProductPurchased() : "your product";
	    String status  = order.getStatus() != null
	                     ? order.getStatus().name() : "PENDING";
	    Double amount  = order.getAmount() != null ? order.getAmount() : 0.0;

	    return "Your order #" + orderId
	         + " for '" + product + "'"
	         + " worth ₹" + String.format("%.2f", amount)
	         + " has been placed. Current status: " + status + ".";
	}

	private String buildOrderStatusChangedMessage(Order order, OrderStatus oldStatus) {

	    String orderId    = order.getOrderId() != null ? order.getOrderId() : "N/A";
	    String newStatus  = order.getStatus() != null  ? order.getStatus().name() : "UNKNOWN";
	    String prevStatus = oldStatus != null           ? oldStatus.name()         : "UNKNOWN";

	    return "Your order #" + orderId
	         + " status has changed from " + prevStatus
	         + " to " + newStatus + ".";
	}

	private String buildOrderUpdatedMessage(Order order) {

	    String orderId = order.getOrderId() != null ? order.getOrderId() : "N/A";

	    return "Details for your order #" + orderId
	         + " have been updated. Please check your order history for the latest information.";
	}

	private String buildOrderCancelledMessage(Order order) {

	    String orderId = order.getOrderId() != null ? order.getOrderId() : "N/A";
	    String product = order.getProductPurchased() != null
	                     ? order.getProductPurchased() : "your product";

	    return "Your order #" + orderId
	         + " for '" + product + "' has been cancelled and removed from the system."
	         + " Please contact support if you have any questions.";
	}
	
	//Rahul's code
	@Override
	public List<Order> filterOrders(
	        String status,
	        String paymentMethod,
	        String transactionStatus,
	        String fromDate,
	        String toDate,
	        Double minAmount,
	        Double maxAmount) {

	    OrderStatus orderStatus = (status != null && !status.equalsIgnoreCase("ALL"))
	            ? OrderStatus.valueOf(status.toUpperCase())
	            : null;

	    LocalDateTime from = (fromDate != null && !fromDate.isEmpty())
	            ? LocalDate.parse(fromDate).atStartOfDay()
	            : null;

	    LocalDateTime to = (toDate != null && !toDate.isEmpty())
	            ? LocalDate.parse(toDate).atTime(23, 59, 59)
	            : null;

	    String pm = (paymentMethod != null && !paymentMethod.equalsIgnoreCase("ALL"))
	            ? paymentMethod : null;

	    String ts = (transactionStatus != null && !transactionStatus.equalsIgnoreCase("ALL"))
	            ? transactionStatus : null;

	    return orderRepository.filterOrders(
	            orderStatus, pm, ts, from, to, minAmount, maxAmount);
	}
}