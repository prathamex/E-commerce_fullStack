package com.cws.shop.serviceImpl;

import com.cws.shop.dto.request.GenerateNewLicenseRequest;
import com.cws.shop.dto.request.LicenseFilterRequest;
import com.cws.shop.dto.request.RenewLicenseRequest;
import com.cws.shop.dto.response.GenerateNewLicenseResponse;
import com.cws.shop.dto.response.LicenseDashboardResponse;
import com.cws.shop.dto.response.LicenseDetailResponse;
import com.cws.shop.dto.response.PagedLicenseResponse;
import com.cws.shop.exception.BadRequestException;
import com.cws.shop.exception.UserNotFoundException;
import com.cws.shop.model.GenerateNewLicense;
import com.cws.shop.model.Product;
import com.cws.shop.model.ProductLicenseStatus;
import com.cws.shop.model.User;
import com.cws.shop.repository.GenerateNewLicenseRepository;
import com.cws.shop.repository.ProductRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.EmailService;
import com.cws.shop.service.GenerateNewLicenseService;
import com.cws.shop.service.NotificationService;
import com.cws.shop.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GenerateNewLicenseServiceImpl
        implements GenerateNewLicenseService {

    private final GenerateNewLicenseRepository licenseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    //Rahul's Code
    private final NotificationService notificationService;

    private static final Logger log = LoggerFactory.getLogger(GenerateNewLicenseServiceImpl.class);

    //Rahul's Code Added notification in constructor
    public GenerateNewLicenseServiceImpl(
            GenerateNewLicenseRepository licenseRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            EmailService emailService,
            NotificationService notificationService) {
        this.licenseRepository = licenseRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public GenerateNewLicenseResponse generateLicense(
            GenerateNewLicenseRequest request) {

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String licenseKey;
        if (Boolean.TRUE.equals(request.getManualKey())) {
            licenseRepository.findByLicenseKey(request.getLicenseKey())
                    .ifPresent(existing -> {
                        throw new BadRequestException("License key already exists");
                    });
            licenseKey = request.getLicenseKey();
        } else {
            licenseKey = generateLicenseKey();
        }

        GenerateNewLicense license = new GenerateNewLicense();
        license.setProduct(product);
        license.setLicenseType(request.getLicenseType());
        license.setLicensePlan(request.getLicensePlan());

        // assigned user (optional)
        User assignedUser = null;
        if (request.getAssignedUserId() != null) {
            assignedUser = userRepository
                    .findById(request.getAssignedUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            license.setAssignedUser(assignedUser);
        }

        license.setCustomerName(request.getCustomerName());
        license.setCustomerEmail(request.getCustomerEmail());
        license.setMobileNumber(request.getMobileNumber());
        license.setCompanyName(request.getCompanyName());
        license.setLicenseKey(licenseKey);
        license.setManualKey(request.getManualKey());
        license.setActivationLimit(request.getActivationLimit());
        license.setActivationDate(request.getActivationDate());
        license.setExpiryDate(request.getExpiryDate());

        //auto sets status
        license.setLicenseStatus(
                calculateStatus(request.getActivationDate(), request.getExpiryDate()));

        GenerateNewLicense savedLicense = licenseRepository.save(license);

        try {
            emailService.sendLicenseEmail(
                    request.getCustomerEmail(),
                    request.getCustomerName(),
                    product.getName(),
                    licenseKey,
                    request.getLicenseType().name(),
                    request.getLicensePlan() != null
                            ? request.getLicensePlan().name() : "N/A",
                    request.getActivationDate(),
                    request.getExpiryDate(),
                    request.getActivationLimit()
            );
        } catch (Exception e) {
            throw new BadRequestException(
                    "License could not be generated because email failed to send to "
                            + request.getCustomerEmail()
                            + ". Reason: " + e.getMessage());
        }

        //Rahul's Code - send notification to assigned user
        if (assignedUser != null) {
            try {
                String title = "License Generated for " + product.getName();

                String message = buildLicenseGeneratedMessage(
                        product.getName(),
                        licenseKey,
                        request
                );

                notificationService.createNotification(
                        assignedUser,
                        title,
                        message,
                        NotificationType.LICENSE
                );

            } catch (Exception e) {
                log.error("[GenerateNewLicenseServiceImpl] Failed to send LICENSE_GENERATED notification to userId={}: {}",
                        assignedUser.getId(), e.getMessage(), e);
            }
        }

        return mapToResponse(savedLicense);
    }

    //pagination(all licenses)
    @Override
    public PagedLicenseResponse getAllLicenses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());

        Page<GenerateNewLicense> licensePage =
                licenseRepository.findByDeletedFalse(pageable);

        return buildPagedResponse(licensePage);
    }

    @Override
    public List<LicenseDashboardResponse> searchLicenses(String keyword) {
        return licenseRepository.searchLicenses(keyword)
                .stream()
                .map(this::mapToDashboardResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PagedLicenseResponse filterLicenses(
            LicenseFilterRequest filter, int page, int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());

        Page<GenerateNewLicense> licensePage = licenseRepository.findWithFilters(
                filter.getActivationStatus(),
                filter.getProductName(),
                filter.getLicenseType(),
                filter.getExpiryDateFrom(),
                filter.getExpiryDateTo(),
                pageable
        );

        return buildPagedResponse(licensePage);
    }

    @Override
    public LicenseDetailResponse viewLicense(Long id) {
        GenerateNewLicense license = getLiveOrThrow(id);
        return mapToDetailResponse(license);
    }

    @Override
    @Transactional
    public void deleteLicense(Long id) {
        GenerateNewLicense license = licenseRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("License not found"));

        if (Boolean.TRUE.equals(license.getDeleted())) {
            throw new BadRequestException("License already deleted");
        }

        license.setDeleted(true);
        licenseRepository.save(license);
    }

    @Override
    @Transactional
    public LicenseDashboardResponse activateLicense(Long id) {
        GenerateNewLicense license = getLiveOrThrow(id);

        if (license.getLicenseStatus() == ProductLicenseStatus.ACTIVE) {
            throw new BadRequestException("License is already active");
        }

        license.setLicenseStatus(ProductLicenseStatus.ACTIVE);
        licenseRepository.save(license);
        return mapToDashboardResponse(license);
    }

    @Override
    @Transactional
    public LicenseDashboardResponse suspendLicense(Long id) {
        GenerateNewLicense license = getLiveOrThrow(id);

        if (license.getLicenseStatus() == ProductLicenseStatus.SUSPENDED) {
            throw new BadRequestException("License is already suspended");
        }

        if (license.getLicenseStatus() != ProductLicenseStatus.ACTIVE &&
            license.getLicenseStatus() != ProductLicenseStatus.INACTIVE) {
            throw new BadRequestException(
                    "Only ACTIVE or INACTIVE licenses can be suspended");
        }

        license.setLicenseStatus(ProductLicenseStatus.SUSPENDED);
        licenseRepository.save(license);
        return mapToDashboardResponse(license);
    }

    @Override
    @Transactional
    public LicenseDashboardResponse renewLicense(Long id,
            RenewLicenseRequest request) {

        GenerateNewLicense license = licenseRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("License not found"));

        ProductLicenseStatus newStatus = calculateStatus(
                license.getActivationDate(), request.getNewExpiryDate());

        license.setExpiryDate(request.getNewExpiryDate());
        license.setLicenseStatus(newStatus);
        license.setDeleted(false);
        licenseRepository.save(license);

        return mapToDashboardResponse(license);
    }

    //calculate dates for updating status
    private ProductLicenseStatus calculateStatus(
            LocalDate activationDate, LocalDate expiryDate) {
        LocalDate today = LocalDate.now();

        if (today.isBefore(activationDate)) {
            return ProductLicenseStatus.INACTIVE;
        } else if (!today.isAfter(expiryDate)) {
            return ProductLicenseStatus.ACTIVE;
        } else {
            return ProductLicenseStatus.EXPIRED;
        }
    }

    private GenerateNewLicense getLiveOrThrow(Long id) {
        GenerateNewLicense license = licenseRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("License not found"));

        if (Boolean.TRUE.equals(license.getDeleted())) {
            throw new RuntimeException("License not found");
        }
        return license;
    }

    private PagedLicenseResponse buildPagedResponse(
            Page<GenerateNewLicense> licensePage) {

        List<LicenseDashboardResponse> content = licensePage.getContent()
                .stream()
                .map(this::mapToDashboardResponse)
                .collect(Collectors.toList());

        return new PagedLicenseResponse(
                content,
                licensePage.getNumber(),
                licensePage.getTotalPages(),
                licensePage.getTotalElements()
        );
    }

    private LicenseDashboardResponse mapToDashboardResponse(
            GenerateNewLicense license) {

        String userAssigned = (license.getAssignedUser() != null)
                ? license.getAssignedUser().getName()
                : license.getCustomerName();

        return new LicenseDashboardResponse(
                license.getId(),
                license.getLicenseKey(),
                license.getProduct().getName(),
                userAssigned,
                license.getLicenseStatus(),
                license.getExpiryDate()
        );
    }

    //Rahul's Code
    private String buildLicenseGeneratedMessage(
            String productName,
            String licenseKey,
            GenerateNewLicenseRequest request) {

        String plan    = request.getLicensePlan() != null
                         ? request.getLicensePlan().name() : "N/A";
        String type    = request.getLicenseType() != null
                         ? request.getLicenseType().name() : "N/A";
        String expiry  = request.getExpiryDate() != null
                         ? request.getExpiryDate().toString() : "N/A";
        Integer devices = request.getActivationLimit() != null
                         ? request.getActivationLimit() : 1;

        return "Your license (" + licenseKey + ") for " + productName
             + " has been generated."
             + " Type: " + type
             + " | Plan: " + plan
             + " | Devices allowed: " + devices
             + " | Expires: " + expiry
             + ". Check your email for full license details.";
    }

    private LicenseDetailResponse mapToDetailResponse(
            GenerateNewLicense license) {

        LicenseDetailResponse response = new LicenseDetailResponse();
        response.setId(license.getId());
        response.setLicenseKey(license.getLicenseKey());
        response.setProductName(license.getProduct().getName());
        response.setLicenseType(license.getLicenseType());
        response.setLicensePlan(license.getLicensePlan());

        if (license.getAssignedUser() != null) {
            response.setAssignedUserId(license.getAssignedUser().getId());
            response.setAssignedUserName(license.getAssignedUser().getName());
        }

        response.setCustomerName(license.getCustomerName());
        response.setCustomerEmail(license.getCustomerEmail());
        response.setMobileNumber(license.getMobileNumber());
        response.setCompanyName(license.getCompanyName());
        response.setManualKey(license.getManualKey());
        response.setActivationLimit(license.getActivationLimit());
        response.setLicenseStatus(license.getLicenseStatus());
        response.setActivationDate(license.getActivationDate());
        response.setExpiryDate(license.getExpiryDate());
        response.setCreatedAt(license.getCreatedAt());
        response.setUpdatedAt(license.getUpdatedAt());

        return response;
    }

    private GenerateNewLicenseResponse mapToResponse(
            GenerateNewLicense license) {
        return new GenerateNewLicenseResponse(
                license.getProduct().getName(),
                license.getLicenseKey(),
                license.getCustomerName(),
                license.getLicenseStatus(),
                license.getExpiryDate()
        );
    }

    private String generateLicenseKey() {
        String year = String.valueOf(LocalDate.now().getYear());
        String part1 = UUID.randomUUID().toString()
                .substring(0, 5).toUpperCase();
        String part2 = UUID.randomUUID().toString()
                .substring(0, 5).toUpperCase();
        return "LIC-" + year + "-" + part1 + "-" + part2;
    }
}