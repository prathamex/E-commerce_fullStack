package com.cws.shop.service;

public interface PasswordResetLimitService {

	
	void validateResetLimit(String email);

    void incrementResetCount(String email);
	
	
}
