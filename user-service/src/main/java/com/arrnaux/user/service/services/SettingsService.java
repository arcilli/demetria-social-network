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
        // Do some validation
        //
        SNUser modifiedUser = snUserDAO.saveSNUser(snUser);
        log.info("Attempt to edit user details for: " + modifiedUser.getEmail());
        return new ResponseEntity<SNUser>(snUser, HttpStatus.OK);
    }
}
