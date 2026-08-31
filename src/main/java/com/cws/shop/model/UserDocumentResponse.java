package com.cws.shop.model;

import java.time.LocalDateTime;

public class UserDocumentResponse {

    private Long id;
    private String url;
    private String orignalUrl;
    private String urlType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDocumentResponse() {
    }

    public UserDocumentResponse(Long id, String url, String orignalUrl, String urlType,
                                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.url = url;
        this.orignalUrl = orignalUrl;
        this.urlType = urlType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getOrignalUrl() { return orignalUrl; }
    public void setOrignalUrl(String orignalUrl) { this.orignalUrl = orignalUrl; }

    public String getUrlType() { return urlType; }
    public void setUrlType(String urlType) { this.urlType = urlType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
