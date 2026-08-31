package com.cws.shop.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class PasswordResetLimit {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private int resetCount;

    private LocalDate resetDate;
    
    public PasswordResetLimit() {
	}
    

	public PasswordResetLimit(Long id, String email, int resetCount, LocalDate resetDate) {
		this.id = id;
		this.email = email;
		this.resetCount = resetCount;
		this.resetDate = resetDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getResetCount() {
		return resetCount;
	}

	public void setResetCount(int resetCount) {
		this.resetCount = resetCount;
	}

	public LocalDate getResetDate() {
		return resetDate;
	}

	public void setResetDate(LocalDate resetDate) {
		this.resetDate = resetDate;
	}

    
    
}
