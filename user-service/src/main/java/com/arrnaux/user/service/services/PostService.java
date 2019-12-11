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

@RequestMapping(value = "postService")
public class PostService {

    @Autowired
    private SNPostDAO snPostDAO;

    @RequestMapping(value = "", method = RequestMethod.POST)
    // TODO: need to bring here a token for user/another method for authorize the actual request
    // returns the id of the post
    public String savePost(@RequestBody SNPost snPost) {
        try {
            SNPost savedPost = snPostDAO.savePost(snPost);
            return savedPost.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Boolean deletePost(@RequestBody SNPost post) {
        try {
            Long nrOfDeletedPosts = snPostDAO.removePost(post.getId());
            // the delete ended with success
            if (nrOfDeletedPosts == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // snPost arg will conain a single comment
    @RequestMapping(value="createComment", method = RequestMethod.POST)
    public String appendCommentToExistingList(@RequestBody SNPost snPost){
        try{
            if (snPost.getCommentList().size() == 1) {
                snPostDAO.savePost(snPost);

//                String newCommentId = snPosDAO.saveP
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
