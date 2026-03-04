package com.wms.flowerwms.global.config;

import com.wms.flowerwms.global.jwt.JwtAuthenticationFilter;
import com.wms.flowerwms.global.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 가능
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/members/register").permitAll()

                        // ADMIN 만
                        .requestMatchers(HttpMethod.POST, "/api/warehouses").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/warehouses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/warehouses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/members").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/members/*/approve").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/members/*/reject").hasRole("ADMIN")

                        // ADMIN, MANAGER 모두 가능
                        .requestMatchers(HttpMethod.GET, "/api/warehouses/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").authenticated()
                        .requestMatchers("/api/inbound/**").authenticated()
                        .requestMatchers("/api/outbound/**").authenticated()
                        .requestMatchers("/api/stocks/**").authenticated()
                        .requestMatchers("/api/dashboard/**").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}