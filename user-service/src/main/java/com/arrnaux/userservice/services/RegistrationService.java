package com.arrnaux.userservice.services;

import com.arrnaux.userservice.data.SNUserDAO;
import com.arrnaux.userservice.model.SNUser;
import com.arrnaux.userservice.model.SNUserRegistrationDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationService {
    final static Logger logger = Logger.getLogger(Login.class);

    @Autowired
    private SNUserDAO snUserDAO;

    // TODO: this should redirect to login page
    @RequestMapping(value = "", method = RequestMethod.POST)
    public boolean registerUser(@RequestBody SNUserRegistrationDTO snUserRegistrationDTO) throws Exception {
        logger.info("Attempt to register with info: " + snUserRegistrationDTO);
        // TODO: add a check for not-null user properties
        SNUser user = snUserDAO.findUserByEmail(snUserRegistrationDTO.getEmail());
        if (user != null) {
            // throw an exception - an user with same email already exists
            Exception e = new Exception("User with same email already exists");
            logger.error("User with same email already exists", e);
            throw e;
        } else {
            snUserDAO.saveSNUser(new SNUser(snUserRegistrationDTO));
            logger.info("Registerd user with info: " + snUserRegistrationDTO);
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

//    private boolean persistUserInformation(SNUserLoginDTO SNUserLoginDTO) {
//        snUserRepository.save(SNUserLoginDTO.toUser());
//        snUserRepository.deleteAll();
//        for (com.arrnaux.userservice.model.SNUser SNUser : snUserRepository.findAll()) {
//            System.out.println(SNUser);
//        }
//        return true;
//    }
}
