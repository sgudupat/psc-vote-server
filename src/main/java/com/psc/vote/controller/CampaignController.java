package com.psc.vote.controller;

import com.psc.vote.common.util.VoteUtil;
import com.psc.vote.vote.domain.Anchor;
import com.psc.vote.vote.domain.Campaign;
import com.psc.vote.vote.domain.CampaignOption;
import com.psc.vote.vote.domain.Reward;
import com.psc.vote.vote.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CampaignController {

    @Autowired
    VoteService voteService;

    static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws Exception {
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date campaignEndDate = dateFormat.parse("2015-08-10 14:58:00");
        long endDate = campaignEndDate.getTime();
        long sysDate = new Date().getTime();
        System.out.println("End Date:" + endDate);
        System.out.println("Current Date:" + sysDate);
        long diffMilliSeconds = endDate - sysDate;
        System.out.println("diffMilliSeconds:" + diffMilliSeconds);
        System.out.println("Seconds:" + diffMilliSeconds/1000);
        System.out.println("Minutes:" + diffMilliSeconds/(1000*60));
        System.out.println("Hours:" + diffMilliSeconds/(1000*60*60));
        System.out.println("Days:" + diffMilliSeconds/(1000*60*60*24));*/
        //System.out.println("Password:" + ("testpassword").substring(0, 6));
        Campaign c = new Campaign();
        c.setStartDate(sdf2.parse("2015-08-01"));
        c.setEndDate(sdf2.parse("2015-08-30"));

        Campaign campaign = new Campaign();
        campaign.setStartDate(sdf2.parse("2015-07-01"));
        campaign.setEndDate(sdf2.parse("2015-08-15"));
        System.out.println("Existing Campaign:" + c.toString());
        System.out.println("New Campaign:" + campaign.toString());

        System.out.println("Value1:" + campaign.getStartDate().after(c.getStartDate()));
        System.out.println("Value1:" + ((campaign.getStartDate().compareTo(c.getStartDate()) ==0) ||campaign.getStartDate().after(c.getStartDate())));
        System.out.println("Value2:" + campaign.getStartDate().before(c.getEndDate()));
        System.out.println("Value2:" + ((campaign.getStartDate().compareTo(c.getEndDate()) ==0) || campaign.getStartDate().before(c.getEndDate())));
        System.out.println("Value3:" + campaign.getEndDate().after(c.getStartDate()));
        System.out.println("Value4:" + campaign.getEndDate().before(c.getEndDate()));
        boolean validCampaign = !(((campaign.getStartDate().compareTo(c.getStartDate()) ==0) ||campaign.getStartDate().after(c.getStartDate()))
                && ((campaign.getStartDate().compareTo(c.getEndDate()) ==0) || campaign.getStartDate().before(c.getEndDate())));
        if (validCampaign) {
            validCampaign = !(((campaign.getEndDate().compareTo(c.getStartDate()) ==0) || campaign.getEndDate().after(c.getStartDate()))
                    && ((campaign.getEndDate().compareTo(c.getEndDate())==0) || campaign.getEndDate().before(c.getEndDate())));
        }
        System.out.println("validCampaign:" + validCampaign);
    }

    @RequestMapping("/createReward")
    @ResponseBody
    public String createReward(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in createReward");
        Reward reward = new Reward();
        reward.setCampaignId(request.getParameter("campaignId"));
        reward.setDescription(request.getParameter("rewardDescription"));
        reward.setImageURL(request.getParameter("imageURL"));
        reward.setPushRegion(request.getParameter("pushRegion"));
        reward.setPushLimit(request.getParameter("pushLimit"));
        reward.setPushFilter(request.getParameter("pushFilter"));

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            reward.setStartDate(sdf.parse(request.getParameter("startDate")));
            reward.setEndDate(sdf.parse(request.getParameter("endDate")));
            voteService.createReward(reward);
            voteService.pushReward(reward);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/getCampaignFromAnchor")
    @ResponseBody
    public String getCampaignFromAnchor(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in getCampaignFromAnchor");
        String clientId = request.getParameter("clientId");
        String anchorId = request.getParameter("anchorName");
        System.out.println("Client:" + clientId);
        System.out.println("Anchor:" + anchorId);
        Anchor anchor = new Anchor();
        anchor.setAnchorName(anchorId);
        anchor.setClientId(clientId);
        try {
            System.out.println("Anchor:" + anchor);
            List<Campaign> campaigns = voteService.getCampaignsByAnchor(anchor);
            System.out.println("campaigns:" + campaigns);
            return VoteUtil.toJSONCampaigns(campaigns);
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/createCampaign")
    @ResponseBody
    public String createCampaign(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in createCampaign");
        String anchorId = request.getParameter("anchorId");
        String question = request.getParameter("question");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String region = request.getParameter("region");
        String rewardInfo = request.getParameter("rewardInfo");

        String option1 = request.getParameter("optionValue1");
        String option2 = request.getParameter("optionValue2");
        String option3 = request.getParameter("optionValue3");
        String option4 = request.getParameter("optionValue4");
        String option5 = request.getParameter("optionValue5");
        System.out.println("anchorId:" + anchorId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String campaignId = ((anchorId.length() >= 16) ? anchorId.substring(0, 16) : anchorId) + sdf.format(new Date());
        System.out.println("campaignId:" + campaignId);

        try {
            Campaign campaign = new Campaign();
            campaign.setCampaignId(campaignId);
            campaign.setAnchorId(anchorId);
            campaign.setQuestion(question);
            campaign.setStartDate(sdf2.parse(startDate));
            campaign.setEndDate(sdf2.parse(endDate));
            campaign.setRegionCountry(region);
            campaign.setRewardInfo(rewardInfo);
            System.out.println("campaign:" + campaign.toString());
            List<CampaignOption> options = new ArrayList<CampaignOption>();
            if (!StringUtils.isEmpty(option1)) {
                options.add(new CampaignOption(option1));
            }
            if (!StringUtils.isEmpty(option2)) {
                options.add(new CampaignOption(option2));
            }
            if (!StringUtils.isEmpty(option3)) {
                options.add(new CampaignOption(option3));
            }
            if (!StringUtils.isEmpty(option4)) {
                options.add(new CampaignOption(option4));
            }
            if (!StringUtils.isEmpty(option5)) {
                options.add(new CampaignOption(option5));
            }
            campaign.setOptions(options);
            System.out.println("campaign (after options):" + campaign.toString());
            voteService.createCampaign(campaign);
            return "success";
        } catch (Exception e) {
            return "fail:" + e.getMessage();
        }
    }

    @RequestMapping("/updateCampaignStatus")
    @ResponseBody
    public String updateCampaignStatus(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in updateCampaignStatus");
        String campaignId = request.getParameter("campaignId");
        String status = request.getParameter("status");
        System.out.println("campaignId:" + campaignId);
        System.out.println("status:" + status);
        try {
            voteService.updateCampaignStatus(campaignId, status);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/modifyCampaign")
    @ResponseBody
    public String modifyCampaign(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in modifyCampaign");
        try {
            Campaign campaign = new Campaign();
            campaign.setCampaignId(request.getParameter("campaignId"));
            campaign.setStartDate(sdf2.parse(request.getParameter("startDate")));
            campaign.setEndDate(sdf2.parse(request.getParameter("endDate")));
            campaign.setRegionCountry(request.getParameter("region"));
            campaign.setRewardInfo(request.getParameter("rewardInfo"));
            System.out.println("campaign:" + campaign);
            voteService.modifyCampaign(campaign);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }
}