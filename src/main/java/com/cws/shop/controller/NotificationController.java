package com.cws.shop.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.NotificationResponse;
import com.cws.shop.dto.response.PagedResponse;
import com.cws.shop.exception.UserNotFoundException;
import com.cws.shop.model.User;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.NotificationService;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
	private final NotificationService notificationService;
	
	private final UserRepository userRepository;
	
    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
		this.userRepository = userRepository;
    }
    
//    private User getCurrentUser(Authentication authentication) {
//
//        return (User) authentication.getPrincipal();
//    }
    
    private User getCurrentUser(
            Authentication authentication) {

        if (authentication == null ||
            authentication.getPrincipal() == null) {

            throw new UserNotFoundException(
                    "User not authenticated");
        }

        return (User) authentication.getPrincipal();
    }
    
    
      
    
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<NotificationResponse>>>
    getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {

        User user = getCurrentUser(authentication);

        PagedResponse<NotificationResponse> notifications =
                notificationService.getAllNotifications(
                        user.getId(),
                        page,
                        size
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Notifications fetched successfully",
                        notifications
                )
        );
    }


    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>>
    getUnreadNotifications(Authentication authentication) {

        User user = getCurrentUser(authentication);

        List<NotificationResponse> notifications =
                notificationService
                        .getUnreadNotifications(user.getId());

        ApiResponse<List<NotificationResponse>> response =
                new ApiResponse<>(
                        true,
                        "Unread notifications fetched successfully",
                        notifications
                );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Long>>
    getUnreadCount(Authentication authentication) {

        User user = getCurrentUser(authentication);

        Long count =
                notificationService.getUnreadCount(user.getId());

        ApiResponse<Long> response =
                new ApiResponse<>(
                        true,
                        "Unread notification count fetched successfully",
                        count
                );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>>
    markAsRead(@PathVariable Long notificationId,
    		Authentication authentication) {
    	
    	 User user = getCurrentUser(authentication);

        NotificationResponse notification =
                notificationService.markAsRead(notificationId, user.getId());

        ApiResponse<NotificationResponse> response =
                new ApiResponse<>(
                        true,
                        "Notification marked as read",
                        notification
                );

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<String>>
    markAllAsRead(Authentication authentication) {

        User user = getCurrentUser(authentication);

        notificationService.markAllAsRead(user.getId());

        ApiResponse<String> response =
                new ApiResponse<>(
                        true,
                        "All notifications marked as read",
                        null
                );

        return ResponseEntity.ok(response);
    }
}
