package com.arrnaux.user.service.services;

import com.arrnaux.demetria.core.userPost.data.SNPostDAO;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Log
@RestController
@RequestMapping(value = "posts")

public class PostService {

    @Autowired
    private SNPostDAO snPostDAO;

    @RequestMapping(value = "", method = RequestMethod.POST)
    // TODO: need to bring here a token for user/another method for authorize the actual request
    // returns the id of the post
    public String sharePost(@RequestBody SNPost snPost) {
        snPostDAO.insertPost(snPost);
        return snPost.getId();
    }
}
