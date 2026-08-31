package com.cws.shop.dto.request;

public class UpdateProfileRequest {
	private String name;
    private String mobileNumber;
    private String companyName;
    private String profileImage;
    
    
    
    public UpdateProfileRequest() {
	}
    
    public UpdateProfileRequest(String name, String mobileNumber, String companyName, String profileImage) {
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.companyName = companyName;
		this.profileImage = profileImage;
	}

    public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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


	public String getProfileImage() {
		return profileImage;
	}


	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	

}
