package com.cws.shop.service;

import com.cws.shop.dto.request.CartItemRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.CartResponse;

import java.util.List;

public interface CartService {

    ApiResponse<CartResponse> addToCart(CartItemRequest request, Long userId);

    ApiResponse<CartResponse> getCart(Long userId);

    ApiResponse<CartResponse> removeFromCart(Long cartItemId, Long userId);

    ApiResponse<CartResponse> mergeGuestCart(List<Long> productIds, Long userId);
    
    ApiResponse<CartResponse> updateQuantity(Long cartItemId,int quantity,Long userId);
}
