package com.arrnaux.frontend.util.users;

import com.arrnaux.demetria.core.interaction.BasicUserUtils;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userAccount.SNUserLoginDTO;
import com.arrnaux.demetria.core.models.userAccount.SNUserRegistrationDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Service
public class UserUtilsService {
    private static final String baseTargetUrl = "http://user-service:8080/";

    private static RestTemplate restTemplate;

    private final RestTemplate autowiredComponent;

    public UserUtilsService(RestTemplate autowiredComponent) {
        this.autowiredComponent = autowiredComponent;
    }

    @PostConstruct
    private void init() {
        restTemplate = this.autowiredComponent;
    }

    public static ResponseEntity<Boolean> executeRegisterRequest(SNUserRegistrationDTO snUserRegistrationDTO) {
        return BasicUserUtils.registerRequest(restTemplate, snUserRegistrationDTO);
    }

    public static SNUser executeLoginRequest(SNUserLoginDTO userLoginDTO) {
        String targetUrl = baseTargetUrl + "login/form";
        ResponseEntity<SNUser> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.POST,
                new HttpEntity<>(userLoginDTO), SNUser.class);
        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            return responseEntity.getBody();
        }
        return null;
    }

    public static ResponseEntity<SNUser> executeUserSettingsChangeRequest(SNUser updatedUser) {
        String targetUrl = baseTargetUrl + "settings/profile";
        return restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(updatedUser), SNUser.class);
    }

    public static ResponseEntity<String> executeChangePhotoRequest(String profilePicture, SNUser user) throws IOException {
        String targetUrl = baseTargetUrl + "settings/changeProfileImage/" + user.getId();
        return restTemplate.exchange(targetUrl, HttpMethod.POST,
                new HttpEntity<>(profilePicture), String.class);
    }

    @Nullable
    public static SNUser getObfuscatedUserByUserName(@Nullable String userName) {
        return BasicUserUtils.getObfuscatedUserByUserName(restTemplate, userName);
    }

    @Nullable
    public static SNUser getObfuscatedUserById(@Nullable String userId) {
        return BasicUserUtils.getObfuscatedUserById(restTemplate, userId);
    }

    public static boolean deleteUser(SNUser snUser) {
        return BasicUserUtils.deleteUser(restTemplate, snUser);
    }

    public static ResponseEntity<Boolean> changeUserPassword(Map<String, Object> oldUserAndNewPassword) {
        return BasicUserUtils.changePassword(restTemplate, oldUserAndNewPassword);
    }
}