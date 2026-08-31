package com.cws.shop.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;


public class RenewLicenseRequest {

    @NotNull(message = "New expiry date is required")
    @Future(message = "New expiry date must be in the future")
    private LocalDate newExpiryDate;

    public RenewLicenseRequest() {}

    public LocalDate getNewExpiryDate() { return newExpiryDate; }
    public void setNewExpiryDate(LocalDate newExpiryDate) {
        this.newExpiryDate = newExpiryDate;
    }
}