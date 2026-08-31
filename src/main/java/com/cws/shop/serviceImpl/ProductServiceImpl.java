package com.cws.shop.serviceImpl;
 
import com.cws.shop.dto.request.ProductDetailsRequest;

import com.cws.shop.dto.request.ProductRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.PagedResponse;
import com.cws.shop.dto.response.ProductAdminResponse;
import com.cws.shop.dto.response.ProductListItemResponse;
import com.cws.shop.dto.response.ProductPublicCardResponse;
import com.cws.shop.dto.response.ProductPublicDetailResponse;
import com.cws.shop.exception.ProductNotFoundException;
import com.cws.shop.model.Product;
import com.cws.shop.model.ProductDetails;
import com.cws.shop.model.ProductStatus;
import com.cws.shop.repository.ProductDetailsRepository;
import com.cws.shop.repository.ProductRepository;
import com.cws.shop.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cws.shop.service.NotificationService;
import com.cws.shop.model.NotificationType;
import com.cws.shop.model.User;
import com.cws.shop.repository.UserRepository;

 
import java.util.List;
import java.util.Optional;
 
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
 
    private final ProductRepository productRepository;
    private final ProductDetailsRepository productDetailsRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

 
    public ProductServiceImpl(ProductRepository productRepository,
                              ProductDetailsRepository productDetailsRepository , NotificationService notificationService,
                              UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productDetailsRepository = productDetailsRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }
 
    @Override
    public ApiResponse<ProductAdminResponse> createProduct(ProductRequest request) {
        Product product = new Product();
        mapRequestToProduct(request, product);
 
        String baseSlug = (request.getSlug() != null && !request.getSlug().isBlank())
                ? slugify(request.getSlug())
                : generateSlug(request.getName());
 
        product.setSlug(ensureUniqueSlug(baseSlug, null));
        applyDiscount(product, request);
 
        Product saved = productRepository.save(product);
        
     
        List<User> users = userRepository.findAll();

        for (User user : users) {
            notificationService.createNotification(
                    user,
                    "New Product Added",
                    "New product available: " + saved.getName(),
                    NotificationType.PRODUCT
            );
        }

        return new ApiResponse<>(true, "Product created successfully", toAdminResponse(saved));
    }
 
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PagedResponse<ProductListItemResponse>> getAllProducts(
            String search, ProductStatus status, Pageable pageable) {
 
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
 
        Page<Product> page = productRepository.findByFilters(searchParam, status, pageable);
 
        List<ProductListItemResponse> items = page.getContent()
                .stream()
                .map(this::toListItemResponse)
                .toList();
 
        PagedResponse<ProductListItemResponse> paged = new PagedResponse<>(
                items, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast()
        );
 
        return new ApiResponse<>(true, "Products fetched successfully", paged);
    }
 
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<ProductAdminResponse> getProductById(Long id) {
        Product product = findProductOrThrow(id);
        return new ApiResponse<>(true, "Product fetched successfully", toAdminResponse(product));
    }
 
    @Override
    public ApiResponse<ProductAdminResponse> updateProduct(Long id, ProductRequest request) {
        Product product = findProductOrThrow(id);
        mapRequestToProduct(request, product);
 
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            String newSlug = slugify(request.getSlug());
            if (!newSlug.equals(product.getSlug())) {
                product.setSlug(ensureUniqueSlug(newSlug, id));
            }
        }
 
        applyDiscount(product, request);
 
        Product saved = productRepository.save(product);
        return new ApiResponse<>(true, "Product updated successfully", toAdminResponse(saved));
    }
 
    @Override
    public ApiResponse<String> updateProductStatus(Long id, ProductStatus status) {
        Product product = findProductOrThrow(id);
        product.setStatus(status);
        productRepository.save(product);
        return new ApiResponse<>(true, "Product status updated to " + status, null);
    }
 
    
    @Override
    public ApiResponse<String> deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
 
        product.setDeleted(true);
        product.setStatus(ProductStatus.ARCHIVED);
 
        productRepository.save(product);
        return new ApiResponse<>(true, "Product deleted successfully", null);
    }
 
    @Override
    public ApiResponse<ProductAdminResponse> saveProductDetails(Long id, ProductDetailsRequest request) {
        Product product = findProductOrThrow(id);
 
        // fetch existing details or create new — handles both insert and update
        ProductDetails details = productDetailsRepository.findByProductId(id)
                .orElse(new ProductDetails());
 
        details.setProduct(product);
        details.setOverview(request.getOverview());
        details.setFeatures(request.getFeatures());
        details.setUseCases(request.getUseCases());
        details.setScreenshots(request.getScreenshots());
        details.setTechnicalRequirements(request.getTechnicalRequirements());
        details.setFaqs(request.getFaqs());
        details.setDownloadFileUrl(request.getDownloadFileUrl());
        details.setDocumentationUrl(request.getDocumentationUrl());
        details.setReleaseNotes(request.getReleaseNotes());
        details.setVersionHistory(request.getVersionHistory());
        details.setDemoUrl(request.getDemoUrl());
        details.setDemoVideos(request.getDemoVideos());
 
        productDetailsRepository.save(details);
 
        Product refreshed = findProductOrThrow(id);
        return new ApiResponse<>(true, "Product details saved successfully", toAdminResponse(refreshed));
    }
 
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PagedResponse<ProductPublicCardResponse>> getPublishedProducts(
            String search, Pageable pageable) {
 
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
 
        Page<Product> page = productRepository.findPublishedBySearch(searchParam, pageable);
 
        List<ProductPublicCardResponse> cards = page.getContent()
                .stream()
                .map(this::toPublicCardResponse)
                .toList();
 
        PagedResponse<ProductPublicCardResponse> paged = new PagedResponse<>(
                cards, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast()
        );
 
        return new ApiResponse<>(true, "Products fetched successfully", paged);
    }
 
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<ProductPublicDetailResponse> getProductBySlug(String slug) {
        // Use findBySlugAndDeletedFalse — deleted products are invisible to buyers
        Product product = productRepository.findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with slug: " + slug));
 
        // return 404 for non-published products — don't reveal that the product exists
        if (product.getStatus() != ProductStatus.PUBLISHED) {
            throw new ProductNotFoundException("Product is not available");
        }
 
        return new ApiResponse<>(true, "Product fetched successfully", toPublicDetailResponse(product));
    }
 
    
 
    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }
 
    private void mapRequestToProduct(ProductRequest request, Product product) {
        product.setName(request.getName());
        product.setShortDescription(request.getShortDescription());
        product.setThumbnailImage(request.getThumbnailImage());
        product.setPrice(request.getPrice());
        product.setDiscountedPrice(request.getDiscountedPrice());
        product.setDiscountPercent(request.getDiscountPercent());
        product.setCurrentVersion(request.getCurrentVersion());
        product.setCategory(request.getCategory());
        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }
        
        
    }
 
    private void applyDiscount(Product product, ProductRequest request) {
        if (request.getDiscountPercent() != null && request.getDiscountedPrice() == null) {
            double discounted = request.getPrice() * (1.0 - request.getDiscountPercent() / 100.0);
            product.setDiscountedPrice(Math.round(discounted * 100.0) / 100.0);
        }
    }
 
    private String generateSlug(String name) {
        return name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
 
    private String slugify(String slug) {
        return slug.toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
 
    private String ensureUniqueSlug(String baseSlug, Long excludeId) {
        String slug = baseSlug;
        int counter = 2;
        while (true) {
            // Use existsBySlugAndDeletedFalse — deleted products' slugs
            //    are freed up so they can be reused by new products.
//            boolean slugTaken = productRepository.existsBySlugAndDeletedFalse(slug);
        	  boolean slugTaken = productRepository.existsBySlug(slug);
            if (!slugTaken) return slug;
 
            if (excludeId != null) {
                // If the slug belongs to the product being updated, it's still valid
//                Optional<Product> existing = productRepository.findBySlugAndDeletedFalse(slug);
            	Optional<Product> existing =
            	        productRepository.findBySlug(slug);
                if (existing.isPresent() && existing.get().getId().equals(excludeId)) return slug;
            }
 
            slug = baseSlug + "-" + counter;
            counter++;
        }
    }
 
    private ProductListItemResponse toListItemResponse(Product p) {
        return new ProductListItemResponse(
                p.getId(), p.getName(), p.getThumbnailImage(), p.getSlug(),
                p.getSalesCount(), p.getRevenue(), p.getAverageRating(),
                p.getTotalReviews(), p.getStatus(), p.getUpdatedAt()
        );
    }
 
    private ProductAdminResponse toAdminResponse(Product p) {
        ProductAdminResponse res = new ProductAdminResponse();
        res.setId(p.getId());
        res.setName(p.getName());
        res.setSlug(p.getSlug());
        res.setShortDescription(p.getShortDescription());
        res.setThumbnailImage(p.getThumbnailImage());
        res.setPrice(p.getPrice());
        res.setDiscountedPrice(p.getDiscountedPrice());
        res.setDiscountPercent(p.getDiscountPercent());
        res.setCurrentVersion(p.getCurrentVersion());
        res.setStatus(p.getStatus());
        res.setSalesCount(p.getSalesCount());
        res.setDownloadCount(p.getDownloadCount());
        res.setRevenue(p.getRevenue());
        res.setAverageRating(p.getAverageRating());
        res.setTotalReviews(p.getTotalReviews());
        res.setCreatedAt(p.getCreatedAt());
        res.setUpdatedAt(p.getUpdatedAt());
        res.setCategory(p.getCategory());
 
        if (p.getDetails() != null) {
            ProductDetails d = p.getDetails();
            res.setOverview(d.getOverview());
            res.setFeatures(d.getFeatures());
            res.setUseCases(d.getUseCases());
            res.setScreenshots(d.getScreenshots());
            res.setTechnicalRequirements(d.getTechnicalRequirements());
            res.setFaqs(d.getFaqs());
            res.setDownloadFileUrl(d.getDownloadFileUrl());
            res.setDocumentationUrl(d.getDocumentationUrl());
            res.setReleaseNotes(d.getReleaseNotes());
            res.setVersionHistory(d.getVersionHistory());
            res.setDemoUrl(d.getDemoUrl());
            res.setDemoVideos(d.getDemoVideos());
        }
 
        return res;
    }
 
    private ProductPublicCardResponse toPublicCardResponse(Product p) {
        ProductPublicCardResponse card = new ProductPublicCardResponse();
        card.setId(p.getId());
        card.setName(p.getName());
        card.setSlug(p.getSlug());
        card.setShortDescription(p.getShortDescription());
        card.setThumbnailImage(p.getThumbnailImage());
        card.setPrice(p.getPrice());
        card.setDiscountedPrice(p.getDiscountedPrice());
        card.setDiscountPercent(p.getDiscountPercent());
        card.setCurrentVersion(p.getCurrentVersion());
        card.setAverageRating(p.getAverageRating());
        card.setTotalReviews(p.getTotalReviews());
        card.setCategory(p.getCategory());
        
        if (p.getDetails() != null) {
            card.setFeatures(p.getDetails().getFeatures());
            card.setTechnicalRequirements(
                    p.getDetails().getTechnicalRequirements()
            );
        }
        
        
        

        return card;
    }
 
    private ProductPublicDetailResponse toPublicDetailResponse(Product p) {
        ProductPublicDetailResponse res = new ProductPublicDetailResponse();
        res.setId(p.getId());
        res.setName(p.getName());
        res.setSlug(p.getSlug());
        res.setShortDescription(p.getShortDescription());
        res.setThumbnailImage(p.getThumbnailImage());
        res.setPrice(p.getPrice());
        res.setDiscountedPrice(p.getDiscountedPrice());
        res.setDiscountPercent(p.getDiscountPercent());
        res.setCurrentVersion(p.getCurrentVersion());
        res.setAverageRating(p.getAverageRating());
        res.setTotalReviews(p.getTotalReviews());
 
        if (p.getDetails() != null) {
            ProductDetails d = p.getDetails();
            res.setOverview(d.getOverview());
            res.setFeatures(d.getFeatures());
            res.setUseCases(d.getUseCases());
            res.setScreenshots(d.getScreenshots());
            res.setTechnicalRequirements(d.getTechnicalRequirements());
            res.setFaqs(d.getFaqs());
            res.setDocumentationUrl(d.getDocumentationUrl());
            res.setReleaseNotes(d.getReleaseNotes());
            res.setVersionHistory(d.getVersionHistory());
            // downloadFileUrl excluded — only returned after purchase + license check
        }
 
        return res;
    }
    
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<ProductPublicCardResponse>> getTopProducts() {

        List<ProductPublicCardResponse> products = productRepository
                .findTop3ByStatusOrderBySalesCountDesc(ProductStatus.PUBLISHED)
                .stream()
                .map(this::toPublicCardResponse)
                .toList();

        return new ApiResponse<>(
                true,
                "Top products fetched successfully",
                products
        );
    }
}