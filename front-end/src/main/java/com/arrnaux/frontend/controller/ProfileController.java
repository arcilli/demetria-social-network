package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.data.SNPostDAO;
import com.arrnaux.demetria.core.userPost.model.Comment;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {

    // Use the restTemplate declared in main Class
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SNPostDAO snPostDAO;

    // see what information should an user
    @GetMapping(value = "profile")
    public ModelAndView displayProfileEditor(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (loggedUser != null) {
            // user's info will be retrieved from session
//            modelAndView.addObject("modifiedUser", loggedUser);

            // TODO: should load only a chunk from user posts
            // TODO: the connection with DBs should be only on user service
            List<SNPost> userPosts = snPostDAO.getUserPostsDateDesc(loggedUser);
            modelAndView.addObject("userPosts", userPosts);
            modelAndView.addObject("newComment", new Comment());
            modelAndView.setViewName("profile");
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }

    @PostMapping(value = "profile")
    public ModelAndView saveProfileChanges(HttpServletRequest request, @ModelAttribute SNUser modifiedUser) {
        ModelAndView modelAndView = new ModelAndView();
        HttpEntity<SNUser> httpEntity = new HttpEntity<>(modifiedUser);
        try {
            ResponseEntity<SNUser> responseEntity =
                    restTemplate.exchange("http://user-service/post", HttpMethod.POST, httpEntity, SNUser.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // the information has been updated
                // invalidate previous session
                request.getSession().setAttribute("user", modifiedUser);
                modelAndView.addObject("informationUpdated", true);
                modelAndView.setViewName("profile");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }
}
