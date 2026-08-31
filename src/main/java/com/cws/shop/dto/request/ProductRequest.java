package com.cws.shop.dto.request;

import com.cws.shop.model.ProductCategory;
import com.cws.shop.model.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    // Optional: if not provided, auto-generated from name
    // e.g. "Employee Management System" -> "employee-management-system"
    private String slug;

    @NotBlank(message = "Short description is required")
    private String shortDescription;

    @NotBlank(message = "Thumbnail image URL is required")
    private String thumbnailImage;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    // Optional: if null, no discount applied
    private Double discountedPrice;

    // Optional: if set and discountedPrice is null, discountedPrice is auto-calculated
    // e.g. price=5000, discountPercent=20 -> discountedPrice=4000
    private Double discountPercent;

    // e.g. "2.3.1"
    private String currentVersion;

    // Optional: defaults to DRAFT if not provided
    private ProductStatus status;
    
    private ProductCategory category;


    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}
    
    

}