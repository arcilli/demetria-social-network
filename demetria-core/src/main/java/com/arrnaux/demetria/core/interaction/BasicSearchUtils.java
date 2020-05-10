package com.arrnaux.demetria.core.interaction;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BasicSearchUtils {
    private static final String serviceBaseUrl = "http://user-service:8080/";

    public static List<SNUser> getUserWithInsensitiveNames(RestTemplate restTemplate, String[] queryTerms) {
        String targetUrl = serviceBaseUrl + "search/user/caseInsensitive/";
        return restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(queryTerms),
                new ParameterizedTypeReference<List<SNUser>>() {
                }).getBody();
    }

    public static List<SNUser> getUsersWithPartialInsensitiveNames(RestTemplate restTemplate, String[] queryTerms) {
        String targetUrl = serviceBaseUrl + "search/user/caseInsensitive/partial/";
        return restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(queryTerms),
                new ParameterizedTypeReference<List<SNUser>>() {
                }).getBody();
    }
}
