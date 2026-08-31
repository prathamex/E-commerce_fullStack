package com.cws.shop.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "licenses")
public class GenerateNewLicense extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //PRODUCT INFORMATION 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_type", nullable = false)
    private LicPlanType licenseType;        

    @Enumerated(EnumType.STRING)
    @Column(name = "license_plan")
    private ProductLicensePlan licensePlan; 

    // USER / CUSTOMER DETAILS

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User assignedUser;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "company_name")
    private String companyName;             // optional

    // LICENSE CONFIG

    @Column(name = "license_key", nullable = false, unique = true)
    private String licenseKey;

    @Column(name = "is_manual_key")
    private Boolean manualKey = false;      

    @Column(name = "activation_limit", nullable = false)
    private Integer activationLimit;        

    @Enumerated(EnumType.STRING)
    @Column(name = "license_status", nullable = false)
    private ProductLicenseStatus licenseStatus;

    @Column(name = "activation_date")
    private LocalDate activationDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    // AUDIT 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by")
    private User generatedBy;               

    // CONSTRUCTORS 

    public GenerateNewLicense() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
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

	public ProductLicenseStatus getLicenseStatus() {
		return licenseStatus;
	}

	public void setLicenseStatus(ProductLicenseStatus licenseStatus) {
		this.licenseStatus = licenseStatus;
	}

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

	public User getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(User generatedBy) {
		this.generatedBy = generatedBy;
	}

    
	

}