package com.cws.shop.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cws.shop.dto.response.MyProductResponse;
import com.cws.shop.model.Product;
import com.cws.shop.model.ProductDetails;
import com.cws.shop.model.PurchasedProduct;
import com.cws.shop.repository.PurchasedProductRepository;
import com.cws.shop.service.PurchasedProductService;

@Service
public class PurchasedProductServiceImpl implements PurchasedProductService {

    private final PurchasedProductRepository purchasedProductRepository;

    public PurchasedProductServiceImpl(PurchasedProductRepository purchasedProductRepository) {
        this.purchasedProductRepository = purchasedProductRepository;
    }

    @Override
    public List<MyProductResponse> getMyProducts(Long userId) {

        List<PurchasedProduct> purchasedProducts =
                purchasedProductRepository.findByUserIdAndActiveTrue(userId);

        List<MyProductResponse> response = new ArrayList<>();

        for (PurchasedProduct purchasedProduct : purchasedProducts) {

            Product product = purchasedProduct.getProduct();

            MyProductResponse dto = new MyProductResponse();

            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setCategory(product.getCategory().name());
            dto.setVersion(product.getCurrentVersion());
            dto.setThumbnailImage(product.getThumbnailImage());
            dto.setPurchaseDate(purchasedProduct.getPurchaseDate());

            dto.setDownloadUrl("/user/products/" + product.getId() + "/download");

            dto.setInvoiceId(purchasedProduct.getOrder().getId());

            response.add(dto);
        }

        return response;
    }

    @Override
    public String downloadProduct(Long userId, Long productId) {
    	
    	PurchasedProduct purchasedProduct = purchasedProductRepository
        .findFirstByUserIdAndProductIdAndActiveTrueOrderByPurchaseDateDesc(userId, productId)
        .orElseThrow(() ->
            new RuntimeException("You have not purchased this product."));

       

        purchasedProduct.setDownloadCount(
                purchasedProduct.getDownloadCount() + 1);

        purchasedProduct.setLastDownloadedAt(LocalDateTime.now());

        purchasedProductRepository.save(purchasedProduct);

        ProductDetails details = purchasedProduct.getProduct().getDetails();

        if (details == null) {
            throw new RuntimeException("Product details not found.");
        }

        if (details.getDownloadFileUrl() == null
                || details.getDownloadFileUrl().isBlank()) {
            throw new RuntimeException("Download file is not available.");
        }

        return details.getDownloadFileUrl();
    }

	
}