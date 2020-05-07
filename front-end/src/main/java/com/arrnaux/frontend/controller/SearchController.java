package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.frontend.util.search.SearchUtilsService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("search")
public class SearchController {

    final
    RestTemplate restTemplate;

    public SearchController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "user", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView searchUser(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        if (null == request.getParameter("queryBoxContent")) {
            modelAndView.setViewName("redirect:/index");
            return modelAndView;
        }

        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (null != loggedUser) {
            String query = request.getParameter("queryBoxContent");
            List<SNUser> foundUsers = SearchUtilsService.getUsersByQuery(query);
            if (null != foundUsers) {
                modelAndView.addObject("foundUsers", new HashSet<>(foundUsers));
                modelAndView.setViewName("search/results");
            }
        } else {
            modelAndView.setViewName("redirect:/index");
        }
        // TODO: set view name as a query
        return modelAndView;
    }
}
