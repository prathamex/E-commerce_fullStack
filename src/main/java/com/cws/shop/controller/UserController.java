package com.cws.shop.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.UserDto;
import com.cws.shop.dto.request.CreateUserRequestDto;
import com.cws.shop.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.model.Role;
import com.cws.shop.model.User;
import com.cws.shop.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthService authService;

    public UserController(UserService userService,
            UserRepository userRepository,
            AuthService authService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<UserDto>> activateUser(@PathVariable Long id) {
        UserDto userDto = userService.activeUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User activated successfully", userDto));
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<UserDto>> suspendUser(@PathVariable Long id) {
        UserDto userDto = userService.suspendUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User suspended successfully", userDto));
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<ApiResponse<UserDto>> blockUser(@PathVariable Long id) {
        UserDto userDto = userService.blockUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User blocked successfully", userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
    }

    // Search users (general search)
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserDto>>> searchUsers(
            @RequestParam String keyword) {
        List<UserDto> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully", users));
    }

    // Get all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully", userService.getAllUsers()));
    }

    // Filter users by status
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<UserDto>>> filterUsers(
            @RequestParam String status) {
        List<UserDto> users = userService.filterUsers(status);
        return ResponseEntity.ok(new ApiResponse<>(true, "Users filtered successfully", users));
    }

    // Used by "Assign to User" dropdown in generate new license form
    // Searches all roles so any registered user can be assigned a license
    @GetMapping("/assign-search")
    public ResponseEntity<ApiResponse<List<UserDto>>> searchUsersForAssign(
            @RequestParam String keyword) {

        List<User> users = userRepository
        		.findByNameContainingIgnoreCaseAndRole(keyword, Role.BUYER);

        List<UserDto> response = users.stream()
                .map(u -> new UserDto(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getMobileNumber(),
                        u.getRole(),
                        u.getStatus(),
                        u.getUpdatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully", response));
    }

    // Create a new user (Admin -> Add User)
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createUser(
            @Valid @RequestBody CreateUserRequestDto request) {

        ApiResponse<String> response = authService.registerBuyer(request);

        return ResponseEntity.ok(response);
    }
}