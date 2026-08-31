package com.cws.shop.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cws.shop.dto.response.NotificationResponse;
import com.cws.shop.dto.response.PagedResponse;
import com.cws.shop.exception.UnauthorizedException;
import com.cws.shop.model.Notification;
import com.cws.shop.model.NotificationType;
import com.cws.shop.model.User;
import com.cws.shop.repository.NotificationRepository;
import com.cws.shop.service.NotificationService;

import jakarta.transaction.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	private final NotificationRepository notificationRepository;
	
	public NotificationServiceImpl(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}
	
	

	@Override
	public NotificationResponse createNotification(User user, String title, String message, NotificationType type) {
		Notification notification = new Notification();

	    notification.setUser(user);
	    notification.setTitle(title);
	    notification.setMessage(message);
	    notification.setType(type);
	    
	    
	    Notification savedNotification =
	            notificationRepository.save(notification);

	    return mapToResponse(savedNotification);

	    
	}
	
	
	private NotificationResponse mapToResponse(Notification notification) {

	    NotificationResponse response = new NotificationResponse();

	    response.setId(notification.getId());
	    response.setTitle(notification.getTitle());
	    response.setMessage(notification.getMessage());
	    response.setCreatedAt(notification.getCreatedAt());
	    response.setRead(notification.isRead());

	    return response;
	}
	
	
	@Override
	public Long getUnreadCount(Long userId) {
		return notificationRepository.countByUserIdAndIsReadFalse(userId);
	}

	//Rahul's Code Update
	@Override
	@Transactional
	public NotificationResponse markAsRead(Long notificationId, Long userId) {

	    Notification notification = notificationRepository
	            .findById(notificationId)
	            .orElseThrow(() ->
	                    new RuntimeException(
	                            "Notification not found"));

	    if (!notification.getUser().getId().equals(userId)) {
	        throw new UnauthorizedException(
	                "You are not authorized to update this notification");
	    }
	    
	    notification.setRead(true);
	    
	   

	    Notification updatedNotification =
	            notificationRepository.save(notification);

	    return mapToResponse(updatedNotification);
	}

	@Override
	public List<NotificationResponse> getUnreadNotifications(Long userId) {

	    List<Notification> notifications =
	            notificationRepository
	            .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);

	    return notifications.stream()
	            .map(this::mapToResponse)
	            .collect(Collectors.toList());
	}

	@Override
	public PagedResponse<NotificationResponse> getAllNotifications(
	        Long userId,
	        int page,
	        int size
	) {

	    LocalDateTime twoMonthsAgo =
	            LocalDateTime.now().minusMonths(2);

	    Pageable pageable =
	            PageRequest.of(page, size);

	    Page<Notification> notificationPage =
	            notificationRepository
	                    .findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(
	                            userId,
	                            twoMonthsAgo,
	                            pageable
	                    );

	    List<NotificationResponse> content =
	            notificationPage.getContent()
	                    .stream()
	                    .map(this::mapToResponse)
	                    .toList();

	    return new PagedResponse<>(
	            content,
	            notificationPage.getNumber(),
	            notificationPage.getSize(),
	            notificationPage.getTotalElements(),
	            notificationPage.getTotalPages(),
	            notificationPage.isLast()
	    );
	}
	
	@Override
	@Transactional
	public void markAllAsRead(Long userId) {

	    List<Notification> unreadNotifications =
	            notificationRepository
	                    .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);

	    unreadNotifications.forEach(notification -> 
	            notification.setRead(true));

	    notificationRepository.saveAll(unreadNotifications);
	}
}
