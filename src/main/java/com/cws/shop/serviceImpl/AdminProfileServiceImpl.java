package com.cws.shop.serviceImpl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cws.shop.dto.response.AdminProfileResponseDto;
import com.cws.shop.exception.UserNotFoundException;
import com.cws.shop.model.User;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.AdminProfileService;

//20-5-26


@Service
public class AdminProfileServiceImpl implements AdminProfileService {

    private final UserRepository userRepository;
    
    public AdminProfileServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AdminProfileResponseDto getAdminProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Admin not found"));

        AdminProfileResponseDto dto =
                new AdminProfileResponseDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setRole(user.getRole().name());

        return dto;
    }
}