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
        String targetUrl = "http://user-service/search/user/caseInsensitive/";
        return getUsersWithQueryTerms(targetUrl, queryTerms);
    }

    @Nullable
    public static List<SNUser> getUsersWithPartialInsensitiveNames(String[] queryTerms) {
        String targetUrl = "http://user-service/search/user/caseInsensitive/partial/";
        return getUsersWithQueryTerms(targetUrl, queryTerms);
    }

    /**
     * @param targetUrl
     * @param queryTerms
     * @return a list of obfuscated users
     */
    @Nullable
    public static List<SNUser> getUsersWithQueryTerms(String targetUrl, String[] queryTerms) {
        return restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(queryTerms),
                new ParameterizedTypeReference<List<SNUser>>() {
                }).getBody();
    }
}
