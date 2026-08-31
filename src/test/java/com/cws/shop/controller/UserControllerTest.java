package com.cws.shop.controller;

import com.cws.shop.dto.request.CreateUserRequestDto;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.service.AuthService;
import com.cws.shop.service.UserService;
import com.cws.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserService userService;
    private UserRepository userRepository;
    private AuthService authService;

    @BeforeEach
    void setup() {
        userService = mock(UserService.class);
        userRepository = mock(UserRepository.class);
        authService = mock(AuthService.class);

        userController = new UserController(userService, userRepository, authService);
    }

    @Test
    void createUser_delegatesToAuthService() {
        CreateUserRequestDto req = new CreateUserRequestDto();
        req.setName("Test User");
        req.setEmail("test@example.com");
        req.setMobileNumber("9999999999");
        req.setPassword("password123");

        ApiResponse<String> apiResponse = new ApiResponse<>(true, "ok", null);

        when(authService.registerBuyer(any(CreateUserRequestDto.class))).thenReturn(apiResponse);

        ResponseEntity<com.cws.shop.dto.response.ApiResponse<String>> resp = userController.createUser(req);

        assertThat(resp.getBody()).isEqualTo(apiResponse);

        ArgumentCaptor<CreateUserRequestDto> captor = ArgumentCaptor.forClass(CreateUserRequestDto.class);
        verify(authService, times(1)).registerBuyer(captor.capture());

        CreateUserRequestDto captured = captor.getValue();
        assertThat(captured.getEmail()).isEqualTo("test@example.com");
        assertThat(captured.getName()).isEqualTo("Test User");
    }
}
