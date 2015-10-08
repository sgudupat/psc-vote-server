package com.psc.vote.controller;

import com.psc.vote.common.util.VoteUtil;
import com.psc.vote.vote.domain.Anchor;
import com.psc.vote.vote.domain.Campaign;
import com.psc.vote.vote.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    VoteService voteService;

    @RequestMapping("/searchAnchor")
    @ResponseBody
    public String getAnchors(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in getAnchors");
        String anchorName = request.getParameter("anchorName");
        try {
            List<Anchor> anchors = voteService.getAnchorsByName(anchorName);
            return VoteUtil.toJSONAnchors(anchors);
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return "fail";
        }
    }

    @RequestMapping("/getCampaign")
    @ResponseBody
    public String getCampaign(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in getCampaign");
        String campaignId = request.getParameter("campaignId");
        String userName = request.getParameter("userName");
        try {
            Campaign campaign = voteService.getCampaign(campaignId, userName);
            return VoteUtil.toJSONCampaign(campaign);
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return "fail::" + e.getMessage();
        }
    }
}