package com.arrnaux.searchservice.util;

import com.arrnaux.demetria.core.interaction.BasicSearchUtils;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
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
        return BasicSearchUtils.getUserWithInsensitiveNames(restTemplate, queryTerms);
    }

    @Nullable
    public static List<SNUser> getUsersWithPartialInsensitiveNames(String[] queryTerms) {
        return BasicSearchUtils.getUsersWithPartialInsensitiveNames(restTemplate, queryTerms);
    }
}
