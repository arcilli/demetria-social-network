package com.arrnaux.userservice.services;

import com.arrnaux.userservice.data.SNUserRepository;
import com.arrnaux.userservice.model.SNUser;
import com.arrnaux.userservice.model.SNUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationService {

    @Autowired
    private SNUserRepository snUserRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String register() {
//        snUserRepository.deleteAll();

//        snUserRepository.save(new SNUser(3, "Mihai", "Geroge"));
        snUserRepository.save(new SNUser(
                9,
                "Nicu",
                "Boca",
                "pass",
                "mail.boca.nicolae@gmail.com"
        ));
        System.out.println("Customers found with findAll():");
        StringBuilder s = new StringBuilder();
        System.out.println("-------------------------------");
        for (SNUser user : snUserRepository.findAll()) {
            System.out.println(user);
            s.append((user.toString()));
        }
        return s.toString();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String registerUser(SNUserDTO SNUserDTO) {
//        if (isValidRequest(SNUserDTO)) {
//            // TODO: go futher
//        }
        return "safsafa";
    }

    private boolean persistUserInformation(SNUserDTO SNUserDTO) {
        snUserRepository.save(SNUserDTO.toUser());
        snUserRepository.deleteAll();
        for (SNUser SNUser : snUserRepository.findAll()) {
            System.out.println(SNUser);
        }
        return true;
    }
}
