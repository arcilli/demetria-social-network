package com.arrnaux.friendshiprelationservice;

import com.arrnaux.friendshiprelationservice.config.ProdConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.arrnaux.demetria.core", "com.arrnaux.friendshiprelationservice"})
public class FriendshipRelationServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(FriendshipRelationServiceApplication.class, args);
    }

}
