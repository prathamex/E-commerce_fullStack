package com.cws.shop.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cws.shop.exception.InvalidTokenException;
import com.cws.shop.exception.TokenExpiredException;
import com.cws.shop.model.Token;
import com.cws.shop.model.TokenType;
import com.cws.shop.model.User;
import com.cws.shop.repository.TokenRepository;
import com.cws.shop.service.TokenService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class TokenServiceImpl implements TokenService{
    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token createToken(User user, TokenType type, long hoursValid) {
        String tokenstr = UUID.randomUUID().toString();
        Token token = new Token();
        token.setToken(tokenstr);
        token.setTokenType(type);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(hoursValid));

        return tokenRepository.save(token);
    }

    @Override
    public Token validateToken(String tokenStr, TokenType type) {
        Token token = tokenRepository.findByTokenAndTokenType(tokenStr, type)
                .orElseThrow(() -> new InvalidTokenException("Token not found"));

        if(token.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token);
            throw new TokenExpiredException("Token has expired");
        }
        return token;
    }

    @Override
    public void deleteExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());

    }

    @Override
    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }

    @Override
    public void deleteExistingTokens(User user, TokenType type) {
        List<Token> tokens = tokenRepository.findByUserAndTokenType(user, type);
        if (!tokens.isEmpty()) {
            tokenRepository.deleteAll(tokens);
        }
    }

}
