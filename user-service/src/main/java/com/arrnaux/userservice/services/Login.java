package com.arrnaux.userservice.services;

import com.arrnaux.userservice.data.SNUserDAO;
import com.arrnaux.userservice.model.SNUser;
import com.arrnaux.userservice.model.Token;
import com.arrnaux.userservice.model.TokenAuthority;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "login")
public class Login {
    final static Logger logger = Logger.getLogger(Login.class);
    @Autowired
    private SNUserDAO snUserDAO;

    @Autowired
    private TokenAuthority tokenAuthority;

    // Should receive a snUserDTO?
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public Token speak(@RequestParam("email") String email,
                       @RequestParam("password") String password) {
        logger.info("User " + email + " is trying to login with: " + password);
        SNUser snUser = snUserDAO.findUserByEmailAndPassword(email, password);
        if (snUser != null) {
            Token generatedToken = tokenAuthority.getToken(snUser);
            logger.info("User " + email + " has logged in.");
            return generatedToken;
        }
        logger.info("Login attempt failed for  with email: " + email);
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String speak2(@PathVariable("id") String id) {
        return "/login" + id;
    }
}