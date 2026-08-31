package com.cws.shop.dto.request;

import jakarta.validation.constraints.Min;

public class UpdateCartQuantityRequest {
	
	
	@Min(value = 1, message = "Quantity must be greater than 0")
	private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
