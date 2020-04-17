package com.arrnaux.searchservice.services;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("search")
@Log
public class SearchService {

    final
    RestTemplate restTemplate;

    @Autowired
    public SearchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @param query
     * @return
     */
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public List<SNUser> searchForUser(@RequestBody String query) {
        String[] queryTerms = query.split("\\s");
        log.info("Searching user with: " + Arrays.toString(queryTerms));

        // Retrieve a list with persons that correspond to the search criteria.
        String targetURL = "http://user-service/search/user";
        List<SNUser> users = restTemplate.exchange(targetURL, HttpMethod.POST, new HttpEntity<>(queryTerms),
                new ParameterizedTypeReference<List<SNUser>>() {
                }).getBody();
        if (null != users) {
            for (SNUser user : users) {
                user.obfuscateUserInformation();
            }
        } else {
            // TODO: if no exact match was found, return a partial one.
        }
        return users;
    }
}