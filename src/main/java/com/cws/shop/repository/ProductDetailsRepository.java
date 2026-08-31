package com.cws.shop.repository;

import com.cws.shop.model.Product;
import com.cws.shop.model.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {

   

	@Query(value = """
	        SELECT * FROM products
	        WHERE deleted = false
	        AND LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%'))
	        """, nativeQuery = true)
	List<Product> searchProducts(@Param("keyword") String keyword);
	
	Optional<ProductDetails> findByProductId(Long productId);

}
