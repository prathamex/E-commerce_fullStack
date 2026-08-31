package com.cws.shop.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import com.cws.shop.security.JwtAuthenticationFilter; // Added
import com.cws.shop.security.RestAccessDeniedHandler;
import com.cws.shop.security.RestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Added
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter; // Added
        private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
        private final RestAccessDeniedHandler restAccessDeniedHandler;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                              RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                              RestAccessDeniedHandler restAccessDeniedHandler) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
                this.restAccessDeniedHandler = restAccessDeniedHandler;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // CORS Configuration
        @Value("${app.cors.allowed-origins}")
        private List<String> allowedOrigins;

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(allowedOrigins); // from application.yml
                configuration.setAllowedMethods(
                                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                configuration.setAllowedHeaders(
                                List.of("*"));

                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**", configuration);

                return source;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(Customizer.withDefaults())
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(sm -> sm
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .exceptionHandling(e -> e
                                                .authenticationEntryPoint(restAuthenticationEntryPoint)
                                                .accessDeniedHandler(restAccessDeniedHandler)
                                )

                                .authorizeHttpRequests(auth -> auth

                                                // Allow all OPTIONS preflight requests
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                                .requestMatchers("/public/**").permitAll() // Added

                                                .requestMatchers(
                                                                "/auth/login",
                                                                "/auth/register",
                                                                "/auth/verify",
                                                                "/auth/forgot-password")
                                                .permitAll()

                                                .requestMatchers("/auth/**").permitAll()
                                                .requestMatchers("/", "/login").permitAll()

                                                .requestMatchers("/auth/me").authenticated()
                                                .requestMatchers("/admin/upload/profile-image").authenticated()


                                                // ONLY PRODUCT SEARCH
                                                .requestMatchers("/admin/dashboard/products/**").permitAll()
                                                .requestMatchers("/user/**").authenticated() 

                                                // Rahul's code- Changed Notification Public To authenticated
                                                .requestMatchers("/notifications/**").authenticated()
                                                .requestMatchers("/api/licenses/**").permitAll()

                                                // kajal Added
                                                .requestMatchers("/api/users/search").permitAll()
                                                .requestMatchers("/auth/register").permitAll()

                                                // Prathamesh added
                                                .requestMatchers("/api/users/assign-search").permitAll()
                                                .requestMatchers("/api/users/filter").hasRole("ADMIN")

                                                // rushikesh added
                                                .requestMatchers("/api/cart/**").permitAll()
                                                
                                             //Harshada added Contact Form
                                                .requestMatchers("/api/contact").permitAll()
                                                
                                                
                                                // Ganesh added                                            
                                                .requestMatchers("/api/payment/**").permitAll()

                                                // Pratik added
                                                // .requestMatchers("/users/**").permitAll()
                                                .requestMatchers("/users", "/users/**").permitAll()
                                                .requestMatchers("/api/users/*/block").hasRole("ADMIN")
                                                .requestMatchers("/api/users/*/suspend").hasRole("ADMIN")
                                                .requestMatchers("/api/users/*/activate").hasRole("ADMIN")
                                                
                                                .requestMatchers("/api/users/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/users").permitAll()

                                                .requestMatchers("/admin/**").hasRole("ADMIN") // Added

                                                // Rushikesh added
                                                .requestMatchers("/admin/upload/**").permitAll()

                                                // ORDER APIs
                                                .requestMatchers("/api/admin/orders/**").permitAll()
                                                .requestMatchers("/user/**").authenticated()

                                                // Rahul's code Update
                                                // Public: anyone can view ratings
                                                .requestMatchers(HttpMethod.GET, "/api/ratings/product/**").permitAll()

                                                // Rahul's code Update
                                                // Authenticated Users only: To submit a rating (BUYER must be logged
                                                // in)
                                                .requestMatchers(HttpMethod.POST, "/api/ratings").authenticated()

                                                .anyRequest().authenticated()

                                )

                                .addFilterBefore(jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

}
