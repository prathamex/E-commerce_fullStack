package com.cws.shop.service;

import com.cws.shop.dto.request.ProductDetailsRequest;
import com.cws.shop.dto.request.ProductRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.PagedResponse;
import com.cws.shop.dto.response.ProductAdminResponse;
import com.cws.shop.dto.response.ProductListItemResponse;
import com.cws.shop.dto.response.ProductPublicCardResponse;
import com.cws.shop.dto.response.ProductPublicDetailResponse;
import com.cws.shop.model.Product;
import com.cws.shop.model.ProductStatus;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface ProductService {

    // Admin
    ApiResponse<ProductAdminResponse> createProduct(ProductRequest request);

    ApiResponse<PagedResponse<ProductListItemResponse>> getAllProducts(String search, ProductStatus status, Pageable pageable);

    ApiResponse<ProductAdminResponse> getProductById(Long id);

    ApiResponse<ProductAdminResponse> updateProduct(Long id, ProductRequest request);

    ApiResponse<String> updateProductStatus(Long id, ProductStatus status);

    ApiResponse<String> deleteProduct(Long id);

    ApiResponse<ProductAdminResponse> saveProductDetails(Long id, ProductDetailsRequest request);

    // Public
    ApiResponse<PagedResponse<ProductPublicCardResponse>> getPublishedProducts(String search, Pageable pageable);

    ApiResponse<ProductPublicDetailResponse> getProductBySlug(String slug);
    ApiResponse<List<ProductPublicCardResponse>> getTopProducts();
    
    
}