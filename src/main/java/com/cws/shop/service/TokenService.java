package com.cws.shop.service;

import com.cws.shop.model.Token;
import com.cws.shop.model.TokenType;
import com.cws.shop.model.User;

public interface TokenService
{
    Token createToken(User user, TokenType type, long hoursValid);

    Token validateToken(String tokenStr, TokenType type);

    void deleteExpiredTokens();

    void deleteToken(Token token);

    void deleteExistingTokens(User user, TokenType type);
}
