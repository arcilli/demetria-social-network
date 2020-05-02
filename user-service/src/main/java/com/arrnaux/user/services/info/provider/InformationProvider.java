package com.arrnaux.user.services.info.provider;

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
public class InformationProvider {

    final
    SNUserDAO snUserDAO;

    public InformationProvider(SNUserDAO snUserDAO) {
        this.snUserDAO = snUserDAO;
    }

    /**
     * @param userName
     * @return an user with obfuscated information or null if the user with associated username does not exist.
     */
    @Nullable
    @RequestMapping(value = "/info/user/{username}", method = RequestMethod.GET)
    public SNUser getUserWithObfuscatedInfo(@PathVariable("username") String userName) {
        return getObfuscatedUserByUsername(userName);
    }

    @Nullable
    @RequestMapping(value = "/info/id/{userId}", method = RequestMethod.GET)
    public SNUser getObfuscatedUserByIdPath(@PathVariable("userId") String userId) {
        SNUser snUser = snUserDAO.findById(userId);
        if (null != snUser) {
            return snUser.obfuscateUserInformation();
        }
        return null;
    }

    @Nullable
    private SNUser getObfuscatedUserByUsername(String username) {
        SNUser snUser = snUserDAO.findUserByUsername(username);
        if (null != snUser) {
            snUser.obfuscateUserInformation();
        }
        return snUser;
    }
}
