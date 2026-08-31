package com.cws.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cws.shop.dto.response.AdminProfileResponseDto;
import com.cws.shop.service.AdminProfileService;



@RestController
@RequestMapping("/api/admin/profile")

public class AdminProfileController {

    private final AdminProfileService adminProfileService ;
    
    public AdminProfileController(AdminProfileService adminProfileService) {
        this.adminProfileService = adminProfileService;
    }
    //20-5-26
    @GetMapping
    public ResponseEntity<AdminProfileResponseDto> getProfile() {

        return ResponseEntity.ok(
                adminProfileService.getAdminProfile()
        );
    }
}