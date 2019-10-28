package com.arrnaux.userservice.registration;

import com.arrnaux.userservice.model.User;
import com.arrnaux.userservice.model.UserDTO;
import com.arrnaux.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

//    @RequestMapping(value = "", method = RequestMethod.GET)
//    public String register() {
//        userRepository.deleteAll();
//
//        userRepository.save(new User(3, "Mihai", "Geroge"));
//        userRepository.save(new User(4, "Ciobanu", "Ghita"));
//        System.out.println("Customers found with findAll():");
//        StringBuilder s = new StringBuilder();
//        System.out.println("-------------------------------");
//        for (User user : userRepository.findAll()) {
//            System.out.println(user);
//            s.append((user.toString()));
//        }
//        return s.toString();
//    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String registerUser(@Valid UserDTO userDTO) {
//        if (isValidRequest(userDTO)) {
//            // TODO: go futher
//        }
        return "safsafa";
    }

    private boolean persistUserInformation(UserDTO userDTO) {
        userRepository.save(userDTO.toUser());
        userRepository.deleteAll();
        for (User user : userRepository.findAll()) {
            System.out.println(user);
        }
        return true;
    }
}
