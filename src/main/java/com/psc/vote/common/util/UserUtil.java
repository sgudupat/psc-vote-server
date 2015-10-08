package com.psc.vote.common.util;

import com.psc.vote.user.domain.User;
import org.json.simple.JSONObject;

public class UserUtil {

    public static String toJSONUser(User user) {
        JSONObject userJSON = new JSONObject();
        userJSON.put("user_name", user.getUsername());
        userJSON.put("mobile", user.getMobile());
        userJSON.put("gender", user.getGender());
        userJSON.put("age", user.getAge());
        userJSON.put("push_notification", user.getPushNotification());
        System.out.println("userJSON:" + userJSON.toJSONString());
        return userJSON.toJSONString();
    }
}