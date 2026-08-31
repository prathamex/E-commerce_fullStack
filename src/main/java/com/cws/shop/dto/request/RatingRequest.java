//Rahul's Code

package com.cws.shop.dto.request;
 
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
 
public class RatingRequest {
 
    @NotNull(message = "Product ID is required")
    private Long productId;
 
    // Rating must be between 1 and 5
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
 
    // Optional review comment
    private String comment;
 
    public RatingRequest() {
    }
 
    public Long getProductId() {
        return productId;
    }
 
    public void setProductId(Long productId) {
        this.productId = productId;
    }
 
    public Integer getRating() {
        return rating;
    }
 
    public void setRating(Integer rating) {
        this.rating = rating;
    }
 
    public String getComment() {
        return comment;
    }
 
    public void setComment(String comment) {
        this.comment = comment;
    }
}