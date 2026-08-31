package com.cws.shop.repository;
 
import com.cws.shop.model.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
 
import java.util.List;
import java.util.Optional;
 
public interface RatingRepository extends JpaRepository<ProductRating, Long> {
 
    // Check if this user has already rated this product
    boolean existsByUserIdAndProductId(Long userId, Long productId);
 
    // Get all ratings for a product (for display on product detail page)
    List<ProductRating> findByProductIdOrderByCreatedAtDesc(Long productId);
 
    // Calculate average rating for a product
    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM ProductRating r WHERE r.product.id = :productId")
    Double calculateAverageRating(@Param("productId") Long productId);
 
    // Count total reviews for a product
    long countByProductId(Long productId);
 
    // Get existing rating by user and product (for update/check)
    Optional<ProductRating> findByUserIdAndProductId(Long userId, Long productId);
}