package com.cws.shop.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cws.shop.model.FileType;
import com.cws.shop.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Cloudinary cloudinary;

    public FileStorageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

   
    @Override
    public String uploadFile(MultipartFile file, FileType fileType) {

        validateFile(file, fileType);

        try {
            Map<String, Object> options = buildUploadOptions(file, fileType);
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), options);
            return (String) result.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException("File upload failed. Please try again.", e);
        }
    }

  
    @Override
    public List<String> uploadMultiple(List<MultipartFile> files, FileType fileType) {

        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("No files provided.");
        }
        if (files.size() > 10) {
            throw new IllegalArgumentException("Maximum 10 files allowed per upload.");
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadFile(file, fileType));
        }
        return urls;
    }

    @Override
    public String generateSecureDownloadUrl(String publicId, int expiresInSecs) {

        try {
            long expireAt = (System.currentTimeMillis() / 1000L) + expiresInSecs;

            return cloudinary.privateDownload(
                    publicId,
                    null,
                    ObjectUtils.asMap(
                            "resource_type", "raw",
                            "type",          "authenticated",
                            "expires_at",    expireAt
                    )
            );

        } catch (Exception e) {
            throw new RuntimeException("Could not generate secure download URL.", e);
        }
    }


    @Override
    public void deleteFile(String fileUrl, FileType fileType) {

        try {
            String publicId     = extractPublicId(fileUrl);
            String resourceType = isVideo(fileType) ? "video"
                                : isRaw(fileType)   ? "raw"
                                : "image";

            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap("resource_type", resourceType)
            );

        } catch (Exception e) {
            System.err.println("Warning: Could not delete file from Cloudinary: " + fileUrl);
        }
    }

   
    private void validateFile(MultipartFile file, FileType fileType) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty.");
        }

        if (file.getSize() > fileType.getMaxSizeBytes()) {
            long maxMB = fileType.getMaxSizeBytes() / (1024 * 1024);
            throw new IllegalArgumentException(
                    "File is too large. Max size for " + fileType.name() + " is " + maxMB + " MB."
            );
        }

        String contentType = file.getContentType();
        boolean allowed = Arrays.stream(fileType.getAllowedMimeTypes())
                                .anyMatch(type -> type.equalsIgnoreCase(contentType));

        if (!allowed) {
            throw new IllegalArgumentException(
                    "File type '" + contentType + "' is not allowed for " + fileType.name() + ". "
                    + "Allowed types: " + String.join(", ", fileType.getAllowedMimeTypes())
            );
        }
    }

    private Map<String, Object> buildUploadOptions(MultipartFile file, FileType fileType) {

        Map<String, Object> options = new HashMap<>();

        // Put file into correct folder in Cloudinary
        options.put("folder", fileType.getFolder());

        // Give the file a unique name
        String original       = file.getOriginalFilename();
        String nameWithoutExt = (original != null && original.contains("."))
                ? original.substring(0, original.lastIndexOf("."))
                : (original != null ? original : "file");

        options.put("public_id", UUID.randomUUID() + "_" + sanitize(nameWithoutExt));

        // Set resource type based on file category
        if (isVideo(fileType)) {
            options.put("resource_type", "video");

        } else if (isRaw(fileType)) {
            options.put("resource_type", "raw");

            // Download files are stored PRIVATE -- accessed only via signed URLs
            if (fileType == FileType.DOWNLOAD_FILE) {
                options.put("type", "authenticated");
            }

        } else {
            // Images: auto-compress + convert to WebP where browser supports it
            options.put("resource_type", "image");
            options.put("quality",       "auto:good");
            options.put("fetch_format",  "auto");
        }

        return options;
    }

    private boolean isVideo(FileType fileType) {
        return fileType == FileType.VIDEO;
    }

    private boolean isRaw(FileType fileType) {
        return fileType == FileType.DOWNLOAD_FILE
            || fileType == FileType.DOCUMENTATION;
    }

    private String sanitize(String name) {
        if (name == null) return "file";
        return name.replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase();
    }

    private String extractPublicId(String fileUrl) {
        // Cloudinary URL: https://res.cloudinary.com/{cloud}/image/upload/v123/{folder}/{id}.ext
        String[] parts = fileUrl.split("/upload/");
        if (parts.length < 2) return fileUrl;

        String path = parts[1].replaceFirst("v\\d+/", "");
        int dot = path.lastIndexOf(".");
        return dot > 0 ? path.substring(0, dot) : path;
    }
}