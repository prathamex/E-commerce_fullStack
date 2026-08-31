package com.cws.shop.controller;

import com.cws.shop.dto.response.ApiResponse;

import com.cws.shop.model.FileType;
import com.cws.shop.model.UserDocumentMapping;
import com.cws.shop.model.UserDocumentResponse;
import com.cws.shop.service.FileStorageService;
import com.cws.shop.dto.response.FileResponse;
import com.cws.shop.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/upload")
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final FileService fileService;

    public FileUploadController(FileStorageService fileStorageService, FileService fileService) {
        this.fileStorageService = fileStorageService;
        this.fileService = fileService;
    }
    
    @PostMapping("/profile-image")
    public ResponseEntity<ApiResponse<String>> uploadProfileImage(
            @RequestParam("file") MultipartFile file) {

        String url = fileStorageService.uploadFile(file, FileType.PROFILE_IMAGE);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Profile image uploaded successfully.",
                        url
                )
        );
    }

    // -------------------------------------------------------
    @PostMapping("/thumbnail")
    public ResponseEntity<ApiResponse<String>> uploadThumbnail(
            @RequestParam("file") MultipartFile file) {

        String url = fileStorageService.uploadFile(file, FileType.THUMBNAIL);
        return ResponseEntity.ok(new ApiResponse<>(true, "Thumbnail uploaded successfully.", url));
    }

    // -------------------------------------------------------
    @PostMapping("/screenshots")
    public ResponseEntity<ApiResponse<List<String>>> uploadScreenshots(
            @RequestParam("files") List<MultipartFile> files) {

        List<String> urls = fileStorageService.uploadMultiple(files, FileType.SCREENSHOT);
        return ResponseEntity.ok(new ApiResponse<>(true,
                urls.size() + " screenshot(s) uploaded successfully.", urls));
    }

    // -------------------------------------------------------
    @PostMapping("/videos")
    public ResponseEntity<ApiResponse<List<String>>> uploadVideos(
            @RequestParam("files") List<MultipartFile> files) {

        List<String> urls = fileStorageService.uploadMultiple(files, FileType.VIDEO);
        return ResponseEntity.ok(new ApiResponse<>(true,
                urls.size() + " video(s) uploaded successfully.", urls));
    }

    @PostMapping("/download-file")
    public ResponseEntity<ApiResponse<String>> uploadDownloadFile(
            @RequestParam("file") MultipartFile file) {

        String url = fileStorageService.uploadFile(file, FileType.DOWNLOAD_FILE);
        return ResponseEntity.ok(new ApiResponse<>(true, "Download file uploaded and secured.", url));
    }

    @PostMapping("/documentation")
    public ResponseEntity<ApiResponse<String>> uploadDocumentation(
            @RequestParam("file") MultipartFile file) {

        String url = fileStorageService.uploadFile(file, FileType.DOCUMENTATION);
        return ResponseEntity.ok(new ApiResponse<>(true, "Documentation uploaded successfully.", url));
    }

    @PostMapping("/file-upload")
    public ResponseEntity<ApiResponse<FileResponse>> uploadImage(@RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) throws IOException {
        ApiResponse apiResponse = fileService.processAndSave(file, description);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/documents/{userId}")
    public ResponseEntity<ApiResponse<List<UserDocumentResponse>>> getFileDocumentsByUserId(@PathVariable Long userId) {
        ApiResponse<List<UserDocumentResponse>> response = fileService.getFileDocumentsByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
