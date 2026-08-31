package com.cws.shop.dto.response;

public class ProductSearchResponse {

    private Long id;
    private String name;
    private Double price;
    private String overview;

    public ProductSearchResponse(Long id, String name, Double price, String overview) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.overview = overview;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getOverview() {
        return overview;
    }
}