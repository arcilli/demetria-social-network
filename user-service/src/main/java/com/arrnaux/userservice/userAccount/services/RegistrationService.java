package com.arrnaux.userservice.userAccount.services;

import com.arrnaux.userservice.userAccount.data.SNUserDAO;
import com.arrnaux.userservice.userAccount.model.SNUser;
import com.arrnaux.userservice.userAccount.model.SNUserRegistrationDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@Log4j

public class RegistrationService {

    @Autowired
    private SNUserDAO snUserDAO;

    // TODO: this should redirect to login page
    @RequestMapping(value = "", method = RequestMethod.POST)
    public boolean registerUser(@RequestBody SNUserRegistrationDTO snUserRegistrationDTO) throws Exception {
        log.info("Attempt to register an user with info: " + snUserRegistrationDTO);
        // TODO: add a check for not-null user properties
        SNUser user = snUserDAO.findUserByEmail(snUserRegistrationDTO.getEmail());
        if (user != null) {
            // throw an exception - an user with same email already exists
            Exception e = new Exception("User with same email already exists");
            log.error("User with same email already exists", e);
            throw e;
        } else {
//            SNUser userToBeSaved = new SNUser(snUserRegistrationDTO);
            snUserDAO.saveSNUser(new SNUser(snUserRegistrationDTO));
            log.info("Registerd user with info: " + snUserRegistrationDTO);
            return true;
        }
    }

//    @RequestMapping(value = "", method = RequestMethod.GET)
//    public String register() {
////        snUserRepository.deleteAll();
//
////        snUserRepository.save(new SNUser(3, "Mihai", "Geroge"));
//        snUserRepository.save(new SNUser(
//                9,
//                "Nicu",
//                "Boca",
//                "pass",
//                "mail.boca.nicolae@gmail.com"
//        ));
//        System.out.println("Customers found with findAll():");
//        StringBuilder s = new StringBuilder();
//        System.out.println("-------------------------------");
//        for (SNUser user : snUserRepository.findAll()) {
//            System.out.println(user);
//            s.append((user.toString()));
//        }
//        return s.toString();
//    }
}
