package com.cws.shop.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import com.cws.shop.validation.ValidMobileNumber;
import jakarta.validation.constraints.Size;

public class UpdateUserRequestDto {
	@Size(min = 3, max = 100)
	@Pattern(regexp = "^[\\p{L} .'-]+$", message = "Name must contain only letters, spaces, apostrophes or hyphens")
	private String name;
    
	@ValidMobileNumber
	@NotBlank(message = "Mobile Number is required")
	private String mobileNumber;
    
	private String status;

	public UpdateUserRequestDto() {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
