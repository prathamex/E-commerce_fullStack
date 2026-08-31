package com.cws.shop.dto.response;

import java.time.LocalDateTime;

public class MyProductResponse {

    private Long id;
    private String name;
    private String category;
    private String version;
    private String thumbnailImage;
    private LocalDateTime purchaseDate;
    private String downloadUrl;
    
    private Long invoiceId;

    public MyProductResponse() {
    }

    public MyProductResponse(Long id, String name, String category, String version,
                             String thumbnailImage, LocalDateTime purchaseDate,
                             String downloadUrl, Long invoiceId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.version = version;
        this.thumbnailImage = thumbnailImage;
        this.purchaseDate = purchaseDate;
        this.downloadUrl = downloadUrl;
        this.invoiceId = invoiceId;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
}