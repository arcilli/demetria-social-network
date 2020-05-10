package com.arrnaux.frontend.util.search;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class SearchUtilsService {
    private static RestTemplate restTemplate;

    private final RestTemplate autowiredComponent;

    public SearchUtilsService(RestTemplate autowiredComponent) {
        this.autowiredComponent = autowiredComponent;
    }

    @PostConstruct
    private void init() {
        restTemplate = this.autowiredComponent;
    }

    public static List<SNUser> getUsersByQuery(String query) {
        String targetUrl = "http://search-service:8085/search/user";
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(query),
                new ParameterizedTypeReference<List<SNUser>>() {
                }).getBody();

        // TODO: add some fof suggestions
    }
}
