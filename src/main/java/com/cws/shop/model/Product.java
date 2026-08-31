package com.cws.shop.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;


@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // basic info shown on listing cards and admin table
    @Column(nullable = false)
    private String name;


//	
//	@JdbcTypeCode(SqlTypes.JSON)
//	@Column(columnDefinition = "json", nullable = false)
//	private List<String> description;
//	
//	@Column(nullable = false)
//	private double price;
//	
//	private LocalDateTime createdAt;
//	private LocalDateTime updatedAt;
//
//	
//	@PrePersist
//	public void onCreate() {
//	    this.createdAt = LocalDateTime.now();
//	    this.updatedAt = LocalDateTime.now();
//	}
//
//	@PreUpdate
//	public void onUpdate() {
//	    this.updatedAt = LocalDateTime.now();
//	}
//	
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public List<String> getDescription() {
//		return description;
//	}
//	public void setDescription(List<String> description) {
//		this.description = description;
//	}
//	public double getPrice() {
//		return price;
//	}
//	public void setPrice(double price) {
//		this.price = price;
//	}
//	
//	public LocalDateTime getCreatedAt() {
//		return createdAt;
//	}
//	public void setCreatedAt(LocalDateTime createdAt) {
//		this.createdAt = createdAt;
//	}
//	public LocalDateTime getUpdatedAt() {
//		return updatedAt;
//	}
//	public void setUpdatedAt(LocalDateTime updatedAt) {
//		this.updatedAt = updatedAt;
//	}
	

    @Column(nullable = false, unique = true)
    private String slug;  // e.g. "employee-management-system" used in /products/{slug}

    @Column(nullable = false, columnDefinition = "TEXT")
    private String shortDescription;

    @Column(nullable = false)
    private String thumbnailImage;

    // pricing
    @Column(nullable = false)
    private double price;

    private Double discountedPrice;  // null if no discount is active

    private Double discountPercent;  // e.g. 20.0 means 20% off, null if no discount

    // version shown on listing card and customer My Products page
    private String currentVersion;  // e.g. "2.3.1"

    // controls visibility on public site
    // DRAFT -> admin only, PUBLISHED -> live, ARCHIVED -> hidden
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    // metrics shown on admin dashboard and products table
    @Column(nullable = false)
    private int salesCount;

    @Column(nullable = false)
    private int downloadCount;

    @Column(nullable = false)
    private double revenue;

    @Column(nullable = false)
    private double averageRating;  // out of 5.0, e.g. 4.9

    @Column(nullable = false)
    private int totalReviews;  // shown as "4.9 (14)" alongside averageRating

    // heavy content like features, screenshots, faqs
    // loaded only when user opens the product detail page
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProductDetails details;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<ProductRating> ratings;

    @Column(nullable = false)
    private Boolean deleted = false;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ProductCategory category;
    
    // timestamps
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;  // shown as Last Updated in admin products table

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = ProductStatus.DRAFT;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // getters and setters

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
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

    public ProductDetails getDetails() {
        return details;
    }

    public void setDetails(ProductDetails details) {
        this.details = details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Boolean getDeleted() {
        return deleted;
    }
 
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    
    public java.util.List<ProductRating> getRatings() {
        return ratings;
    }
     
    public void setRatings(java.util.List<ProductRating> ratings) {
        this.ratings = ratings;
    }

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}
    
    
}
