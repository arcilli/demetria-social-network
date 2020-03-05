package com.arrnaux.user.services;

import com.arrnaux.demetria.core.models.userAccount.PasswordUtils;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userAccount.SNUserRegistrationDTO;
import com.arrnaux.user.data.SNUserDAO;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Log4j
@RestController
@RequestMapping("/register")
public class RegisterService {

    private final SNUserDAO snUserDAO;

    public RegisterService(SNUserDAO snUserDAO) {
        this.snUserDAO = snUserDAO;
    }

    // TODO: decide on a method to work with errors
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity registerUser(@RequestBody SNUserRegistrationDTO snUserRegistrationDTO) {
        log.info("Attempt to register an user with info: " + snUserRegistrationDTO);
        // TODO: throw an error
        if (!snUserRegistrationDTO.registrationFormIncomplete()) {
            SNUser user = snUserDAO.findUserByEmail(snUserRegistrationDTO.getEmail());
            if (null != user) {
                log.error("An user with same email already exists");
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                // throw an error for this case
                if (!snUserRegistrationDTO.passwordIsMatched()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                // user with same username
                SNUser userByUsername = snUserDAO.findUserByUsername(snUserRegistrationDTO.getUserName());
                if (null != userByUsername) {
                    // An user with same username exists.
                    return new ResponseEntity<>(false, HttpStatus.IM_USED);
                }

                Optional<String> hashedPassword = PasswordUtils.hashPassword(snUserRegistrationDTO.getPassword(), null);
                if (hashedPassword.isPresent()) {
                    snUserRegistrationDTO.setPassword(hashedPassword.get());
                    SNUser savedUser = snUserDAO.saveSNUser(new SNUser(snUserRegistrationDTO));
                    log.info("Registered user with info: " + savedUser);
                    return new ResponseEntity<>(true, HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}