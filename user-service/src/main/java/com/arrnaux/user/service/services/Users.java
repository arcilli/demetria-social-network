package com.arrnaux.user.service.services;

import com.arrnaux.demetria.core.userAccount.data.SNUserDAO;
import com.arrnaux.demetria.core.userAccount.model.SNUser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "users")
@Log4j
public class Users {

    @Autowired
    SNUserDAO snUserDAO;

    /**
     * @param username
     * @return an user with obfuscated information or null if the user with associated username does not exist.
     */
    @Nullable
    @RequestMapping(value = "/info/{username}", method = RequestMethod.POST)
    public SNUser getUserWithObfuscatedInfo(@PathVariable("username") String username) {
        SNUser snUser = snUserDAO.findUserByUsername(username);
        if (null != snUser) {
            snUser.obfuscateUserInformation();
        }
        return snUser;
    }
}
