package com.cws.shop.service;

import com.cws.shop.dto.request.ForgotPassRequestDto;
import org.springframework.security.core.Authentication;

import com.cws.shop.dto.request.CreateUserRequestDto;
import com.cws.shop.dto.request.LoginRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.LoginResponse;


public interface AuthService {
	
	ApiResponse<LoginResponse> login(LoginRequest request);
	
	ApiResponse<String> logout(Authentication authentication);
	
	ApiResponse<String> registerBuyer(CreateUserRequestDto request);
	
	ApiResponse<String> verifyEmail(String token);

    ApiResponse<?> forgotPassword(ForgotPassRequestDto request);


}
