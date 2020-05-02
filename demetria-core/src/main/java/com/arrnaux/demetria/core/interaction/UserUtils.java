package com.arrnaux.demetria.core.interaction;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

public class UserUtils {

    @Nullable
    public static SNUser getObfuscatedUserByUserName(RestTemplate restTemplate, @Nullable String userName) {
        if (null != userName) {
            String targetUrl = "http://user-service/users/info/user/" + userName;
            ResponseEntity<SNUser> snUserResponseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET,
                    HttpEntity.EMPTY, SNUser.class);
            return snUserResponseEntity.getBody();
        }
        return null;
    }

    @Nullable
    public static SNUser getObfuscatedUserById(RestTemplate restTemplate, @Nullable String userId) {
        if (null != userId) {
            String targetUrl = "http://user-service/users/info/id/" + userId;
            ResponseEntity<SNUser> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET,
                    HttpEntity.EMPTY, SNUser.class);
            return responseEntity.getBody();
        }
        return null;
    }
}
