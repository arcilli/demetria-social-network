package com.arrnaux.user.service.services;

import com.arrnaux.demetria.core.userAccount.data.SNUserDAO;
import com.arrnaux.demetria.core.userAccount.model.SNUser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            String hashedPassword = snUserDAO.findUserByEmail(snUser.getEmail()).getPassword();
            if (null != hashedPassword) {
                snUser.setPassword(hashedPassword);
                SNUser modifiedUser = snUserDAO.saveSNUser(snUser);
                return new ResponseEntity<SNUser>(modifiedUser, HttpStatus.OK);
            }
        }
        return new ResponseEntity<SNUser>((SNUser) null, HttpStatus.FORBIDDEN);
    }
}
