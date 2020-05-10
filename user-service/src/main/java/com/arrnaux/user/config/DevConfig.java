package com.arrnaux.user.config;

import com.mongodb.MongoClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"!prod"})
@EnableEurekaClient
public class DevConfig {

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient("localhost");
    }
}
