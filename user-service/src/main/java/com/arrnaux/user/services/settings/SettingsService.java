package com.arrnaux.user.services.settings;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.user.data.SNUserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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
                                                     @RequestBody String base64Image) {
        log.info("User: " + userId + " is changing the profile image.");
        try {
            SNUser snUser = SNUser.builder()
                    .id(userId)
                    .build();
            snUser = snUserDAO.replaceProfileImage(snUser, base64Image);
            if (null != snUser) {
                return new ResponseEntity<>(snUser.getProfileImageBase64(), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "changePassword", method = RequestMethod.PATCH)
    public ResponseEntity<Boolean> changePassword(@RequestBody Map<String, Object> parameters) {
        ObjectMapper objectMapper = new ObjectMapper();
        SNUser oldUser = objectMapper.convertValue(parameters.get("oldUser"), SNUser.class);
        String newPass = objectMapper.convertValue(parameters.get("newPassword"), String.class);
        if (null != oldUser && null != newPass) {
            SNUser targetUser = snUserDAO.findUserByEmailAndPlainPassword(oldUser.getEmail(), oldUser.getPassword());
            if (null == targetUser) {
                return new ResponseEntity<>(false, HttpStatus.ACCEPTED);
            }
            targetUser.obfuscateUserInformation();
            if (null != snUserDAO.updateUserPassword(targetUser, newPass)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
    }
}
