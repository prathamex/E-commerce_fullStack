package com.cws.shop.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cws.shop.dto.response.AdminDashboardResponseDto;
import com.cws.shop.model.OrderStatus;
import com.cws.shop.repository.OrderRepository;
import com.cws.shop.repository.ProductRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.AdminDashboardService;

@Service
@Transactional(readOnly = true)
public class AdminDashboardServiveImpl implements AdminDashboardService{
	private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

	public AdminDashboardServiveImpl(OrderRepository orderRepository, UserRepository userRepository,
			ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}

	@Override
	public AdminDashboardResponseDto getDashboardData() {
		// TODO Auto-generated method stub
		Double totalRevenue = orderRepository.getTotalRevenue(OrderStatus.COMPLETED);
		
		 Long totalUsers = userRepository.count();

	     Long totalDownloads = productRepository.getTotalDownloads();
	                
	     return new AdminDashboardResponseDto(
	                totalRevenue,
	                totalUsers,
	                totalDownloads
	        );
	}
	
	
    
    
}
