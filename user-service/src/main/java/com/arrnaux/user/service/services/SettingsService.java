package com.arrnaux.user.service.services;

import com.arrnaux.demetria.core.userAccount.data.SNUserDAO;
import com.arrnaux.demetria.core.userAccount.model.SNUser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Log4j
@RequestMapping("settings")
public class SettingsService {

    @Autowired
    private SNUserDAO snUserDAO;

    @PostMapping("profile")
    public ResponseEntity<SNUser> changeUserDetails(@RequestBody SNUser snUser) {
        // method invocation may produce NPE
        // TODO: use Objects.requireNonNull
        log.info("Attempt to edit user details for: " + snUser.getEmail());

        // initialize password with the stored value, since others layers don't have access at it
        if (null != snUserDAO.findUserByEmail(snUser.getEmail())) {
            // method invocation may produce NPE
            // TODO: use Objects.requireNonNull
            String hashedPassword = Objects.requireNonNull(snUserDAO.findUserByEmail(snUser.getEmail())).getPassword();
            if (null != hashedPassword) {
                snUser.setPassword(hashedPassword);
                SNUser modifiedUser = snUserDAO.saveSNUser(snUser);
                return new ResponseEntity<>(modifiedUser, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>((SNUser) null, HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "deleteAccount", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteUserAccount(@RequestBody SNUser snUser) {
        log.info("Attempt to delete user: " + snUser.getEmail());
        if (null != snUserDAO.findUserByEmail(snUser.getEmail())) {
            Boolean result = snUserDAO.removeUserAccount(snUser.getEmail());
            if (result) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
        }
        return null;
    }
}
