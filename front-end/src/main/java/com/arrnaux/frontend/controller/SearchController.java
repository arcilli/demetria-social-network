package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("search")
public class SearchController {

    final
    RestTemplate restTemplate;

    public SearchController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "user", method = RequestMethod.POST,
            consumes = {"application/x-www-form-urlencoded"})
    public ModelAndView searchUser(HttpServletRequest request, String queryBoxContent) {
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        if (null != loggedUser) {
            String targetUrl = "http://search-service/search/user";
            List<SNUser> users = restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(queryBoxContent),
                    new ParameterizedTypeReference<List<SNUser>>() {
                    }).getBody();
            if (null != users) {
                modelAndView.addObject("foundUsers", users);
            }
            modelAndView.setViewName("redirect:search/result");
        } else {
            modelAndView.setViewName("redirect:index");
        }
        // TODO: set view name as a query
        return modelAndView;
    }

    @RequestMapping(value = "result", method = RequestMethod.GET)
    public ModelAndView showResults(ModelAndView modelAndView, HttpServletRequest request) {
        modelAndView.setViewName("results");
        return modelAndView;
    }
}
