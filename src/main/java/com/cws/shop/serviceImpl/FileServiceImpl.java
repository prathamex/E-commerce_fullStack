package com.cws.shop.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cws.shop.config.ImageProperties;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.FileResponse;
import com.cws.shop.exception.FileStorageException;
import com.cws.shop.exception.ImageProcessingException;
import com.cws.shop.exception.InvalidImageFormatException;
import com.cws.shop.exception.UnauthorizedException;
import com.cws.shop.model.User;
import com.cws.shop.model.UserDocumentMapping;
import com.cws.shop.model.UserDocumentResponse;
import com.cws.shop.repository.UserDocumentRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.FileService;
import net.coobird.thumbnailator.Thumbnails;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private static final String ROOT_FOLDER = "cws-products"; // ✅ STATIC ROOT
    private ImageProperties imageProperties;
    private Cloudinary cloudinary;

    private UserRepository userRepository;
    private UserDocumentRepository userDocumentRepository;

    public FileServiceImpl(ImageProperties imageProperties, Cloudinary cloudinary,
            UserRepository userRepository, UserDocumentRepository userDocumentRepository) {
        this.imageProperties = imageProperties;
        this.cloudinary = cloudinary;
        this.userRepository = userRepository;
        this.userDocumentRepository = userDocumentRepository;
    }

    @Override
    public ApiResponse<FileResponse> processAndSave(MultipartFile file, String description) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new FileStorageException("File name is invalid");
        }
        String extension = getExtension(originalFilename);
        validateFormat(extension);
        if (file.getSize() > imageProperties.getMaxSize()) {
            throw new FileStorageException("File size exceeds max allowed size");
        }
        try {
            // =========================
            // Build base folder path
            // =========================
            String baseFolder = ROOT_FOLDER + "/" + description;
            // =========================
            // Upload ORIGINAL
            // =========================
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", baseFolder + "/original",
                            "resource_type", "auto"));
            String originalUrl = uploadResult.get("secure_url").toString();

            User user = userRepository.findById(getAuthenticatedUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // =========================
            // Resize + Upload
            // =========================
            Map<String, String> resizedPaths = new HashMap<>();
            List<UserDocumentMapping> userDocumentMappingList = new ArrayList<>();
            for (Map.Entry<String, ImageProperties.Size> entry : imageProperties.getSizes().entrySet()) {
                String sizeName = entry.getKey();
                ImageProperties.Size size = entry.getValue();
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Thumbnails.of(file.getInputStream())
                            .size(size.getWidth(), size.getHeight())
                            .outputQuality(0.9)
                            .keepAspectRatio(true)
                            .toOutputStream(baos);
                    Map resizedUpload = cloudinary.uploader().upload(
                            baos.toByteArray(),
                            ObjectUtils.asMap(
                                    "folder", baseFolder + "/" + sizeName,
                                    "resource_type", "auto"));
                    String url = resizedUpload.get("secure_url").toString();
                    resizedPaths.put(sizeName, url);
                    // =========================
                    // Create UserDocumentMapping Object
                    // =========================
                    UserDocumentMapping userDocumentMapping = new UserDocumentMapping();
                    userDocumentMapping.setUser(user);
                    userDocumentMapping.setOrignalUrl(originalUrl);
                    switch (sizeName) {
                        case "large":
                            userDocumentMapping.setUrl(url);
                            userDocumentMapping.setUrlType(sizeName);
                            break;
                        case "medium":
                            userDocumentMapping.setUrl(url);
                            userDocumentMapping.setUrlType(sizeName);
                            break;
                        case "thumbnail":
                            userDocumentMapping.setUrl(url);
                            userDocumentMapping.setUrlType(sizeName);
                            break;
                    }
                    userDocumentMappingList.add(userDocumentMapping);
                } catch (IOException e) {
                    throw new ImageProcessingException(
                            "Failed to resize & upload image for size: " + sizeName, e);
                }
            }

            // =========================
            // SAVE TO DATABASE
            // =========================
            userDocumentRepository.saveAll(userDocumentMappingList);
            // =========================
            // Response
            // =========================
            FileResponse response = new FileResponse();
            response.setOriginal(originalUrl);
            response.setResizedImages(resizedPaths);
            ApiResponse<FileResponse> apiResponse = new ApiResponse<>();
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Uploaded File Resized Successfully");
            apiResponse.setData(response);
            return apiResponse;
        } catch (Exception e) {
            throw new FileStorageException("Cloudinary upload failed", e);
        }
    }

    @Override
    public ApiResponse<List<UserDocumentResponse>> getFileDocumentsByUserId(Long userId) {
        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Fetch documents
        List<UserDocumentMapping> UserDocumentResponse = userDocumentRepository.findByUser(user);
        // Convert Entity → DT
        List<UserDocumentResponse> collect = UserDocumentResponse.stream()
                .map(doc -> new UserDocumentResponse(
                        doc.getId(),
                        doc.getUrl(),
                        doc.getOrignalUrl(),
                        doc.getUrlType(),
                        doc.getCreatedAt(),
                        doc.getUpdatedAt()))
                .collect(Collectors.toList());
        ApiResponse<List<UserDocumentResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("File Retrived Successfully");
        apiResponse.setData(collect);
        return apiResponse;
    }

    // =========================
    // Helper Methods
    // =========================
    private String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            throw new InvalidImageFormatException("Invalid file format");
        }
        return fileName.substring(lastIndex + 1).toLowerCase();
    }

    private void validateFormat(String extension) {
        if (!imageProperties.getAllowedFormats().contains(extension)) {
            throw new InvalidImageFormatException("Unsupported file format: " + extension);
        }
    }

    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return ((User) auth.getPrincipal()).getId();
        }
        throw new UnauthorizedException("Authentication required");
    }
}