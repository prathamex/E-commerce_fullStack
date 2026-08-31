package com.cws.shop.controller;

import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.PagedResponse;
import com.cws.shop.dto.response.ProductPublicCardResponse;
import com.cws.shop.dto.response.ProductPublicDetailResponse;
import com.cws.shop.service.ProductService;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/products")
public class PublicProductController {

    private final ProductService productService;

    public PublicProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ProductPublicCardResponse>>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(productService.getPublishedProducts(search, pageable));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ProductPublicDetailResponse>> getProductBySlug(
            @PathVariable String slug) {
        return ResponseEntity.ok(productService.getProductBySlug(slug));
    }
    
    @GetMapping("/top")
    public ResponseEntity<ApiResponse<List<ProductPublicCardResponse>>> getTopProducts() {
        return ResponseEntity.ok(productService.getTopProducts());
    }
    
    
}