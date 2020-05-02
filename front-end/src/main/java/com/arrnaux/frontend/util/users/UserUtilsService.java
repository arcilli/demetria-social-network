package com.arrnaux.frontend.util.users;

import com.arrnaux.demetria.core.interaction.UserUtils;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userAccount.SNUserLoginDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public static SNUser loginRequest(SNUserLoginDTO userLoginDTO) {
        ResponseEntity<SNUser> responseEntity = restTemplate.exchange("http://user-service/login/form",
                HttpMethod.POST, new HttpEntity<>(userLoginDTO), SNUser.class);
        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            return responseEntity.getBody();
        }
        return null;
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