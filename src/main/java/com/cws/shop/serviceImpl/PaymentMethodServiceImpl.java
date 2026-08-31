package com.cws.shop.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cws.shop.dto.request.PaymentMethodRequestDto;
import com.cws.shop.dto.response.PaymentMethodResponseDto;
import com.cws.shop.exception.ResourceNotFoundException;
import com.cws.shop.model.PaymentMethod;
import com.cws.shop.repository.PaymentMethodRepository;
import com.cws.shop.service.PaymentMethodService;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public PaymentMethodResponseDto createPaymentMethod(PaymentMethodRequestDto requestDto) {

        if (paymentMethodRepository.existsByMethodName(requestDto.getMethodName())) {
            throw new RuntimeException("Payment method already exists");
        }

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethodName(requestDto.getMethodName());
        paymentMethod.setActive(requestDto.getActive());

        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);

        return mapToDto(savedPaymentMethod);
    }

    @Override
    public List<PaymentMethodResponseDto> getAllPaymentMethods() {

        return paymentMethodRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentMethodResponseDto getPaymentMethodById(Long id) {

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));

        return mapToDto(paymentMethod);
    }

    @Override
    public PaymentMethodResponseDto updatePaymentMethod(Long id,
            PaymentMethodRequestDto requestDto) {

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));

        paymentMethod.setMethodName(requestDto.getMethodName());
        paymentMethod.setActive(requestDto.getActive());

        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(paymentMethod);

        return mapToDto(updatedPaymentMethod);
    }

    @Override
    public void deletePaymentMethod(Long id) {

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));

        paymentMethodRepository.delete(paymentMethod);
    }

    // DTO Mapper

    private PaymentMethodResponseDto mapToDto(PaymentMethod paymentMethod) {

        PaymentMethodResponseDto dto = new PaymentMethodResponseDto();

        dto.setId(paymentMethod.getId());
        dto.setMethodName(paymentMethod.getMethodName());
        dto.setActive(paymentMethod.getActive());
        dto.setCreatedAt(paymentMethod.getCreatedAt());

        return dto;
    }
}