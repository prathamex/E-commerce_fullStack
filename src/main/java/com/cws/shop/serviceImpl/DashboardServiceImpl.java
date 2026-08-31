package com.cws.shop.serviceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cws.shop.dto.response.DashboardSummaryResponse;
import com.cws.shop.model.Order;
import com.cws.shop.model.ProductLicenseStatus;
import com.cws.shop.repository.GenerateNewLicenseRepository;
import com.cws.shop.repository.OrderRepository;
import com.cws.shop.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final OrderRepository orderRepository;
    private final GenerateNewLicenseRepository licenseRepository;

    public DashboardServiceImpl(OrderRepository orderRepository,
                                GenerateNewLicenseRepository licenseRepository) {
        this.orderRepository = orderRepository;
        this.licenseRepository = licenseRepository;
    }

    @Override
    public DashboardSummaryResponse getDashboardSummary(Long userId) {

        List<Order> orders = orderRepository.findByUserId(userId);

        Set<String> products = new HashSet<>();

        for (Order order : orders) {

            if (order.getProductPurchased() != null) {

                Arrays.stream(order.getProductPurchased().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .forEach(products::add);
            }
        }

        DashboardSummaryResponse response = new DashboardSummaryResponse();

        response.setPurchasedProducts(products.size());
        response.setTotalOrders(orders.size());

        response.setActiveLicenses(
                licenseRepository.countByAssignedUserAndStatus(
                        userId,
                        ProductLicenseStatus.ACTIVE
                )
        );

        // Download history module not implemented yet
        response.setRecentDownloads(0);

        return response;
    }
}