package com.arrnaux.userservice;

import com.arrnaux.userservice.data.SNUserDAO;
import com.arrnaux.userservice.model.SNUser;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class UserContextStub implements UserContext {
    private final SNUserDAO SNUserDAO;

    private long currentUserId = 0;

    @Autowired
    public UserContextStub(SNUserDAO SNUserDAO) {
        if (null == SNUserDAO) {
            throw new IllegalArgumentException("SNUser service cannot be null");
        }
        this.SNUserDAO = SNUserDAO;
    }

    @Override
    public SNUser getCurrentUser() {
        return SNUserDAO.getUser(currentUserId);
    }

    @Override
    public void setCurrentUser(SNUser SNUser) {
        if (null == SNUser) {
            throw new IllegalArgumentException("SNUser cannot be null");
        }
        Long currentId = SNUser.getId();
        if (currentId == null) {
            throw new IllegalArgumentException("SNUser.getId() cannot be null");
        }
        this.currentUserId = currentId;
    }
}
