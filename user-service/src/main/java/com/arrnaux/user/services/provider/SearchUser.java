package com.arrnaux.user.services.provider;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.user.data.SNUserDAO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("search")
public class SearchUser {
    final SNUserDAO snUserDAO;

    public SearchUser(SNUserDAO snUserDAO) {
        this.snUserDAO = snUserDAO;
    }

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public List<SNUser> getUsersForQuery(@RequestBody String[] queryTerms) {
        return snUserDAO.findUserByNameQuery(queryTerms);
    }

    @RequestMapping(value = "user/caseInsensitive/", method = RequestMethod.POST)
    public List<SNUser> getCaseInsensitiveUsersForQuery(@RequestBody String[] queryTerms) {
        return snUserDAO.findUserByInsensitiveQuery(queryTerms);
    }

    @RequestMapping(value = "/user/caseInsensitive/partial/", method = RequestMethod.POST)
    public List<SNUser> getUsersByPartialCaseInsensitiveName(@RequestBody String[] queryTerms) {
        return snUserDAO.findUserByPartialInsensitiveQuery(queryTerms);
    }
}