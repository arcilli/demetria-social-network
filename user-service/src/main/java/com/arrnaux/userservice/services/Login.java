package com.arrnaux.userservice.services;

import com.arrnaux.userservice.data.SNUserDAO;
import com.arrnaux.userservice.model.SNUser;
import com.arrnaux.userservice.model.SNUserDTO;
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
    
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public Token speak(@RequestBody SNUserDTO userDTO) {
        logger.info("User " + userDTO.getEmail() + " is trying to login with: " + userDTO.getPassword());
        SNUser snUser = snUserDAO.findUserByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword());
        if (snUser != null) {
            Token generatedToken = tokenAuthority.getToken(snUser);
            logger.info("User " + userDTO.getEmail() + " has logged in.s");
            return generatedToken;
        }
        logger.info("Login attempt failed for  with email: " + userDTO.getEmail());
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String speak2(@PathVariable("id") String id) {
        return "/login" + id;
    }
}