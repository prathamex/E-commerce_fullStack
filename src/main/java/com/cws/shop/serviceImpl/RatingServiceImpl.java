//Rahul's Code

package com.cws.shop.serviceImpl;
 
import com.cws.shop.dto.request.RatingRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.RatingResponse;
import com.cws.shop.exception.DuplicateRatingException;
import com.cws.shop.exception.ProductNotFoundException;
import com.cws.shop.exception.UserNotFoundException;
import com.cws.shop.model.Product;
import com.cws.shop.model.ProductRating;
import com.cws.shop.model.User;
import com.cws.shop.repository.ProductRepository;
import com.cws.shop.repository.RatingRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.RatingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
import java.util.stream.Collectors;
 
@Service
@Transactional
public class RatingServiceImpl implements RatingService {
 
    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
 
    public RatingServiceImpl(RatingRepository ratingRepository,
                             ProductRepository productRepository,
                             UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
 
    @Override
    public ApiResponse<RatingResponse> submitRating(RatingRequest request, Long userId) {
 
        // 1. Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
 
        // 2. Validate product exists and is not deleted
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with id: " + request.getProductId()));
 
        // 3. Prevent duplicate rating — one rating per user per product
        boolean alreadyRated = ratingRepository.existsByUserIdAndProductId(
                userId, request.getProductId());
 
        if (alreadyRated) {
            throw new DuplicateRatingException(
                    "You have already rated this product. Each user can rate a product only once.");
        }
 
        // 4. Save the new rating
        ProductRating productRating = new ProductRating();
        productRating.setRating(request.getRating());
        productRating.setComment(request.getComment());
        productRating.setUser(user);
        productRating.setProduct(product);
 
        ProductRating savedRating = ratingRepository.save(productRating);
 
        // 5. Recalculate average rating and total reviews for this product
        //    These values are stored on the Product entity so the admin dashboard
        //    and public listings always show live data without extra joins.
        Double newAverage = ratingRepository.calculateAverageRating(product.getId());
        long newTotal = ratingRepository.countByProductId(product.getId());
 
        // Round to 1 decimal place — e.g. 4.9, 5.0, 3.7
        double roundedAverage = Math.round(newAverage * 10.0) / 10.0;
 
        product.setAverageRating(roundedAverage);
        product.setTotalReviews((int) newTotal);
        productRepository.save(product);
 
        // 6. Build and return response
        RatingResponse response = mapToResponse(savedRating, roundedAverage, newTotal);
 
        return new ApiResponse<>(true, "Rating submitted successfully", response);
    }
 
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<RatingResponse>> getProductRatings(Long productId) {
 
        // Verify product exists
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
 
        List<ProductRating> ratings =
                ratingRepository.findByProductIdOrderByCreatedAtDesc(productId);
 
        List<RatingResponse> responseList = ratings.stream()
                .map(r -> mapToResponse(r, r.getProduct().getAverageRating(),
                        ratingRepository.countByProductId(productId)))
                .collect(Collectors.toList());
 
        return new ApiResponse<>(true, "Ratings fetched successfully", responseList);
    }
 
    // --- Private Helper ---
 
    private RatingResponse mapToResponse(ProductRating rating,
                                          double currentAverage,
                                          long currentTotal) {
        RatingResponse response = new RatingResponse();
        response.setId(rating.getId());
        response.setRating(rating.getRating());
        response.setComment(rating.getComment());
        response.setUserName(rating.getUser().getName());
        response.setUserId(rating.getUser().getId());
        response.setProductId(rating.getProduct().getId());
        response.setNewAverageRating(currentAverage);
        response.setNewTotalReviews(currentTotal);
        response.setCreatedAt(rating.getCreatedAt());
        return response;
    }
}