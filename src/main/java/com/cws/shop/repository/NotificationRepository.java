package com.cws.shop.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.shop.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	
	List<Notification>findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    Long countByUserIdAndIsReadFalse(Long userId);

    List<Notification>findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    List<Notification>findByUserIdOrderByCreatedAtDesc(Long userId);
    
    Page<Notification> findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(
            Long userId,
            LocalDateTime createdAt,
            Pageable pageable
    );

}
