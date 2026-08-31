package com.cws.shop.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String methodName;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

  
}