package com.arrnaux.user.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
@Profile("prod")
public class ProdConfig {

    private final Environment environment;

    public ProdConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public MongoClient mongoClient() {
        if (null != environment.getProperty("spring.data.mongodb.uri")) {
            return new MongoClient(new MongoClientURI(Objects.requireNonNull(environment.getProperty("spring.data.mongodb.uri"))));
        }
        return null;
    }
}
