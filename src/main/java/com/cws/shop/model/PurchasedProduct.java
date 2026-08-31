package com.cws.shop.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "purchased_products")
public class PurchasedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who purchased the product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Order through which the product was purchased
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Purchased product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Price paid during purchase
    @Column(nullable = false)
    private Double purchasePrice;

    // Date and time of purchase
    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    // Number of times the product has been downloaded
    @Column(nullable = false)
    private Integer downloadCount = 0;

    // Last download timestamp
    private LocalDateTime lastDownloadedAt;

    // License status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductLicenseStatus licenseStatus = ProductLicenseStatus.ACTIVE;

    // Ownership status
    @Column(nullable = false)
    private Boolean active = true;

    // Record creation timestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Record update timestamp
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.purchaseDate == null) {
            this.purchaseDate = now;
        }

        if (this.downloadCount == null) {
            this.downloadCount = 0;
        }

        if (this.active == null) {
            this.active = true;
        }

        if (this.licenseStatus == null) {
            this.licenseStatus = ProductLicenseStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public PurchasedProduct() {
    }

    public PurchasedProduct(Long id, User user, Order order, Product product,
                            Double purchasePrice, LocalDateTime purchaseDate,
                            Integer downloadCount, LocalDateTime lastDownloadedAt,
                            ProductLicenseStatus licenseStatus, Boolean active,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.order = order;
        this.product = product;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
        this.downloadCount = downloadCount;
        this.lastDownloadedAt = lastDownloadedAt;
        this.licenseStatus = licenseStatus;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public LocalDateTime getLastDownloadedAt() {
        return lastDownloadedAt;
    }

    public void setLastDownloadedAt(LocalDateTime lastDownloadedAt) {
        this.lastDownloadedAt = lastDownloadedAt;
    }

    public ProductLicenseStatus getLicenseStatus() {
        return licenseStatus;
    }

    public void setLicenseStatus(ProductLicenseStatus licenseStatus) {
        this.licenseStatus = licenseStatus;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}