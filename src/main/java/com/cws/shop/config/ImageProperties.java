package com.cws.shop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "image")
public class ImageProperties {

    private String uploadDir;
    private Map<String, Size> sizes;
    private long maxSize;
    private List<String> allowedFormats;

    public String getUploadDir() { return uploadDir; }
    public void setUploadDir(String uploadDir) { this.uploadDir = uploadDir; }

    public Map<String, Size> getSizes() { return sizes; }
    public void setSizes(Map<String, Size> sizes) { this.sizes = sizes; }

    public long getMaxSize() { return maxSize; }
    public void setMaxSize(long maxSize) { this.maxSize = maxSize; }

    public List<String> getAllowedFormats() { return allowedFormats; }
    public void setAllowedFormats(List<String> allowedFormats) { this.allowedFormats = allowedFormats; }

    public static class Size {

        private int width;
        private int height;

        public int getWidth() { return width; }
        public void setWidth(int width) { this.width = width; }

        public int getHeight() { return height; }
        public void setHeight(int height) { this.height = height; }
    }
}
