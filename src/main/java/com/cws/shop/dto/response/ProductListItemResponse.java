package com.cws.shop.dto.response;

import com.cws.shop.model.ProductStatus;

import java.time.LocalDateTime;


public class ProductListItemResponse {

    private Long id;

    private String name;

    private String thumbnailImage;

    private String slug;

    private int salesCount;

    private double revenue;

    private double averageRating;

    // shown as "4.9 (14)" alongside averageRating
    private int totalReviews;

    // DRAFT / PUBLISHED / ARCHIVED
    private ProductStatus status;

    // "Last Updated" column
    private LocalDateTime updatedAt;


    public ProductListItemResponse() {
    }

    public ProductListItemResponse(Long id, String name, String thumbnailImage, String slug,
                                   int salesCount, double revenue, double averageRating,
                                   int totalReviews, ProductStatus status, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.thumbnailImage = thumbnailImage;
        this.slug = slug;
        this.salesCount = salesCount;
        this.revenue = revenue;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.status = status;
        this.updatedAt = updatedAt;
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}