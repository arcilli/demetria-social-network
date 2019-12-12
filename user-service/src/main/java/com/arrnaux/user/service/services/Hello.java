package com.arrnaux.user.service.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("hello")
@RestController
public class Hello {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String sayHelloToSmartOne() {
        return "Hello";
    }
}
