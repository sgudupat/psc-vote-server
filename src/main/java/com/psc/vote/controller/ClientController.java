package com.psc.vote.controller;

import com.psc.vote.client.domain.Client;
import com.psc.vote.client.domain.ClientRegret;
import com.psc.vote.client.service.ClientService;
import com.psc.vote.common.util.ClientUtil;
import com.psc.vote.common.util.VoteUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

@Controller
public class ClientController {

    @Autowired
    ClientService clientService;

    @RequestMapping("/getClient")
    @ResponseBody
    public String getClient(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in get client");
        Client client = new Client();
        client.setClientId(request.getParameter("clientId"));
        System.out.println("Client:" + client);
        try {
            Client dbClient = clientService.getClient(client);
            return ClientUtil.toJSONClient(dbClient);
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/updateClientPassword")
    @ResponseBody
    public String updatePassword(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in get client");
        Client client = new Client();
        client.setClientId(request.getParameter("clientId"));
        client.setPassword(VoteUtil.encryptPassword(request.getParameter("password")));
        System.out.println("Client:" + client);
        try {
            clientService.updatePassword(client);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/updateForgotPasswordCode")
    @ResponseBody
    public String updateForgotPasswordCode(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in get client");
        Client client = new Client();
        client.setClientId(request.getParameter("clientId"));
        client.setPasswordCode(request.getParameter("passwordCode"));
        System.out.println("Client:" + client);
        try {
            clientService.updatePasswordCode(client);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/updateClient")
    @ResponseBody
    public String updateClient(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in update client");
        Client client = new Client();
        client.setClientId(request.getParameter("clientId"));
        client.setClientName(request.getParameter("clientName"));
        client.setWebsiteURL(request.getParameter("websiteURL"));
        client.setAbout(request.getParameter("about"));
        client.setOperatingCountry(request.getParameter("country"));
        System.out.println("client:" + client);
        String password = request.getParameter("password");
        try {
            System.out.println("Before update");
            clientService.updateClient(client);
            if (!StringUtils.isEmpty(password)) {
                System.out.println("Before password update");
                client.setPassword(VoteUtil.encryptPassword(password));
                clientService.updatePassword(client);
            }
            System.out.println("Before return");
            return "success";
        } catch (Exception e) {
            System.out.println("Before error" + e.getMessage());
            return "fail";
        }
    }


    @RequestMapping("/postRegret")
    @ResponseBody
    public String postRegret(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in post regret");
        ClientRegret regret = new ClientRegret();
        regret.setDeviceId(request.getParameter("deviceId"));
        regret.setRegretDetail(request.getParameter("regretInfo"));
        System.out.println("Client Regret:" + regret);
        try {
            clientService.insertRegret(regret);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Start image upload::");
        String imagec= request.getParameter("image");
        String fileName = request.getParameter("filename");
        String extn = StringUtils.substringAfter(fileName, ".");
        System.out.println("imagec::" + imagec);
        System.out.println("fileName::" + fileName);
        System.out.println("extn::" + extn);        BufferedImage image = null;
        byte[] imageByte;
        System.out.println("Before Try:");
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imagec);
            System.out.println("After Image Decode:");
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            System.out.println("After Image Built:");
            ImageIO.write(image, extn, new File("/home/ec2-user/tomcat-7.0.63/webapps/images/" + fileName));
            System.out.println("After Image Write:");
            bis.close();
            System.out.println("End:");
            return "success";
        } catch (Exception e) {
            System.out.println("Error::" + e.getMessage());
            return "fail";
        }
    }
}