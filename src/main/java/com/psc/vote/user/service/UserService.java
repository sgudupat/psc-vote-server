package com.psc.vote.user.service;

import com.psc.vote.user.domain.User;
import com.psc.vote.user.domain.UserReward;
import com.psc.vote.user.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserService {

    @Autowired
    UserManager userManager;

    public User getUser(User user) {
        return userManager.getUser(user);
    }

    public void registerUser(User user) throws Exception {
        userManager.registerUser(user);
    }

    public void updateUser(User user) throws Exception {
        userManager.updateUser(user);
    }

    public void updatePassword(User user) throws Exception {
        userManager.updatePassword(user);
    }

    public List<UserReward> showRewards(String username) throws Exception {
        return userManager.showRewards(username);
    }
}
