//Rahul's Code

package com.cws.shop.controller;
 
import com.cws.shop.dto.request.RatingRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.RatingResponse;
import com.cws.shop.model.User;
import com.cws.shop.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController
@RequestMapping("/api/ratings")
public class RatingController {
 
    private final RatingService ratingService;
 
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }
 
    // Authenticated BUYER submits a star rating (1–5) for a product.
    
    @PostMapping
    public ResponseEntity<ApiResponse<RatingResponse>> submitRating(
            @Valid @RequestBody RatingRequest request,
            Authentication authentication) {
 
        // Get logged-in user from JWT context (set by JwtAuthenticationFilter)
        User user = (User) authentication.getPrincipal();
 
        ApiResponse<RatingResponse> response =
                ratingService.submitRating(request, user.getId());
 
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
 
    //fetch all ratings/reviews for a product.
     // Shown on the product detail page review section.
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> getProductRatings(
            @PathVariable Long productId) {
 
        ApiResponse<List<RatingResponse>> response =
                ratingService.getProductRatings(productId);
 
        return ResponseEntity.ok(response);
    }
}