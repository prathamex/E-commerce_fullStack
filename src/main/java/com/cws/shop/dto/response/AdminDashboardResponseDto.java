package com.cws.shop.dto.response;

public class AdminDashboardResponseDto {
	private Double totalRevenue;

    private Long totalUsers;

    private Long totalDownloads;
    
    public AdminDashboardResponseDto() {
    	
    }
    
	public AdminDashboardResponseDto(Double totalRevenue, Long totalUsers, Long totalDownloads) {
		this.totalRevenue = totalRevenue;
		this.totalUsers = totalUsers;
		this.totalDownloads = totalDownloads;
	}

	public Double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(Double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public Long getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(Long totalUsers) {
		this.totalUsers = totalUsers;
	}

	public Long getTotalDownloads() {
		return totalDownloads;
	}

	public void setTotalDownloads(Long totalDownloads) {
		this.totalDownloads = totalDownloads;
	}
    
    
}
