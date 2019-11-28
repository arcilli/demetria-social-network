package com.arrnaux.user.service.services;

import com.arrnaux.demetria.core.userAccount.data.SNUserDAO;
import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userAccount.model.SNUserLoginDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "login")
@Log4j
public class Login {
    @Autowired
    private SNUserDAO snUserDAO;

    // not sure that this is used for something any mroe
//    @Autowired
//    private TokenAuthority tokenAuthority;

    // TODO: start a session when a user has logged in
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public ResponseEntity<SNUser> userLogin(@RequestBody SNUserLoginDTO userDTO) {
        //log.info("User " + userDTO.getEmail() + " is trying to login with: " + userDTO.getPassword());
        SNUser snUser = snUserDAO.findUserByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword());
        if (snUser != null) {
            log.info("User " + userDTO.getEmail() + " has logged in.");
            snUser.setPassword("");
            return new ResponseEntity<SNUser>(snUser, HttpStatus.ACCEPTED);
        }
        log.info("Login attempt failed for  with email: " + userDTO.getEmail());

        // snUser is null
        return new ResponseEntity<SNUser>(snUser, HttpStatus.FORBIDDEN);
    }
}