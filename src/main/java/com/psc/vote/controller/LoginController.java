package com.psc.vote.controller;

import com.psc.vote.client.domain.Client;
import com.psc.vote.client.service.ClientService;
import com.psc.vote.common.util.ClientUtil;
import com.psc.vote.common.util.UserUtil;
import com.psc.vote.common.util.VoteUtil;
import com.psc.vote.user.domain.User;
import com.psc.vote.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    ClientService clientService;

    @RequestMapping("/login")
    @ResponseBody
    public String loginUser(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in login user");
        User user = new User();
        user.setUsername(request.getParameter("username"));
        String password = request.getParameter("password");
        System.out.println("User:" + user);
        try {
            User dbUser = userService.getUser(user);
            if (VoteUtil.checkPassword(password, dbUser.getPassword())) {
                return UserUtil.toJSONUser(dbUser);
            }
            return "fail";
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/loginClient")
    @ResponseBody
    public String loginClient(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in login client");
        Client client = new Client();
        client.setClientId(request.getParameter("clientId"));
        String password = request.getParameter("password");
        System.out.println("Client:" + client);
        try {
            Client dbClient = clientService.getClient(client);
            if (VoteUtil.checkPassword(password, dbClient.getPassword())) {
                return ClientUtil.toJSONClient(dbClient);
            }
            return "fail";
        } catch (Exception e) {
            return "fail";
        }
    }
}