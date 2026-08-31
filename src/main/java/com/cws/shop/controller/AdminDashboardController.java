package com.cws.shop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cws.shop.dto.response.AdminDashboardResponseDto;
import com.cws.shop.model.Product;
import com.cws.shop.repository.ProductDetailsRepository;
import com.cws.shop.repository.ProductRepository;
import com.cws.shop.service.AdminDashboardService;

@RestController
@RequestMapping("/admin/dashboard/products")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AdminDashboardController {
	private final AdminDashboardService adminDashboardService;

    private final ProductRepository productRepository;

    public AdminDashboardController(ProductRepository productRepository, AdminDashboardService adminDashboardService) {
        this.productRepository = productRepository;
        this.adminDashboardService = adminDashboardService;
    }
    
    
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(defaultValue = "") String keyword) {

    	return productRepository.searchProducts(keyword);
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponseDto> getDashboardData() {

    	AdminDashboardResponseDto response =
                adminDashboardService.getDashboardData();

        return ResponseEntity.ok(response);
    }
}