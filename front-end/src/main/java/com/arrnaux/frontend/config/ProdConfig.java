package com.arrnaux.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Profile({"prod"})
@Configuration
public class ProdConfig {

    @Bean
    RestTemplate restTemplate() {
        // Used a HttpComponentsClientHttpRequestFactory since PATCH method is not supported by default.
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}