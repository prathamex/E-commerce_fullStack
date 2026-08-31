package com.cws.shop.dto.response;

import com.cws.shop.model.FaqItem;
import com.cws.shop.model.VersionEntry;

import java.util.List;


public class ProductPublicDetailResponse
{

    // --- Card fields (same as ProductPublicCardResponse) ---
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

    // --- Full Detail Section (from ProductDetails table) ---
    private String overview;
    private List<String> features;
    private List<String> useCases;
    private List<String> screenshots;
    private List<String> technicalRequirements;
    private List<FaqItem> faqs;
    private String documentationUrl;
    private String releaseNotes;
    private List<VersionEntry> versionHistory;

    // NOTE: downloadFileUrl is intentionally NOT here — protected behind purchase + license check

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
}