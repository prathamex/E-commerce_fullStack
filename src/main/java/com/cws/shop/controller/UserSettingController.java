package com.cws.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.cws.shop.dto.request.ChangePasswordRequest;
import com.cws.shop.dto.request.UpdateProfileRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.UserProfileResponseDto;
import com.cws.shop.exception.UnauthorizedException;
import com.cws.shop.model.User;
import com.cws.shop.service.UserSettingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user/settings")
public class UserSettingController {

    private final UserSettingService userSettingService;

    public UserSettingController(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    /**
     * Returns the currently authenticated user.
     */
    private User getCurrentUser(Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal() == null ||
                !(authentication.getPrincipal() instanceof User)) {

            throw new UnauthorizedException("User not authenticated");
        }

        return (User) authentication.getPrincipal();
    }

    /**
     * Get logged-in user's profile.
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> getProfile(
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        return ResponseEntity.ok(
                userSettingService.getProfile(user)
        );
    }

    /**
     * Update profile information.
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<String>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        return ResponseEntity.ok(
                userSettingService.updateProfile(user, request)
        );
    }

    /**
     * Change password.
     */
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        return ResponseEntity.ok(
                userSettingService.changePassword(user, request)
        );
    }
}