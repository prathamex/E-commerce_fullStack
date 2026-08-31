package com.cws.shop.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.cws.shop.dto.request.ForgotPassRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cws.shop.dto.request.CreateUserRequestDto;
import com.cws.shop.dto.request.LoginRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.PasswordPolicyDto;
import com.cws.shop.dto.response.LoginResponse;
import com.cws.shop.dto.response.UserDto;
import com.cws.shop.exception.InvalidTokenException;
import com.cws.shop.exception.TokenAlreadyUsedException;
import com.cws.shop.exception.TokenExpiredException;
import com.cws.shop.model.User;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
	
	private final AuthService authService;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }
    
    
    @PostMapping("/register")
    public ApiResponse<String> registerBuyer(
            @Valid @RequestBody
            CreateUserRequestDto request) {

        return authService.registerBuyer(request);
    }
    
    @GetMapping("/verify")
    public void verifyEmail(
            @RequestParam String token,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        PrintWriter writer = response.getWriter();

        try {

            authService.verifyEmail(token);

            writer.println("<h2>Account Verified Successfully</h2>");

        } catch (InvalidTokenException ex) {

            writer.println("<h2>Invalid Verification Token</h2>");

        } catch (TokenExpiredException ex) {

            writer.println("<h2>Verification Token Expired</h2>");

        } catch (TokenAlreadyUsedException ex) {

            writer.println("<h2>Account Already Verified</h2>");
        }
    }
    
    

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(authService.logout(authentication));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(@Valid @RequestBody ForgotPassRequestDto request){
        ApiResponse<?> response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                Map.of(
                        "email", user.getEmail(),
                        "name", user.getName(),
                        "role", user.getRole(),
                        "status", user.getStatus()
                )
        );
    }

    @GetMapping("/password-policy")
    public ApiResponse<PasswordPolicyDto> getPasswordPolicy() {
        PasswordPolicyDto policy = new PasswordPolicyDto(
                8,
                20,
                true,
                true,
                true,
                true,
                "Password must be 8-20 characters and contain at least one uppercase letter, one lowercase letter, one number and one special character"
        );

        return new ApiResponse<>(true, "Password policy", policy);
    }

}
