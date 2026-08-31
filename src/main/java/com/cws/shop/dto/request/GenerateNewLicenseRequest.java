package com.cws.shop.dto.request;

import com.cws.shop.model.LicPlanType;
//import com.cws.shop.model.ProductLicenseStatus;
import com.cws.shop.model.ProductLicensePlan;
import jakarta.validation.constraints.Email;
import com.cws.shop.validation.ValidMobileNumber;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class GenerateNewLicenseRequest {

   
    @NotNull(message = "Product id is required")
    private Long productId;

    @NotNull(message = "License type is required")
    private LicPlanType licenseType;

    private ProductLicensePlan licensePlan;      
    
    private Long assignedUserId;                 

    
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

	@NotBlank(message = "Mobile number is required")
	// Keep both validations: pattern (exact 10 digits) and custom mobile validator
	@Pattern(regexp = "\\d{10}", message = "Mobile number must be exactly 10 numeric digits")
	@ValidMobileNumber
	private String mobileNumber;
    
    
    private String companyName;                  

    private String licenseKey;                   

    private Boolean manualKey = false;           

    @NotNull(message = "Activation limit is required")
    @Min(value = 1, message = "Minimum 1 device")
    @Max(value = 3, message = "Maximum 3 devices")
    private Integer activationLimit;

   /* @NotNull(message = "License status is required")
    private ProductLicenseStatus licenseStatus; */
    
    @NotNull(message = "Activation date is required")
    private LocalDate activationDate;
    
    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;
    
    
    //Getters Setters
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public LicPlanType getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(LicPlanType licenseType) {
		this.licenseType = licenseType;
	}

	public ProductLicensePlan getLicensePlan() {
		return licensePlan;
	}

	public void setLicensePlan(ProductLicensePlan licensePlan) {
		this.licensePlan = licensePlan;
	}

	public Long getAssignedUserId() {
		return assignedUserId;
	}

	public void setAssignedUserId(Long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLicenseKey() {
		return licenseKey;
	}

	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
	}

	public Boolean getManualKey() {
		return manualKey;
	}

	public void setManualKey(Boolean manualKey) {
		this.manualKey = manualKey;
	}

	public Integer getActivationLimit() {
		return activationLimit;
	}

	public void setActivationLimit(Integer activationLimit) {
		this.activationLimit = activationLimit;
	}

	/*public ProductLicenseStatus getLicenseStatus() {
		return licenseStatus;
	}

	public void setLicenseStatus(ProductLicenseStatus licenseStatus) {
		this.licenseStatus = licenseStatus;
	}*/

	public LocalDate getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(LocalDate activationDate) {
		this.activationDate = activationDate;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

    
    
    
   

}
