package com.cws.shop.dto.response;

public class PasswordPolicyDto {

    private int minLength;
    private int maxLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigits;
    private boolean requireSpecialCharacters;
    private String description;

    public PasswordPolicyDto() {}

    public PasswordPolicyDto(int minLength, int maxLength, boolean requireUppercase, boolean requireLowercase,
                             boolean requireDigits, boolean requireSpecialCharacters, String description) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.requireUppercase = requireUppercase;
        this.requireLowercase = requireLowercase;
        this.requireDigits = requireDigits;
        this.requireSpecialCharacters = requireSpecialCharacters;
        this.description = description;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isRequireUppercase() {
        return requireUppercase;
    }

    public void setRequireUppercase(boolean requireUppercase) {
        this.requireUppercase = requireUppercase;
    }

    public boolean isRequireLowercase() {
        return requireLowercase;
    }

    public void setRequireLowercase(boolean requireLowercase) {
        this.requireLowercase = requireLowercase;
    }

    public boolean isRequireDigits() {
        return requireDigits;
    }

    public void setRequireDigits(boolean requireDigits) {
        this.requireDigits = requireDigits;
    }

    public boolean isRequireSpecialCharacters() {
        return requireSpecialCharacters;
    }

    public void setRequireSpecialCharacters(boolean requireSpecialCharacters) {
        this.requireSpecialCharacters = requireSpecialCharacters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
