package com.cws.shop.controller;

import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.DashboardSummaryResponse;
import com.cws.shop.dto.response.MyProductResponse;
import com.cws.shop.model.User;
import com.cws.shop.service.DashboardService;
import com.cws.shop.service.PurchasedProductService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/dashboard")
public class UserDashboardController {

    private final DashboardService dashboardService;
    private final PurchasedProductService purchasedProductService;

    

    public UserDashboardController(DashboardService dashboardService, PurchasedProductService purchasedProductService) {
		super();
		this.dashboardService = dashboardService;
		this.purchasedProductService = purchasedProductService;
	}

	@GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getDashboardSummary(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        DashboardSummaryResponse response =
                dashboardService.getDashboardSummary(user.getId());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Dashboard summary fetched successfully.",
                        response
                )
        );
    }
    
    @GetMapping("/my-products")
    public ResponseEntity<ApiResponse<List<MyProductResponse>>> getMyProducts(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List
        <MyProductResponse> products =
                purchasedProductService.getMyProducts(user.getId());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "My products fetched successfully.",
                        products
                )
        );
    }
}