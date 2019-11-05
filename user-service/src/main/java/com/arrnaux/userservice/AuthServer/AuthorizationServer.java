package com.arrnaux.userservice.AuthServer;

import com.arrnaux.userservice.userAccount.model.Token;
import com.arrnaux.userservice.userAccount.model.TokenAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("auth")
@RestController
// TODO: this should authorize requests by checking the JWT
public class AuthorizationServer {

    @Autowired
    private TokenAuthority tokenAuthority;

    @RequestMapping(value = "/tokenValidation", method = RequestMethod.POST)
    public boolean validateToken(@RequestBody Token token) {
        return tokenAuthority.tokenIsValid(token);
    }
}
