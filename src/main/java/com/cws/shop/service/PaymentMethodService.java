package com.cws.shop.service;

import java.util.List;

import com.cws.shop.dto.request.PaymentMethodRequestDto;
import com.cws.shop.dto.response.PaymentMethodResponseDto;

public interface PaymentMethodService {

    PaymentMethodResponseDto createPaymentMethod(PaymentMethodRequestDto requestDto);

    List<PaymentMethodResponseDto> getAllPaymentMethods();

    PaymentMethodResponseDto getPaymentMethodById(Long id);

    PaymentMethodResponseDto updatePaymentMethod(Long id, PaymentMethodRequestDto requestDto);

    void deletePaymentMethod(Long id);
}