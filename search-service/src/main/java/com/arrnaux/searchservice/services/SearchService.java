package com.arrnaux.searchservice.services;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.searchservice.UserUtilsService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @return a list of user that correspond 1:1 to the query.
     */
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public List<SNUser> searchForUser(@RequestBody String query) {
        String[] queryTerms = Arrays.stream(query.split("\\s"))
                .map(String::toLowerCase)
                .toArray(String[]::new);
        log.info("Searching user with: " + Arrays.toString(queryTerms));

        // Retrieve a list with persons that correspond to the search criteria.
        List<SNUser> exactMatchedUsers = UserUtilsService.getUserWithInsensitiveNames(queryTerms);
        if (null != exactMatchedUsers) {
            for (SNUser user : exactMatchedUsers) {
                user.obfuscateUserInformation();
            }
        } else {
            // TODO: return a partial result  & change the description of the method.
        }
        return exactMatchedUsers;
    }
}