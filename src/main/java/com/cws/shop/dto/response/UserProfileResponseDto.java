package com.cws.shop.dto.response;

import java.time.LocalDateTime;

import com.cws.shop.model.UserStatus;

public class UserProfileResponseDto {

    private String name;
    private String email;
    private String mobileNumber;
    private String companyName;
    private String profileImage;
    private UserStatus status;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLogin;

    public UserProfileResponseDto() {
    }

    public UserProfileResponseDto(String name, String email, String mobileNumber,
            String companyName, String profileImage, UserStatus status,
            LocalDateTime registrationDate, LocalDateTime lastLogin) {

        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.companyName = companyName;
        this.profileImage = profileImage;
        this.status = status;
        this.registrationDate = registrationDate;
        this.lastLogin = lastLogin;
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}