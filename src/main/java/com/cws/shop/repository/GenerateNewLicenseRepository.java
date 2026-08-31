package com.cws.shop.repository;

import com.cws.shop.model.GenerateNewLicense;
import com.cws.shop.model.LicPlanType;
import com.cws.shop.model.ProductLicenseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GenerateNewLicenseRepository
        extends JpaRepository<GenerateNewLicense, Long> {

    

    Optional<GenerateNewLicense> findByLicenseKey(String licenseKey);

    List<GenerateNewLicense> findByProductId(Long productId);
    List<GenerateNewLicense> findByAssignedUserId(Long userId);
    List<GenerateNewLicense> findByLicenseStatus(
            ProductLicenseStatus licenseStatus);

    List<GenerateNewLicense> findByDeletedFalse();

    List<GenerateNewLicense> findByExpiryDateBeforeAndDeletedFalse(
            LocalDate date);

    @Query("SELECT g FROM GenerateNewLicense g WHERE " +
           "g.deleted = false AND (" +
           "LOWER(g.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(g.licenseKey) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<GenerateNewLicense> searchLicenses(@Param("keyword") String keyword);

    List<GenerateNewLicense> findByGeneratedById(Long adminId);

   
    Page<GenerateNewLicense> findByDeletedFalse(Pageable pageable);

   
    @Query("SELECT g FROM GenerateNewLicense g WHERE " +
           "g.deleted = false AND " +
           "(:status IS NULL OR g.licenseStatus = :status) AND " +
           "(:productName IS NULL OR LOWER(g.product.name) " +
               "LIKE LOWER(CONCAT('%', :productName, '%'))) AND " +
           "(:licenseType IS NULL OR g.licenseType = :licenseType) AND " +
           "(:expiryDateFrom IS NULL OR g.expiryDate >= :expiryDateFrom) AND " +
           "(:expiryDateTo IS NULL OR g.expiryDate <= :expiryDateTo)")
    Page<GenerateNewLicense> findWithFilters(
            @Param("status") ProductLicenseStatus status,
            @Param("productName") String productName,
            @Param("licenseType") LicPlanType licenseType,
            @Param("expiryDateFrom") LocalDate expiryDateFrom,
            @Param("expiryDateTo") LocalDate expiryDateTo,
            Pageable pageable);
    
    
    @Query("""
            SELECT COUNT(l)
            FROM GenerateNewLicense l
            WHERE l.assignedUser.id = :userId
            AND l.licenseStatus = :status
        """)
        long countByAssignedUserAndStatus(@Param("userId") Long userId,
                                          @Param("status") ProductLicenseStatus status);
}

