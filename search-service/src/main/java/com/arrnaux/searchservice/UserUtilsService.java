package com.arrnaux.searchservice;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

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
    public static List<SNUser> getUserWithInsensitiveNames(String[] queryTerms) {
        String targetURL = "http://user-service/search/caseInsensitive/user";
        return restTemplate.exchange(targetURL, HttpMethod.POST, new HttpEntity<>(queryTerms),
                new ParameterizedTypeReference<List<SNUser>>() {
                }).getBody();
    }
}
