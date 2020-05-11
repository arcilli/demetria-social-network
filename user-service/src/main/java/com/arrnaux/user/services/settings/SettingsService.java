package com.arrnaux.user.services.settings;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.user.data.SNUserDAO;
import com.arrnaux.user.utils.Base64Handler;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Objects;

@Log
@RestController
@RequestMapping("settings")
public class SettingsService {

    private final SNUserDAO snUserDAO;

    public SettingsService(SNUserDAO snUserDAO) {
        this.snUserDAO = snUserDAO;
    }

    @PostMapping("profile")
    public ResponseEntity<SNUser> changeUserDetails(@RequestBody SNUser snUser) {
        log.info("Attempt to edit user details for: " + snUser.getEmail());
        // Initialize password with the stored value, since others layers don't have access at it.
        if (null != snUserDAO.findUserByEmail(snUser.getEmail())) {
            String hashedPassword = Objects.requireNonNull(snUserDAO.findUserByEmail(snUser.getEmail())).getPassword();
            if (null != hashedPassword) {
                snUser.setPassword(hashedPassword);
                SNUser modifiedUser = snUserDAO.saveSNUser(snUser);
                return new ResponseEntity<>(modifiedUser, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>((SNUser) null, HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "deleteAccount", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteUserAccount(@RequestBody SNUser snUser) {
        log.info("Attempt to delete user: " + snUser.getId());
        if (null != snUserDAO.findUserByEmail(snUser.getEmail())) {
            Boolean result = snUserDAO.removeUserAccount(snUser.getEmail());
            if (result) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
        }
        return null;
    }

    @RequestMapping(value = "changeProfileImage/{userId}", method = RequestMethod.POST)
    public ResponseEntity<String> changeProfileImage(@PathVariable("userId") String userId,
                                                     @RequestBody byte[] image) {
        log.info("User: " + userId + " is changing the profile image.");
        // Convert profilePicture to Base64.
        try {
            String encodedImage = Base64.getEncoder().encodeToString(image);
            SNUser snUser = SNUser.builder()
                    .id(userId)
                    .build();
            String imageType = Base64Handler.getImageType(encodedImage.charAt(0));
            if (null != imageType) {
                encodedImage = "data:image/" + imageType + ";base64, " + encodedImage;
            }
            snUser = snUserDAO.replaceProfileImage(snUser, encodedImage);
            if (null != snUser) {
                return new ResponseEntity<>(snUser.getProfileImageBase64(), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
