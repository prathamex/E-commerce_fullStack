package com.cws.shop.serviceImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cws.shop.dto.request.ChangePasswordRequest;
import com.cws.shop.dto.request.UpdateProfileRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.UserProfileResponseDto;
import com.cws.shop.exception.PasswordMismatchException;
import com.cws.shop.exception.SamePasswordException;
import com.cws.shop.exception.UnauthorizedException;
import com.cws.shop.exception.WeakPasswordException;
import com.cws.shop.model.User;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.UserSettingService;

@Service
public class UserSettingServiceImpl implements UserSettingService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private static final String PASSWORD_REGEX =
            "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$";

    public UserSettingServiceImpl(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApiResponse<UserProfileResponseDto> getProfile(User user) {

        return new ApiResponse<>(
                true,
                "Profile fetched successfully",
                mapToProfileResponse(user)
        );
    }

    @Override
    public ApiResponse<String> updateProfile(User user,
                                             UpdateProfileRequest request) {

        user.setName(request.getName().trim());
        user.setMobileNumber(request.getMobileNumber().trim());

        if (request.getCompanyName() != null) {
            user.setCompanyName(request.getCompanyName().trim());
            
          
        }
        if (request.getProfileImage() == null ||
        	    request.getProfileImage().isBlank()) {

        	    user.setProfileImage(null);
        	} else {
        	    user.setProfileImage(request.getProfileImage());
        	}

        // Profile image should be updated using the dedicated upload API.
        // Remove profileImage from UpdateProfileRequest if you are using
        // a separate upload endpoint.

        userRepository.save(user);

        return new ApiResponse<>(
                true,
                "Profile updated successfully",
                null
        );
    }

    @Override
    public ApiResponse<String> changePassword(User user,
                                              ChangePasswordRequest request) {

        // Verify current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new UnauthorizedException(
                    "Current password is incorrect");
        }

        // Verify confirm password
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new PasswordMismatchException(
                    "New password and confirm password do not match");
        }

        // Prevent same password
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {

            throw new SamePasswordException(
                    "New password cannot be the same as the current password");
        }

        // Password policy
        if (!request.getNewPassword().matches(PASSWORD_REGEX)) {

            throw new WeakPasswordException(
                    "Password must be at least 8 characters and contain one uppercase letter, one number, and one special character."
            );
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        return new ApiResponse<>(
                true,
                "Password updated successfully",
                null
        );
    }

    private UserProfileResponseDto mapToProfileResponse(User user) {

        UserProfileResponseDto response = new UserProfileResponseDto();

        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setMobileNumber(user.getMobileNumber());
        response.setCompanyName(user.getCompanyName());
        response.setProfileImage(user.getProfileImage());
        response.setStatus(user.getStatus());
        response.setRegistrationDate(user.getCreatedAt());
        response.setLastLogin(user.getLastLogin());

        return response;
    }
}