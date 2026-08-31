//Rahul's Code

package com.cws.shop.service;
 
import com.cws.shop.dto.request.RatingRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.RatingResponse;
 
import java.util.List;
 
public interface RatingService {
 
    // Authenticated user submits a rating for a product
    // Throws DuplicateRatingException if user already rated this product
    ApiResponse<RatingResponse> submitRating(RatingRequest request, Long userId);
 
    // Get all ratings/reviews for a product (for product detail page)
    ApiResponse<List<RatingResponse>> getProductRatings(Long productId);
}