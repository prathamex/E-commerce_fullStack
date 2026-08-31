package com.cws.shop.service;

import com.cws.shop.dto.request.ChangePasswordRequest;
import com.cws.shop.dto.request.UpdateProfileRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.UserProfileResponseDto;
import com.cws.shop.model.User;

public interface UserSettingService {
	
//	     ApiResponse<UserProfileResponse> getProfile();

	ApiResponse<UserProfileResponseDto> getProfile(User user);
	ApiResponse<String> updateProfile(User user, UpdateProfileRequest request);
	ApiResponse<String> changePassword(User user, ChangePasswordRequest request);

}
