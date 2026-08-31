package com.cws.shop.dto.response;

import com.cws.shop.model.ProductLicenseStatus;
import java.time.LocalDate;

public class LicenseDashboardResponse {

    private Long id;
    private String licenseId;        
    private String productName;
    private String userAssigned;     // customerName
    private ProductLicenseStatus activationStatus;
    private LocalDate expiryDate;

    public LicenseDashboardResponse() {}

    public LicenseDashboardResponse(Long id,
                                    String licenseId,
                                    String productName,
                                    String userAssigned,
                                    ProductLicenseStatus activationStatus,
                                    LocalDate expiryDate) {
        this.id = id;
        this.licenseId = licenseId;
        this.productName = productName;
        this.userAssigned = userAssigned;
        this.activationStatus = activationStatus;
        this.expiryDate = expiryDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLicenseId() { return licenseId; }
    public void setLicenseId(String licenseId) { this.licenseId = licenseId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getUserAssigned() { return userAssigned; }
    public void setUserAssigned(String userAssigned) { this.userAssigned = userAssigned; }

    public ProductLicenseStatus getActivationStatus() { return activationStatus; }
    public void setActivationStatus(ProductLicenseStatus activationStatus) {
        this.activationStatus = activationStatus;
    }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}