package com.cws.shop.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cws.shop.dto.request.ResetPasswordRequestDto;
import com.cws.shop.model.PasswordResetToken;
import com.cws.shop.model.User;
import com.cws.shop.exception.InvalidTokenException;
import com.cws.shop.repository.PasswordResetTokenRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.ResetPasswordService;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordServiceImpl(
            UserRepository userRepository,
            PasswordResetTokenRepository tokenRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void resetPassword(String token,
                              ResetPasswordRequestDto requestDto) {

        if (!requestDto.getNewPassword()
                .equals(requestDto.getConfirmPassword())) {

            throw new IllegalArgumentException(
                    "New password and confirm password do not match");
        }

        PasswordResetToken resetToken = tokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new InvalidTokenException("Invalid reset token"));

        if (resetToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new InvalidTokenException("Reset token has expired");
        }

        User user = resetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(
                        requestDto.getNewPassword()));

        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }
}