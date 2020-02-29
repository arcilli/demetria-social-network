package com.arrnaux.user.service.services;

import com.arrnaux.demetria.core.userAccount.data.SNUserDAO;
import com.arrnaux.demetria.core.userAccount.model.SNUser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Null;

@RestController
@RequestMapping(value = "users")
@Log4j
public class Users {

    @Autowired
    SNUserDAO snUserDAO;

    @RequestMapping(value = "/info/{username}", method = RequestMethod.POST)
    /**
     * return an user with obfuscated information or null if the user with associated username does not exist.
     */
    public SNUser getUserWithObfuscatedInfo(@PathVariable("username") String username) {
        SNUser snUser = snUserDAO.findUserByUsername(username);
        if (null != snUser) {
            snUser.obfuscateUserInformation();
        }
        return snUser;
    }
}
