package com.cws.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cws.shop.dto.request.ResetPasswordRequestDto;
import com.cws.shop.service.ResetPasswordService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    public ResetPasswordController(
            ResetPasswordService resetPasswordService) {

        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @Valid @RequestBody ResetPasswordRequestDto requestDto) {

        resetPasswordService.resetPassword(token, requestDto);

        return ResponseEntity.ok("Password reset successfully");
    }
}