package com.cws.shop.dto.response;

import com.cws.shop.model.LicPlanType;
import com.cws.shop.model.ProductLicensePlan;
import com.cws.shop.model.ProductLicenseStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;



public class LicenseDetailResponse {

    private Long id;
    private String licenseKey;

    
    private String productName;
    private LicPlanType licenseType;
    private ProductLicensePlan licensePlan;

    
    private Long assignedUserId;
    private String assignedUserName;
    private String customerName;
    private String customerEmail;
    private String mobileNumber;
    private String companyName;

    
    private Boolean manualKey;
    private Integer activationLimit;
    private ProductLicenseStatus licenseStatus;
    private LocalDate activationDate;
    private LocalDate expiryDate;

    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LicenseDetailResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLicenseKey() { return licenseKey; }
    public void setLicenseKey(String licenseKey) { this.licenseKey = licenseKey; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public LicPlanType getLicenseType() { return licenseType; }
    public void setLicenseType(LicPlanType licenseType) { this.licenseType = licenseType; }

    public ProductLicensePlan getLicensePlan() { return licensePlan; }
    public void setLicensePlan(ProductLicensePlan licensePlan) { this.licensePlan = licensePlan; }

    public Long getAssignedUserId() { return assignedUserId; }
    public void setAssignedUserId(Long assignedUserId) { this.assignedUserId = assignedUserId; }

    public String getAssignedUserName() { return assignedUserName; }
    public void setAssignedUserName(String assignedUserName) { this.assignedUserName = assignedUserName; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Boolean getManualKey() { return manualKey; }
    public void setManualKey(Boolean manualKey) { this.manualKey = manualKey; }

    public Integer getActivationLimit() { return activationLimit; }
    public void setActivationLimit(Integer activationLimit) { this.activationLimit = activationLimit; }

    public ProductLicenseStatus getLicenseStatus() { return licenseStatus; }
    public void setLicenseStatus(ProductLicenseStatus licenseStatus) { this.licenseStatus = licenseStatus; }

    public LocalDate getActivationDate() { return activationDate; }
    public void setActivationDate(LocalDate activationDate) { this.activationDate = activationDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}