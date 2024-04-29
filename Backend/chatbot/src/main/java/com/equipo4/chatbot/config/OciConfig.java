package com.equipo4.chatbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "oci.tenant")
@Configuration(proxyBeanMethods = false)
public class OciConfig {

    private String region;

    private Vault vault = new Vault();

    @Data
    @NoArgsConstructor
    public static class Vault {
        
        private String dbUsernameOcid;

        private String dbPasswordOcid;
    } 
}
