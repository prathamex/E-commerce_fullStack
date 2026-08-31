package com.cws.shop.dto.request;

import java.util.List;

public class MergeCartRequest {

    private List<Long> productIds;

    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
}
