package com.arrnaux.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Profile({"prod"})
@Configuration
public class ProdConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}