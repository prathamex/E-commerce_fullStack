package com.cws.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.razorpay.RazorpayClient;

@Configuration
public class PaymentConfig {
	@Value("${razorpay.key-id}")
	private String key;
	
	@Value("${razorpay.key-secret}")
	private String secret;
	
	@Bean
    public RazorpayClient razorpayClient() throws Exception {
		
		System.out.println("KEY = " + key);
	    System.out.println("SECRET = " + secret);
        return new RazorpayClient(key, secret);
    }
	
	
}
