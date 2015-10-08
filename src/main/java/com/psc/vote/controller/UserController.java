package com.psc.vote.controller;

import com.psc.vote.common.util.UserUtil;
import com.psc.vote.common.util.VoteUtil;
import com.psc.vote.user.domain.Gender;
import com.psc.vote.user.domain.Notify;
import com.psc.vote.user.domain.User;
import com.psc.vote.user.domain.UserReward;
import com.psc.vote.user.service.UserService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/myRewards")
    @ResponseBody
    public String showRewards(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in showRewards");
        String username = request.getParameter("username");
        System.out.println("User:" + username);
        try {
            List<UserReward> userRewards = userService.showRewards(username);
            return toJSONUserRewards(userRewards);
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public String getUser(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in get user");
        User user = new User();
        user.setUsername(request.getParameter("username"));
        System.out.println("User:" + user);
        try {
            User dbUser = userService.getUser(user);
            return UserUtil.toJSONUser(dbUser);
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/updatePassword")
    @ResponseBody
    public String updatePassword(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in get user");
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(VoteUtil.encryptPassword(request.getParameter("password")));
        System.out.println("User:" + user);
        try {
            userService.updatePassword(user);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public String updateUser(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in update user");
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setAge(request.getParameter("age"));
        user.setGender(String.valueOf(Gender.valueOf((request.getParameter("gender")).toUpperCase()).getTypeChar()));
        user.setPushNotification(String.valueOf(Notify.valueOf((request.getParameter("pushNotification")).toUpperCase()).getTypeChar()));
        System.out.println("User:" + user);
        try {
            userService.updateUser(user);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    private String toJSONUserRewards(List<UserReward> userRewards) {
        JSONArray userRewardsJSON = new JSONArray();
        for (UserReward userReward : userRewards) {
            JSONObject userRewardJSON = new JSONObject();
            userRewardJSON.put("reward_id", userReward.getRewardId());
            userRewardJSON.put("reward_desc", userReward.getDescription());
            userRewardJSON.put("reward_image", userReward.getImageURL());
            userRewardJSON.put("start_date", userReward.getStartDate());
            userRewardJSON.put("end_date", userReward.getEndDate());
            userRewardJSON.put("creation_date", userReward.getCreationDate());
            userRewardJSON.put("user_name", userReward.getUsername());
            userRewardJSON.put("user_viewed", userReward.getUserViewed());
            userRewardJSON.put("user_availed", userReward.getUserAvailed());
            userRewardJSON.put("avail_date", userReward.getAvailDate());
            userRewardJSON.put("reward_push_date", userReward.getRewardPushDate());
            userRewardsJSON.add(userRewardJSON);
        }
        System.out.println("userRewardsJSON:" + userRewardsJSON.toJSONString());
        return userRewardsJSON.toJSONString();
    }
}