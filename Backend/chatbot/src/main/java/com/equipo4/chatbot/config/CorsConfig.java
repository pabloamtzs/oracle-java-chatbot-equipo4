package com.equipo4.chatbot.config;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    Logger logger = LoggerFactory.getLogger(CorsConfig.class);
    public CorsFilter corsFilter(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000","https://objectstorage.mx-queretaro-1.oraclecloud.com",
                "https://petstore.swagger.io"));
        config.setAllowedMethods(List.of("GET","POST","PUT","OPTIONS","DELETE","PATCH"));
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.addAllowedHeader("*");
        config.addExposedHeader("location");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        CorsFilter filter = new CorsFilter(source);
        return filter;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(corsFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

}
