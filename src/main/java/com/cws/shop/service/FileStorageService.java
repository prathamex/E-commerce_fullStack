package com.cws.shop.service;

import com.cws.shop.model.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {

    // Upload a single file -- returns its public HTTPS URL
    String uploadFile(MultipartFile file, FileType fileType);

    // Upload multiple files -- returns list of HTTPS URLs
    List<String> uploadMultiple(List<MultipartFile> files, FileType fileType);

    // Generate a time-limited signed URL for private download files
    // Called only after license validation passes
    String generateSecureDownloadUrl(String publicId, int expiresInSecs);

    // Delete a file from Cloudinary by its URL
    // Called when admin removes or replaces a file
    void deleteFile(String fileUrl, FileType fileType);
}