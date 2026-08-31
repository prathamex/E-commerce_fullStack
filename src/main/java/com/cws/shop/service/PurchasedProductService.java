package com.cws.shop.service;

import java.util.List;
import java.util.Optional;

import com.cws.shop.dto.response.MyProductResponse;
import com.cws.shop.model.PurchasedProduct;

public interface PurchasedProductService {

	List<MyProductResponse> getMyProducts(Long userId);

    // Download purchased product
    String downloadProduct(Long userId, Long productId);
    
    
    
   

}