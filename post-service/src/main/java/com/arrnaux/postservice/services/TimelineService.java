package com.arrnaux.postservice.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "timeline")
public class TimelineService {

    @RequestMapping(method = RequestMethod.GET)
    public String sayHello() {
        return "hello";
    }
}
