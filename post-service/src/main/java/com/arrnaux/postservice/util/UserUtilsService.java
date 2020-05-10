package com.arrnaux.postservice.util;

import com.arrnaux.demetria.core.interaction.BasicUserUtils;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class UserUtilsService {
    private static RestTemplate restTemplate;

    private final RestTemplate autowiredComponent;

    public UserUtilsService(RestTemplate autowiredComponent) {
        this.autowiredComponent = autowiredComponent;
    }

    @PostConstruct
    private void init() {
        restTemplate = this.autowiredComponent;
    }

    public static SNUser getObfuscatedUserById(String userId) {
        return BasicUserUtils.getObfuscatedUserById(restTemplate, userId);
    }

    public static SNUser getObfuscatedUserByUserName(String userName) {
        return BasicUserUtils.getObfuscatedUserByUserName(restTemplate, userName);
    }
}
