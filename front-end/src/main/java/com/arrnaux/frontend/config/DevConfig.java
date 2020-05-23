package com.arrnaux.frontend.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile({"!prod"})
@EnableDiscoveryClient
public class DevConfig {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        // Used a HttpComponentsClientHttpRequestFactory since PATCH method is not supported by default.
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}