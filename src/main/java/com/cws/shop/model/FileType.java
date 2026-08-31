package com.cws.shop.model;

public enum FileType {

    THUMBNAIL(
            "cws-products/thumbnails",
            new String[]{ "image/jpeg", "image/png", "image/webp" },
            10L * 1024 * 1024         // 10 MB  (was 5 MB)
    ),

    SCREENSHOT(
            "cws-products/screenshots",
            new String[]{ "image/jpeg", "image/png", "image/webp" },
            20L * 1024 * 1024         // 20 MB  (was 10 MB)
    ),

    VIDEO(
            "cws-products/videos",
            new String[]{ "video/mp4", "video/webm", "video/quicktime" },
            400L * 1024 * 1024        // 400 MB (was 200 MB)
    ),

    DOWNLOAD_FILE(
            "cws-products/downloads",
            new String[]{
                "application/zip",
                "application/x-zip-compressed",
                "application/java-archive",
                "application/octet-stream",
                "application/x-msdownload"
            },
            1024L * 1024 * 1024       // 1 GB   (was 500 MB)
    ),
    
    PROFILE_IMAGE(
            "cws-users/profile-images",
            new String[]{ "image/jpeg", "image/png", "image/webp" },
            10L * 1024 * 1024  
           
    ),

    DOCUMENTATION(
            "cws-products/docs",
            new String[]{ "application/pdf" },
            40L * 1024 * 1024         // 40 MB  (was 20 MB)
    );

    private final String   folder;
    private final String[] allowedMimeTypes;
    private final long     maxSizeBytes;

    FileType(String folder, String[] allowedMimeTypes, long maxSizeBytes) {
        this.folder           = folder;
        this.allowedMimeTypes = allowedMimeTypes;
        this.maxSizeBytes     = maxSizeBytes;
    }

    public String   getFolder()           { return folder; }
    public String[] getAllowedMimeTypes() { return allowedMimeTypes; }
    public long     getMaxSizeBytes()    { return maxSizeBytes; }
}