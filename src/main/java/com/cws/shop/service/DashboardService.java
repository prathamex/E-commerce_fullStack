package com.cws.shop.service;

import com.cws.shop.dto.response.DashboardSummaryResponse;

public interface DashboardService {
	
	DashboardSummaryResponse getDashboardSummary(Long userId);

}
