package com.cws.shop.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cws.shop.model.TokenType;
import com.cws.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.shop.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
	Optional<Token> findByToken(String token);

    Optional<Token> findByTokenAndTokenType(String token, TokenType type);

    // delete all expired tokens
    void deleteByExpiryDateBefore(LocalDateTime now);

    List<Token> findByUserAndTokenType(User user, TokenType tokenType);
}
