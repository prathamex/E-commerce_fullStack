package com.cws.shop.dto.response;

import com.cws.shop.model.ProductLicenseStatus;
import java.time.LocalDate;

public class GenerateNewLicenseResponse {
	

    private String productName;
    private String licenseKey;
	private String customerName;
    private ProductLicenseStatus licenseStatus;
    private LocalDate expiryDate;

    //Constructors
    
    
    public GenerateNewLicenseResponse() {
    	
    }

   
    public GenerateNewLicenseResponse(
            String productName,
            String licenseKey,
			String customerName,
            ProductLicenseStatus licenseStatus,
            LocalDate expiryDate) {
        this.productName = productName;
        this.licenseKey = licenseKey;
		this.customerName = customerName;
        this.licenseStatus = licenseStatus;
        this.expiryDate = expiryDate;
    }

	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLicenseKey() {
		return licenseKey;
	}
	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
	}

	public String getCustomerName() { return customerName; }
	public void setCustomerName(String customerName) { this.customerName = customerName; }

	public ProductLicenseStatus getLicenseStatus() {
		return licenseStatus;
	}
	public void setLicenseStatus(ProductLicenseStatus licenseStatus)
	{
		this.licenseStatus = licenseStatus;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

    

}
