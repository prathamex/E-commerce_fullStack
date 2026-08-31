//Rahul's Code

package com.cws.shop.model;
 
import jakarta.persistence.*;
import java.time.LocalDateTime;
 
@Entity
@Table(
    name = "product_ratings",
    uniqueConstraints = {
        // One rating per user per product — enforced at DB level
        @UniqueConstraint(
            name = "uq_user_product_rating",
            columnNames = {"user_id", "product_id"}
        )
    }
)
public class ProductRating {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    // Rating value: 1, 2, 3, 4, or 5 (stars)
    @Column(nullable = false)
    private int rating;
 
    // Optional review comment from user
    @Column(columnDefinition = "TEXT")
    private String comment;
 
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
 
    @Column(nullable = false)
    private LocalDateTime updatedAt;
 
    // The user who gave this rating
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
 
    // The product being rated
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
 
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
 
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
 
    public ProductRating() {
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
 
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
 
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
 
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }
 
    public Product getProduct() {
        return product;
    }
 
    public void setProduct(Product product) {
        this.product = product;
    }
}