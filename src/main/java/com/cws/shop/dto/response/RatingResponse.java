//Rahul's Code

package com.cws.shop.dto.response;

import java.time.LocalDateTime;
 
public class RatingResponse {
 
    private Long id;
 
    // The rating value submitted (1–5)
    private int rating;
 
    // Optional comment
    private String comment;
 
    // User details (shown in review list)
    private String userName;
 
    private Long userId;
 
    // Product info
    private Long productId;
 
    // Updated product-level stats after this rating was saved
    // These are the values the frontend uses to update the star display
    private double newAverageRating;
 
    private long newTotalReviews;
 
    private LocalDateTime createdAt;
 
    public RatingResponse() {
    }
 
    // --- Getters & Setters ---
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public int getRating() {
        return rating;
    }
 
    public void setRating(int rating) {
        this.rating = rating;
    }
 
    public String getComment() {
        return comment;
    }
 
    public void setComment(String comment) {
        this.comment = comment;
    }
 
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
 
    public Long getUserId() {
        return userId;
    }
 
    public void setUserId(Long userId) {
        this.userId = userId;
    }
 
    public Long getProductId() {
        return productId;
    }
 
    public void setProductId(Long productId) {
        this.productId = productId;
    }
 
    public double getNewAverageRating() {
        return newAverageRating;
    }
 
    public void setNewAverageRating(double newAverageRating) {
        this.newAverageRating = newAverageRating;
    }
 
    public long getNewTotalReviews() {
        return newTotalReviews;
    }
 
    public void setNewTotalReviews(long newTotalReviews) {
        this.newTotalReviews = newTotalReviews;
    }
 
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
 
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}