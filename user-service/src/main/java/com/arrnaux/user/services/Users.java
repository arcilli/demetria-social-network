package com.arrnaux.user.services;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.user.data.SNUserDAO;
import lombok.extern.log4j.Log4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "users")
@Log4j
public class Users {

    final
    SNUserDAO snUserDAO;

    public Users(SNUserDAO snUserDAO) {
        this.snUserDAO = snUserDAO;
    }

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
