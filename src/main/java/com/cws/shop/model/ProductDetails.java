package com.cws.shop.model;


import jakarta.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import java.util.List;

import jakarta.validation.constraints.*;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "product_details")
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // this side owns the FK column product_id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    @JsonIgnore
    private Product product;

    // full markdown description shown at top of product details page
    @Column(nullable = false, columnDefinition = "TEXT")
    private String overview;

    // key feature bullet points e.g. ["Attendance tracking", "Payroll integration"]
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> features;

    // real world use cases e.g. ["Ideal for HR departments", "Works for SMEs"]
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> useCases;

    // ordered list of screenshot image URLs for gallery on details page
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> screenshots;

    // system requirements e.g. ["Windows 10 or later", "Java 17+", "MySQL 8+"]
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> technicalRequirements;

    // changed from List<String> to List<FaqItem> (proper typed objects with question + answer)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<FaqItem> faqs;

    // secure download link, accessible only after purchase + license validation
    @Column(columnDefinition = "TEXT")
    private String downloadFileUrl;

    // link to product documentation or user manual PDF
    @Column(columnDefinition = "TEXT")
    private String documentationUrl;

    // release notes for current version, shown in "What's New" section
    @Column(columnDefinition = "TEXT")
    private String releaseNotes;

    // changed from List<String> to List<VersionEntry> (proper typed objects with version, date, notes)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<VersionEntry> versionHistory;
    
   // added for you tube or any other demo link   
    @Column(columnDefinition = "TEXT")
    private String demoUrl;
    
    
    // added for you tube or any other video files
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> demoVideos;


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

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

