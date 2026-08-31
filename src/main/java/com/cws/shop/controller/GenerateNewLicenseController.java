package com.cws.shop.controller;

import com.cws.shop.dto.request.GenerateNewLicenseRequest;
import com.cws.shop.dto.request.LicenseFilterRequest;
import com.cws.shop.dto.request.RenewLicenseRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.GenerateNewLicenseResponse;
import com.cws.shop.dto.response.LicenseDashboardResponse;
import com.cws.shop.dto.response.LicenseDetailResponse;
import com.cws.shop.dto.response.PagedLicenseResponse;
import com.cws.shop.service.GenerateNewLicenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/licenses")
public class GenerateNewLicenseController {

    private final GenerateNewLicenseService generateNewLicenseService;

    public GenerateNewLicenseController(
            GenerateNewLicenseService generateNewLicenseService) {
        this.generateNewLicenseService = generateNewLicenseService;
    }

    
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<GenerateNewLicenseResponse>> generateLicense(
            @Valid @RequestBody GenerateNewLicenseRequest request) {
        GenerateNewLicenseResponse response =
                generateNewLicenseService.generateLicense(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,
                        "License generated and emailed successfully", response));
    }

   
    @GetMapping
    public ResponseEntity<ApiResponse<PagedLicenseResponse>> getAllLicenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedLicenseResponse response =
                generateNewLicenseService.getAllLicenses(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Licenses fetched successfully", response));
    }

    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<LicenseDashboardResponse>>> searchLicenses(
            @RequestParam String keyword) {
        List<LicenseDashboardResponse> licenses =
                generateNewLicenseService.searchLicenses(keyword);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Search results", licenses));
    }

    
    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<PagedLicenseResponse>> filterLicenses(
            @RequestBody LicenseFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedLicenseResponse response =
                generateNewLicenseService.filterLicenses(filter, page, size);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Filtered licenses fetched", response));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LicenseDetailResponse>> viewLicense(
            @PathVariable Long id) {
        LicenseDetailResponse response =
                generateNewLicenseService.viewLicense(id);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "License details fetched", response));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLicense(
            @PathVariable Long id) {
        generateNewLicenseService.deleteLicense(id);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "License deleted successfully", null));
    }

    
    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<LicenseDashboardResponse>> activateLicense(
            @PathVariable Long id) {
        LicenseDashboardResponse response =
                generateNewLicenseService.activateLicense(id);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "License activated successfully", response));
    }

    
    @PutMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<LicenseDashboardResponse>> suspendLicense(
            @PathVariable Long id) {
        LicenseDashboardResponse response =
                generateNewLicenseService.suspendLicense(id);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "License suspended successfully", response));
    }

    
    
    @PutMapping("/{id}/renew")
    public ResponseEntity<ApiResponse<LicenseDashboardResponse>> renewLicense(
            @PathVariable Long id,
            @Valid @RequestBody RenewLicenseRequest request) {
        LicenseDashboardResponse response =
                generateNewLicenseService.renewLicense(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "License renewed successfully", response));
    }
}