package com.arrnaux.userservice.userAccount.services;

import com.arrnaux.userservice.userAccount.data.SNUserDAO;
import com.arrnaux.userservice.userAccount.model.SNUser;
import com.arrnaux.userservice.userAccount.model.SNUserLoginDTO;
import com.arrnaux.userservice.userAccount.model.Token;
import com.arrnaux.userservice.userAccount.model.TokenAuthority;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "login")
@Log4j
public class Login {
    @Autowired
    private SNUserDAO snUserDAO;

    @Autowired
    private TokenAuthority tokenAuthority;

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public Token userLogin(@RequestBody SNUserLoginDTO userDTO) {
        log.info("User " + userDTO.getEmail() + " is trying to login with: " + userDTO.getPassword());
        SNUser snUser = snUserDAO.findUserByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword());
        if (snUser != null) {
            Token generatedToken = tokenAuthority.generateToken(snUser);
            log.info("User " + userDTO.getEmail() + " has logged in.");
            return generatedToken;
        }
        log.info("Login attempt failed for  with email: " + userDTO.getEmail());
        return new Token("-1");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String speak2(@PathVariable("id") String id) {
        return "/login" + id;
    }
}