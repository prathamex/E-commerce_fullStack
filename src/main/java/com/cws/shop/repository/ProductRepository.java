package com.cws.shop.repository;
 
import com.cws.shop.model.Product;
import com.cws.shop.model.ProductStatus;
import com.cws.shop.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
 
public interface ProductRepository extends JpaRepository<Product, Long>
{
    // Used in public detail page lookup by slug — also guards against deleted products
    Optional<Product> findBySlugAndDeletedFalse(String slug);
 
    // Used during slug uniqueness check — only non-deleted slugs are "taken"
    boolean existsBySlugAndDeletedFalse(String slug);
 
    //Added `p.deleted = false` to exclude soft-deleted products from admin list
    @Query(
        "SELECT p FROM Product p WHERE " +
        "p.deleted = false AND " +
        "(:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
        "(:status IS NULL OR p.status = :status)"
    )
    Page<Product> findByFilters(
            @Param("search") String search,
            @Param("status") ProductStatus status,
            Pageable pageable
    );
 
    //Added `p.deleted = false` to exclude soft-deleted products from public listing
    @Query(
        "SELECT p FROM Product p WHERE " +
        "p.deleted = false AND " +
        "p.status = 'PUBLISHED' AND " +
        "(:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))"
    )
    Page<Product> findPublishedBySearch(
            @Param("search") String search,
            Pageable pageable
    );
    
    @Query("""
            SELECT COALESCE(SUM(p.downloadCount), 0)
            FROM Product p
           """)
    Long getTotalDownloads();
    
    @Query(value = """
            SELECT * FROM products
            WHERE deleted = false
            AND LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """, nativeQuery = true)
    List<Product> searchProducts(@Param("keyword") String keyword);

    List<Product> findByNameContaining(String keyword);
    
    Optional<Product> findBySlug(String slug);

    boolean existsBySlug(String slug);
    
    List<Product> findTop3ByStatusOrderBySalesCountDesc(ProductStatus status);
	
}

