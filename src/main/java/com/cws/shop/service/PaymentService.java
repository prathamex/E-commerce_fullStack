package com.cws.shop.service;

import org.json.JSONObject;

import com.razorpay.Order;

public interface PaymentService {
	JSONObject createOrder(Double amount, Long userId) throws Exception;

	boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) throws Exception;

	boolean handleWebhook(String payload, String signature) throws Exception;
}
