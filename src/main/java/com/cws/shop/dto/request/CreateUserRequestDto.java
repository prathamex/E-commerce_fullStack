package com.cws.shop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import com.cws.shop.validation.ValidMobileNumber;
import jakarta.validation.constraints.Size;

 public class CreateUserRequestDto {
 	 @NotBlank(message = "Name is required")
 	    @Size(min = 3, max = 100)
 	    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Name must contain only letters, spaces, apostrophes or hyphens")
 	    private String name;

	    @Email(message = "Invalid email")
	    @NotBlank(message = "Email is required")
	    private String email;

					@NotBlank(message = "Mobile Number is required")
					@ValidMobileNumber
					private String mobileNumber;

					@NotBlank(message = "Password is required")
					@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,20}$",
							message = "Password must be 8-20 characters and contain at least one uppercase letter, one lowercase letter, one number and one special character")
					private String password;

	    public CreateUserRequestDto() {
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getMobileNumber() {
	        return mobileNumber;
	    }

	    public void setMobileNumber(String mobileNumber) {
	        this.mobileNumber = mobileNumber;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }
	
}
