package com.equipo4.chatbot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
        http
            .csrf (csrf -> csrf.disable())
            .authorizeRequests (authorize -> authorize
                .requestMatchers("/api/empleado", "/build/**", "/homepage.html").permitAll()
                .anyRequest().permitAll())
            .httpBasic(withDefaults())
            .formLogin(formLogin -> formLogin
                .defaultSuccessUrl("/build/index.html",true))
            .sessionManagement (session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
            .userDetailsService(customUserDetailsService);
            
        return http.build();
    }
}
