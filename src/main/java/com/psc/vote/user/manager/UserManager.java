package com.psc.vote.user.manager;

import com.psc.vote.user.dao.UserDao;
import com.psc.vote.user.dao.UserRewardsDao;
import com.psc.vote.user.domain.User;
import com.psc.vote.user.domain.UserReward;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserManager {

    @Autowired
    UserDao userDao;

    @Autowired
    UserRewardsDao userRewardsDao;

    public User getUser(User user) {
        return userDao.getUser(user);
    }

    public void registerUser(User user) throws Exception {
        userDao.registerUser(user);
    }

    public void updateUser(User user) throws Exception {
        userDao.updateUser(user);
    }

    public void updatePassword(User user) throws Exception {
        userDao.updatePassword(user);
    }

    public List<UserReward> showRewards(String username) throws Exception {
        return userRewardsDao.getUserRewards(username);
    }
}
