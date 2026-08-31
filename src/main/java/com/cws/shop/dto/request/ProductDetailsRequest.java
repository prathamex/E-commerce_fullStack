package com.cws.shop.dto.request;

import com.cws.shop.model.FaqItem;
import com.cws.shop.model.VersionEntry;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Used by Admin to CREATE or UPDATE a product's full detail section.
 * This is the heavy content — features, faqs, screenshots, version history, etc.
 * Called via: PUT /admin/products/{id}/details
 * Acts as UPSERT — safe to call multiple times on the same product.
 */
public class ProductDetailsRequest {

    // full markdown description
	@NotBlank(message = "Overview is required")
    private String overview;

    // key feature bullet points
    private List<String> features;

    // real-world use cases
    private List<String> useCases;

    // image URLs for gallery
    private List<String> screenshots;

    // system requirements
    private List<String> technicalRequirements;

    // question + answer pairs
    private List<FaqItem> faqs;

    // secure download link (admin only)
    private String downloadFileUrl;

    // public docs/manual link
    private String documentationUrl;

    // current version changelog
    private String releaseNotes;

    // all past versions
    private List<VersionEntry> versionHistory;
    
    
    // YouTube demo link
    private String demoUrl;

    // Multiple uploaded demo videos
    private List<String> demoVideos;


    // Getters and Setters

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<String> getUseCases() {
        return useCases;
    }

    public void setUseCases(List<String> useCases) {
        this.useCases = useCases;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
    }

    public List<String> getTechnicalRequirements() {
        return technicalRequirements;
    }

    public void setTechnicalRequirements(List<String> technicalRequirements) {
        this.technicalRequirements = technicalRequirements;
    }

    public List<FaqItem> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<FaqItem> faqs) {
        this.faqs = faqs;
    }

    public String getDownloadFileUrl() {
        return downloadFileUrl;
    }

    public void setDownloadFileUrl(String downloadFileUrl) {
        this.downloadFileUrl = downloadFileUrl;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public List<VersionEntry> getVersionHistory() {
        return versionHistory;
    }

    public void setVersionHistory(List<VersionEntry> versionHistory) {
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
    
    

}