package com.cws.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cws.shop.model.Order;
import com.cws.shop.model.OrderStatus;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	 // GLOBAL SEARCH (Admin dashboard search bar)
		@Query("SELECT o FROM Order o WHERE " +
			       "LOWER(o.orderId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
			       "LOWER(o.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
			       "LOWER(o.productPurchased) LIKE LOWER(CONCAT('%', :keyword, '%'))")
			List<Order> searchOrders(@Param("keyword") String keyword);

	
	@Query("""
            SELECT COALESCE(SUM(o.amount), 0)
            FROM Order o
            WHERE o.status = :status
           """)
    Double getTotalRevenue(OrderStatus status);
	
//	@Query("SELECT o FROM Order o WHERE " +
//		       "LOWER(o.orderId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//		       "LOWER(o.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//		       "LOWER(o.productPurchased) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//	List<Order> searchOrders(String keyword);
	
	//Rahul's code
	@Query("SELECT o FROM Order o WHERE " +
		       "(:status IS NULL OR o.status = :status) AND " +
		       "(:paymentMethod IS NULL OR LOWER(o.paymentMethod) = LOWER(:paymentMethod)) AND " +
		       "(:transactionStatus IS NULL OR LOWER(o.transactionStatus) = LOWER(:transactionStatus)) AND " +
		       "(:fromDate IS NULL OR o.createdAt >= :fromDate) AND " +
		       "(:toDate IS NULL OR o.createdAt <= :toDate) AND " +
		       "(:minAmount IS NULL OR o.amount >= :minAmount) AND " +
		       "(:maxAmount IS NULL OR o.amount <= :maxAmount)")
		List<Order> filterOrders(
		    @Param("status") OrderStatus status,
		    @Param("paymentMethod") String paymentMethod,
		    @Param("transactionStatus") String transactionStatus,
		    @Param("fromDate") LocalDateTime fromDate,
		    @Param("toDate") LocalDateTime toDate,
		    @Param("minAmount") Double minAmount,
		    @Param("maxAmount") Double maxAmount
		);
	
	long countByUserId(Long userId);
	
	Order findByRazorpayPaymentId(String razorpayPaymentId);
	List<Order> findByUserId(Long userId);
}
