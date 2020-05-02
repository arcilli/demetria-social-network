package com.arrnaux.friendshiprelationservice.util.user;

import com.arrnaux.demetria.core.interaction.UserUtils;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import org.springframework.lang.Nullable;
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

    @Nullable
    public static SNUser getObfuscatedUserByUserName(@Nullable String userName) {
        return UserUtils.getObfuscatedUserByUserName(restTemplate, userName);
    }

    @Nullable
    public static SNUser getObfuscatedUserById(@Nullable String userId) {
        return UserUtils.getObfuscatedUserById(restTemplate, userId);
    }
}
