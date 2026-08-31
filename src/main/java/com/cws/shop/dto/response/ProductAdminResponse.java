package com.cws.shop.dto.response;

import com.cws.shop.model.FaqItem;
import com.cws.shop.model.ProductCategory;
import com.cws.shop.model.ProductStatus;
import com.cws.shop.model.VersionEntry;

import java.time.LocalDateTime;
import java.util.List;


public class ProductAdminResponse {

    // --- Basic Info (from Product table) ---
    private Long id;
    private String name;
    private String slug;
    private String shortDescription;
    private String thumbnailImage;
    private double price;
    private Double discountedPrice;
    private Double discountPercent;
    private String currentVersion;
    private ProductStatus status;
    private int salesCount;
    private int downloadCount;
    private double revenue;
    private double averageRating;
    private int totalReviews;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ProductCategory category;

    // --- Details Section (from ProductDetails table) — null if not yet created ---
    private String overview;
    private List<String> features;
    private List<String> useCases;
    private List<String> screenshots;
    private List<String> technicalRequirements;
    private List<FaqItem> faqs;
    private String downloadFileUrl;
    private String documentationUrl;
    private String releaseNotes;
    private List<VersionEntry> versionHistory;
    private String demoUrl;
    private List<String> demoVideos;
    

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

    public ProductStatus getStatus()
    {
        return status;
    }

    public void setStatus(ProductStatus status)
    {
        this.status = status;
    }

    public int getSalesCount()
    {
        return salesCount;
    }

    public void setSalesCount(int salesCount)
    {
        this.salesCount = salesCount;
    }

    public int getDownloadCount()
    {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount)
    {
        this.downloadCount = downloadCount;
    }

    public double getRevenue()
    {
        return revenue;
    }

    public void setRevenue(double revenue)
    {
        this.revenue = revenue;
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

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getOverview()
    {
        return overview;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public List<String> getFeatures()
    {
        return features;
    }

    public void setFeatures(List<String> features)
    {
        this.features = features;
    }

    public List<String> getUseCases()
    {
        return useCases;
    }

    public void setUseCases(List<String> useCases)
    {
        this.useCases = useCases;
    }

    public List<String> getScreenshots()
    {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots)
    {
        this.screenshots = screenshots;
    }

    public List<String> getTechnicalRequirements()
    {
        return technicalRequirements;
    }

    public void setTechnicalRequirements(List<String> technicalRequirements)
    {
        this.technicalRequirements = technicalRequirements;
    }

    public List<FaqItem> getFaqs()
    {
        return faqs;
    }

    public void setFaqs(List<FaqItem> faqs)
    {
        this.faqs = faqs;
    }

    public String getDownloadFileUrl()
    {
        return downloadFileUrl;
    }

    public void setDownloadFileUrl(String downloadFileUrl)
    {
        this.downloadFileUrl = downloadFileUrl;
    }

    public String getDocumentationUrl()
    {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl)
    {
        this.documentationUrl = documentationUrl;
    }

    public String getReleaseNotes()
    {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes)
    {
        this.releaseNotes = releaseNotes;
    }

    public List<VersionEntry> getVersionHistory()
    {
        return versionHistory;
    }

    public void setVersionHistory(List<VersionEntry> versionHistory)
    {
        this.versionHistory = versionHistory;
    }

	public String getDemoUrl() {
		return demoUrl;
	}

	public void setDemoUrl(String demoUrl) {
		this.demoUrl = demoUrl;
	}

	public List<String> getDemoVideos() {
		return demoVideos;
	}

	public void setDemoVideos(List<String> demoVideos) {
		this.demoVideos = demoVideos;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}
	
	
    
    
}