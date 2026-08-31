package com.cws.shop.dto.request;

import com.cws.shop.validation.ValidMobileNumber;
import jakarta.validation.constraints.NotBlank;

public class ContactDTO {
	
	private String firstName;
    private String lastName;
    private String email;
    @NotBlank(message = "Mobile number is required")
    @ValidMobileNumber
    private String mobile;
    private String message;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    
    

}
