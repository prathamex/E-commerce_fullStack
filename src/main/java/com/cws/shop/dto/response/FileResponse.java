package com.cws.shop.dto.response;

import java.util.Map;

public class FileResponse {

    private String original;
    private Map<String, String> resizedImages;

    public FileResponse() {
    }

    public FileResponse(String original, Map<String, String> resizedImages) {
        this.original = original;
        this.resizedImages = resizedImages;
    }

    public String getOriginal() { return original; }
    public void setOriginal(String original) { this.original = original; }

    public Map<String, String> getResizedImages() { return resizedImages; }
    public void setResizedImages(Map<String, String> resizedImages) { this.resizedImages = resizedImages; }
}
