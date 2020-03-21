package com.arrnaux.postservice.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Autowired
    Environment environment;

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public MongoClient mongoClient() {
        if (null != environment.getProperty("spring.data.mongodb.uri")) {
            return new MongoClient(new MongoClientURI(environment.getProperty("spring.data.mongodb.uri")));
        } else {
            return new MongoClient("localhost");
        }
    }

}
