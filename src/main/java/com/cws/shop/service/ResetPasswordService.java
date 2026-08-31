package com.cws.shop.service;

import com.cws.shop.dto.request.ResetPasswordRequestDto;

public interface ResetPasswordService {

    void resetPassword(String token, ResetPasswordRequestDto requestDto);
}