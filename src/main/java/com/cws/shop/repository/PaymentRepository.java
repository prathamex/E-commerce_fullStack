package com.cws.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.shop.model.Payment;
import com.cws.shop.model.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	Payment findByOrderId(String orderId);
	
	Payment findByPaymentId(String paymentId);
	
	Payment findByStatus (PaymentStatus status);
	
	boolean existsByPaymentId(String paymentId);
}
