package com.arrnaux.friendshiprelationservice;

import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class FriendshipRelationServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(FriendshipRelationServiceApplication.class, args);
    }

}
