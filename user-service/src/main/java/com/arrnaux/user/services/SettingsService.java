package com.arrnaux.user.services;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.user.data.SNUserDAO;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Log4j
@RequestMapping("settings")
public class SettingsService {

    private final SNUserDAO snUserDAO;

    public SettingsService(SNUserDAO snUserDAO) {
        this.snUserDAO = snUserDAO;
    }

    @PostMapping("profile")
    public ResponseEntity<SNUser> changeUserDetails(@RequestBody SNUser snUser) {
        log.info("Attempt to edit user details for: " + snUser.getEmail());
        // Initialize password with the stored value, since others layers don't have access at it
        if (null != snUserDAO.findUserByEmail(snUser.getEmail())) {
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
