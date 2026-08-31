package com.cws.shop.dto.response;

import java.time.LocalDateTime;

import com.cws.shop.model.Role;
import com.cws.shop.model.UserStatus;

public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String mobileNumber;
    private Role role;
    private UserStatus status;
    private LocalDateTime updatedAt;
    private long totalOrders;
    public UserDto() {
	}
    
    
  
	

	public UserDto(Long id, String name, String email, String mobileNumber, Role role, UserStatus status,
			LocalDateTime updatedAt, long totalOrders) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.role = role;
		this.status = status;
		this.updatedAt = updatedAt;
		this.totalOrders = totalOrders;
	}
	
	
	





	public UserDto(Long id, String name, String email, String mobileNumber, Role role, UserStatus status,
			LocalDateTime updatedAt) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.role = role;
		this.status = status;
		this.updatedAt = updatedAt;
	}





	public long getTotalOrders() {
		return totalOrders;
	}

    public void setTotalOrders(long totalOrders) {
		this.totalOrders = totalOrders;
	}

   public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public LocalDateTime getUpdatedAt() {
	    return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
	    this.updatedAt = updatedAt;
	}
	
	
}
