package com.cws.shop.dto.response;

import java.util.List;

import com.cws.shop.model.ProductCategory;

public class ProductPublicCardResponse
{
    private Long id;
    private String name;
    private String slug;
    private String shortDescription;
    private String thumbnailImage;
    private double price;
    private Double discountedPrice;
    private Double discountPercent;
    private String currentVersion;
    private double averageRating;
    private int totalReviews;
    private List<String> features;
    private ProductCategory category;
    private List<String> technicalRequirements;

    // --- Getters and Setters ---

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSlug()
    {
        return slug;
    }

    public void setSlug(String slug)
    {
        this.slug = slug;
    }

    public String getShortDescription()
    {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    public String getThumbnailImage()
    {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage)
    {
        this.thumbnailImage = thumbnailImage;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public Double getDiscountedPrice()
    {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice)
    {
        this.discountedPrice = discountedPrice;
    }

    public Double getDiscountPercent()
    {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent)
    {
        this.discountPercent = discountPercent;
    }

    public String getCurrentVersion()
    {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion)
    {
        this.currentVersion = currentVersion;
    }

    public double getAverageRating()
    {
        return averageRating;
    }

    public void setAverageRating(double averageRating)
    {
        this.averageRating = averageRating;
    }

    public int getTotalReviews()
    {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews)
    {
        this.totalReviews = totalReviews;
    }

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public List<String> getTechnicalRequirements() {
		return technicalRequirements;
	}

	public void setTechnicalRequirements(List<String> technicalRequirements) {
		this.technicalRequirements = technicalRequirements;
	}
    
	
	
    
}