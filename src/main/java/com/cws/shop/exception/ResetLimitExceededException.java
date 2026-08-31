package com.cws.shop.exception;

public class ResetLimitExceededException extends RuntimeException {
	
	public ResetLimitExceededException(String message) {
        super(message);
    }

}
