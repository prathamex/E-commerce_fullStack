package com.cws.shop.dto.request;

import jakarta.validation.constraints.NotBlank;

public class PaymentMethodRequestDto {

    @NotBlank(message = "Method name is required")
    private String methodName;

    private Boolean active = true;

    // Getters and Setters

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}