package com.cws.shop.service;

import java.util.List;

import com.cws.shop.model.Order;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    List<Order> searchOrders(String keyword);

    Double getRevenueByStatus(String status);

    Order createOrder(Order order);

    Order updateOrder(Long id, Order order);

    void deleteOrder(Long id);
    
    
    //Added by harshada 23-05-2026
    // Save Order
    Order saveOrder(Order order);
    
//    // Get All Orders
//    List<Order> getAllOrders();
    
//    // Get Order By Id
//    Order getOrderById(Long id);
    
//    // Delete Order
//    void deleteOrder(Long id);
    
//    // Update Order
//    Order updateOrder(Long id, Order order);
    
    //Rahul's code
    List<Order> filterOrders(
    	    String status,
    	    String paymentMethod,
    	    String transactionStatus,
    	    String fromDate,
    	    String toDate,
    	    Double minAmount,
    	    Double maxAmount
    	);
}