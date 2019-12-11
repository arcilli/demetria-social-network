package com.arrnaux.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.arrnaux.demetria.core", "com.arrnaux.frontend"})
public class FrontEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontEndApplication.class, args);
    }

    @Bean
    @LoadBalanced
    // TODO: use this restTemplate, instead of autowire a new one every time
    public RestTemplate getRestTemplate() {

        return new RestTemplate();
    }
}