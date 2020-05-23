package com.arrnaux.demetria.core.interaction;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userAccount.SNUserRegistrationDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class BasicUserUtils {

    private static final String serviceBaseUrl = "http://user-service:8080/";

    @Nullable
    public static SNUser getObfuscatedUserByUserName(RestTemplate restTemplate, @Nullable String userName) {
        if (null != userName) {
            String targetUrl = serviceBaseUrl + "users/info/user/" + userName;
            ResponseEntity<SNUser> snUserResponseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET,
                    HttpEntity.EMPTY, SNUser.class);
            return snUserResponseEntity.getBody();
        }
        return null;
    }

    @Nullable
    public static SNUser getObfuscatedUserById(RestTemplate restTemplate, @Nullable String userId) {
        if (null != userId) {
            String targetUrl = serviceBaseUrl + "users/info/id/" + userId;
            ResponseEntity<SNUser> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET,
                    HttpEntity.EMPTY, SNUser.class);
            return responseEntity.getBody();
        }
        return null;
    }

    public static ResponseEntity<Boolean> registerRequest(RestTemplate restTemplate, SNUserRegistrationDTO snUserRegistrationDTO) {
        String targetUrl = serviceBaseUrl + "register";
        return restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(snUserRegistrationDTO), Boolean.class);
    }

    public static Boolean deleteUser(RestTemplate restTemplate, SNUser snUser) {
        String targetURL = serviceBaseUrl + "settings/deleteAccount";
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(targetURL, HttpMethod.DELETE,
                new HttpEntity<>(snUser), Boolean.class);
        if (null != responseEntity.getBody()) {
            return responseEntity.getBody();
        }
        return false;
    }

    public static ResponseEntity<Boolean> changePassword(RestTemplate restTemplate, Map<String, Object> parameters) {
        String targetUrl = serviceBaseUrl + "settings/changePassword";
        return restTemplate.exchange(targetUrl, HttpMethod.PATCH,
                new HttpEntity<>(parameters), Boolean.class);
    }
}
