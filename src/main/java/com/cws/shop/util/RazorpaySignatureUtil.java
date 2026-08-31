package com.cws.shop.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class RazorpaySignatureUtil {
	public static boolean verifySignature(String OrderId, String paymentId, String signature, String secret) throws Exception{
		String payload = OrderId + "|" + paymentId;

        Mac mac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey =
                new SecretKeySpec(secret.getBytes(), "HmacSHA256");

        mac.init(secretKey);

        byte[] hash = mac.doFinal(payload.getBytes());

        String generatedSignature =
                new String(hash);

        return generatedSignature.equals(signature);
	}
}
