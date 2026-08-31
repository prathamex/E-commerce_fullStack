package com.cws.shop.security;


import com.cws.shop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cws.shop.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

    	String path = request.getRequestURI();

    	// Skip JWT validation for public product APIs
    	if (
    	    path.startsWith("/admin/dashboard/products") ||
    	    path.startsWith("/auth/login") ||
    	    path.startsWith("/auth/register") ||
    	    path.startsWith("/auth/verify") ||
    	    path.startsWith("/admin/dashboard/products")
    	) {
    	    filterChain.doFilter(request, response);
    	    return;
    	}
        try {

            String token = getJwtFromRequest(request);

            logger.debug("JwtAuthenticationFilter: request URI={}, Authorization header present={}", path, request.getHeader("Authorization") != null);

            if (token == null) {
                logger.debug("No JWT token found in request for path {}", path);
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtService.isTokenValid(token)) {
                logger.debug("JWT token invalid for path {}", path);
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtService.extractEmail(token);
//            System.out.println(email);

            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (user.getActiveToken() == null ||
                    !user.getActiveToken().equals(token)) {

                filterChain.doFilter(request, response);
                return;
            }

            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,   
                            null,
                            Collections.singletonList(authority)
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) &&
                bearerToken.startsWith("Bearer ")) {

            return bearerToken.substring(7);
        }

        return null;
    }
}