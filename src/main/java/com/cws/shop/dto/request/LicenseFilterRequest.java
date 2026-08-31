package com.cws.shop.dto.request;

import com.cws.shop.model.LicPlanType;
import com.cws.shop.model.ProductLicenseStatus;
import java.time.LocalDate;



public class LicenseFilterRequest {

    private ProductLicenseStatus activationStatus; 
    private String productName;                    
    private LicPlanType licenseType;               
    private LocalDate expiryDateFrom;              
    private LocalDate expiryDateTo;                

    public LicenseFilterRequest() {}

    public ProductLicenseStatus getActivationStatus() { return activationStatus; }
    public void setActivationStatus(ProductLicenseStatus activationStatus) {
        this.activationStatus = activationStatus; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public LicPlanType getLicenseType() { return licenseType; }
    public void setLicenseType(LicPlanType licenseType) { this.licenseType = licenseType; }

    public LocalDate getExpiryDateFrom() { return expiryDateFrom; }
    public void setExpiryDateFrom(LocalDate expiryDateFrom) {
        this.expiryDateFrom = expiryDateFrom; }

    public LocalDate getExpiryDateTo() { return expiryDateTo; }
    public void setExpiryDateTo(LocalDate expiryDateTo) {
        this.expiryDateTo = expiryDateTo; }
}
