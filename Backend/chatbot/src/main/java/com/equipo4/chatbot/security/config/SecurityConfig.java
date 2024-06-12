package com.equipo4.chatbot.security.config;

import lombok.RequiredArgsConstructor;

import javax.ws.rs.HttpMethod;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import com.equipo4.chatbot.security.jwt.JWTAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityfilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf
                        .disable())
                .authorizeHttpRequests(requests -> {
                    try {
                        requests
                        .antMatchers("/api/auth/**","/api/empleado/**","/api/auth/login", "/api/empleado")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                        .and()
                        .sessionManagement(management -> management
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authenticationProvider(authenticationProvider)
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        return http.build();
    }
}
