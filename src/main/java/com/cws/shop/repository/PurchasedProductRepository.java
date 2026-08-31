package com.cws.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.shop.model.PurchasedProduct;

public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct, Long> {

	List<PurchasedProduct> findByUserIdAndActiveTrue(Long userId);

	Optional<PurchasedProduct> findFirstByUserIdAndProductIdAndActiveTrueOrderByPurchaseDateDesc(
	        Long userId,
	        Long productId);

    List<PurchasedProduct> findByOrderId(Long orderId);

    List<PurchasedProduct> findByProductId(Long productId);

}
