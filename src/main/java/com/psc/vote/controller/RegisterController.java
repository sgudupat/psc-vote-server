package com.psc.vote.controller;

import com.psc.vote.client.domain.Client;
import com.psc.vote.client.service.ClientService;
import com.psc.vote.common.util.ClientUtil;
import com.psc.vote.common.util.UserUtil;
import com.psc.vote.common.util.VoteUtil;
import com.psc.vote.user.domain.Gender;
import com.psc.vote.user.domain.User;
import com.psc.vote.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {

    @Autowired
    UserService userService;

    @Autowired
    ClientService clientService;

    @RequestMapping("/register")
    @ResponseBody
    public String registerUser(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in register user");
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(VoteUtil.encryptPassword(request.getParameter("password")));
        user.setMobile(request.getParameter("mobile"));
        user.setGender(String.valueOf(Gender.valueOf((request.getParameter("gender")).toUpperCase()).getTypeChar()));
        user.setAge(request.getParameter("age"));
        user.setDeviceId(request.getParameter("deviceId"));
        user.setRegistrationId(request.getParameter("registrationId"));
        user.setPushNotification("N"); //While registering Push notification will be False
        System.out.println("User:" + user);
        try {
            userService.registerUser(user);
            return UserUtil.toJSONUser(user);
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/registerClient")
    @ResponseBody
    public String registerClient(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in register client");
        System.out.println("password:" + request.getParameter("password"));
        Client client = new Client();
        client.setClientId(request.getParameter("clientId"));
        client.setPassword(VoteUtil.encryptPassword(request.getParameter("password")));
        client.setEmailAddress(request.getParameter("emailAddress"));
        System.out.println("client:" + client);
        try {
            clientService.registerClient(client);
            return ClientUtil.toJSONClient(client);
        } catch (Exception e) {
            return "fail";
        }
    }
}
