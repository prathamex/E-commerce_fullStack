package com.cws.shop.service;

import com.cws.shop.dto.request.GenerateNewLicenseRequest;
import com.cws.shop.dto.request.LicenseFilterRequest;
import com.cws.shop.dto.request.RenewLicenseRequest;
import com.cws.shop.dto.response.GenerateNewLicenseResponse;
import com.cws.shop.dto.response.LicenseDashboardResponse;
import com.cws.shop.dto.response.LicenseDetailResponse;
import com.cws.shop.dto.response.PagedLicenseResponse;

import java.util.List;

public interface GenerateNewLicenseService {

    
    GenerateNewLicenseResponse generateLicense(GenerateNewLicenseRequest request);

    PagedLicenseResponse getAllLicenses(int page, int size);

    List<LicenseDashboardResponse> searchLicenses(String keyword);

    PagedLicenseResponse filterLicenses(LicenseFilterRequest filter, int page, int size);
    //actions
    LicenseDetailResponse viewLicense(Long id);
    void deleteLicense(Long id);
    LicenseDashboardResponse activateLicense(Long id);
    LicenseDashboardResponse suspendLicense(Long id);
    LicenseDashboardResponse renewLicense(Long id, RenewLicenseRequest request);
}