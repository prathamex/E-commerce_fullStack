package com.cws.shop.dto.response;


public class DashboardSummaryResponse {

    private long purchasedProducts;
    private long recentDownloads;
    private long activeLicenses;
    private long totalOrders;

    public DashboardSummaryResponse() {
    }

    public DashboardSummaryResponse(long purchasedProducts,
                                    long recentDownloads,
                                    long activeLicenses,
                                    long totalOrders) {
        this.purchasedProducts = purchasedProducts;
        this.recentDownloads = recentDownloads;
        this.activeLicenses = activeLicenses;
        this.totalOrders = totalOrders;
    }

    public long getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(long purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public long getRecentDownloads() {
        return recentDownloads;
    }

    public void setRecentDownloads(long recentDownloads) {
        this.recentDownloads = recentDownloads;
    }

    public long getActiveLicenses() {
        return activeLicenses;
    }

    public void setActiveLicenses(long activeLicenses) {
        this.activeLicenses = activeLicenses;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }
}