package com.psc.vote.controller;

import com.psc.vote.common.util.VoteUtil;
import com.psc.vote.vote.domain.Anchor;
import com.psc.vote.vote.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class AnchorController {

    @Autowired
    VoteService voteService;

    @RequestMapping("/getClientAnchors")
    @ResponseBody
    public String getClientAnchors(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in getClientAnchors");
        String clientId = request.getParameter("clientId");
        System.out.println("Client:" + clientId);
        try {
            List<Anchor> anchors = voteService.getAnchorsByClientId(clientId);
            return VoteUtil.toJSONOnlyAnchorInfo(anchors);
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/checkAnchorAvailability")
    @ResponseBody
    public String checkAnchorAvailability(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in checkAnchorAvailability");
        String anchorId = request.getParameter("anchorId");
        System.out.println("Anchor:" + anchorId);
        try {
            boolean anchorAvailable = voteService.checkAnchorAvailability(anchorId);
            if (anchorAvailable) {
                return "success";
            }
            return "fail";
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/createAnchor")
    @ResponseBody
    public String createAnchor(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in createAnchor");
        String anchorId = request.getParameter("anchorId");
        String clientId = request.getParameter("clientId");
        System.out.println("Anchor:" + anchorId);
        System.out.println("Client:" + clientId);
        Anchor anchor = new Anchor();
        anchor.setAnchorName(anchorId);
        anchor.setClientId(clientId);
        anchor.setStatus("Y");
        try {
            voteService.createAnchor(anchor);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/deleteAnchor")
    @ResponseBody
    public String deleteAnchor(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in deleteAnchor");
        String anchorId = request.getParameter("anchorId");
        System.out.println("Anchor:" + anchorId);
        try {
            voteService.deleteAnchor(anchorId);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }


    @RequestMapping("/recentSearchAnchors")
    @ResponseBody
    public String recentSearchAnchors(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in recentSearchAnchors");
        String userName = request.getParameter("userName");
        System.out.println("userName:" + userName);
        try {
            List<Anchor> anchors = voteService.recentUserViewedAnchors(userName);
            return VoteUtil.toJSONAnchors(anchors);
        } catch (Exception e) {
            return "fail";
        }
    }
}