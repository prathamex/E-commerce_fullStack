package com.cws.shop.service;

import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.model.UserDocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    public ApiResponse processAndSave(MultipartFile file, String description) throws IOException;

    ApiResponse<List<UserDocumentResponse>> getFileDocumentsByUserId(Long userId);
}
