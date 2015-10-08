package com.psc.vote.vote.manager;

import com.psc.vote.user.dao.UserDao;
import com.psc.vote.user.dao.UserRewardsDao;
import com.psc.vote.user.domain.User;
import com.psc.vote.vote.dao.*;
import com.psc.vote.vote.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class VoteManager {

    @Autowired
    AnchorDao anchorDao;

    @Autowired
    CampaignDao campaignDao;

    @Autowired
    CampaignOptionsDao campaignOptionsDao;

    @Autowired
    VotesDao votesDao;

    @Autowired
    RewardDao rewardDao;

    @Autowired
    UserRewardsDao userRewardsDao;

    @Autowired
    UserDao userDao;

    public List<Anchor> getAnchorsByName(String anchorName) {
        List<Anchor> anchors = anchorDao.getAnchorsByName(anchorName);
        System.out.println("Anchors:" + anchors.toString());
        for (Anchor anchor : anchors) {
            List<Campaign> campaigns = campaignDao.getCampaignsByAnchor(anchor.getAnchorName());
            anchor.setCampaigns(campaigns);
        }
        System.out.println("Anchors:" + anchors.toString());
        return anchors;
    }

    public List<Anchor> recentUserViewedAnchors(String userName) {
        List<Anchor> anchors = votesDao.recentUserViewedAnchors(userName);
        System.out.println("Anchors:" + anchors.toString());
        for (Anchor anchor : anchors) {
            List<Campaign> campaigns = campaignDao.getCampaignsByAnchor(anchor.getAnchorName());
            anchor.setCampaigns(campaigns);
        }
        System.out.println("Anchors:" + anchors.toString());
        return anchors;
    }

    public Campaign getCampaign(String campaignId, String userName) throws Exception {
        Campaign campaign = campaignDao.getCampaign(campaignId);
        System.out.println("Campaign:" + campaign.toString());
        List<CampaignOption> options = campaignOptionsDao.getOptionsByCampaignId(campaignId);
        campaign.setOptions(options);
        Vote vote = votesDao.getVote(userName, campaignId);
        campaign.setVote(vote);
        System.out.println("Campaign:" + campaign.toString());
        return campaign;
    }

    public void submitVote(String username, String optionId) throws Exception {
        System.out.println("submitVote");
        votesDao.submitVote(username, optionId);
        System.out.println("after submitVote");
    }

    public void resubmitVote(String username, String optionId, String oldOptionId) throws Exception {
        System.out.println("resubmitVote");
        votesDao.resubmitVote(username, optionId, oldOptionId);
        System.out.println("after resubmitVote");
    }

    public List<Map<String, String>> displayStats(String campaignId) throws Exception {
        System.out.println("displayStats");
        List<Map<String, String>> records = votesDao.displayStats(campaignId);
        System.out.println("displayStats:" + records.toString());
        return records;
    }

    public List<Anchor> getAnchorsByClientId(String clientId) throws Exception {
        System.out.println("getAnchorsByClientId");
        List<Anchor> anchors = anchorDao.getAnchorsByClientId(clientId);
        System.out.println("getAnchorsByClientId:" + anchors);
        return anchors;
    }

    public boolean checkAnchorAvailability(String anchorId) {
        Anchor anchor = anchorDao.checkAnchorAvailability(anchorId);
        if (anchor == null) {
            return true;
        }
        return false;
    }

    public void createAnchor(Anchor anchor) throws Exception {
        anchorDao.createAnchor(anchor);
    }

    public void deleteAnchor(String anchorId) throws Exception {
        anchorDao.deleteAnchor(anchorId);
    }

    public boolean validateCampaign(Campaign campaign) throws Exception {
        System.out.println("campaign:" + campaign);
        boolean validCampaign = true;
        //Validate Dates of Campaign
        if (campaign.getEndDate().before(campaign.getStartDate())) {
            return false;
        }
        //Validate Dates with existing campaigns
        List<Campaign> campaigns = campaignDao.getCampaignsByAnchor(campaign.getAnchorId());
        for (Campaign c : campaigns) {
            if (c.getEndDate().after(new Date()) && StringUtils.isEmpty(c.getStatus()) && validCampaign) {
                System.out.println("Active Campaign:" + c.toString());
                validCampaign = !(((campaign.getStartDate().compareTo(c.getStartDate()) == 0) || campaign.getStartDate().after(c.getStartDate()))
                        && ((campaign.getStartDate().compareTo(c.getEndDate()) == 0) || campaign.getStartDate().before(c.getEndDate())));
                if (validCampaign) {
                    validCampaign = !(((campaign.getEndDate().compareTo(c.getStartDate()) == 0) || campaign.getEndDate().after(c.getStartDate()))
                            && ((campaign.getEndDate().compareTo(c.getEndDate()) == 0) || campaign.getEndDate().before(c.getEndDate())));
                }
                System.out.println("validCampaign:" + validCampaign);
            }
        }
        System.out.println("validCampaign:" + validCampaign);
        System.out.println("after create campaign:");
        return validCampaign;
    }

    public void createCampaign(Campaign campaign) throws Exception {
        System.out.println("campaign:" + campaign);
        campaignDao.createCampaign(campaign);
        for (CampaignOption option : campaign.getOptions()) {
            System.out.println("campaign option:" + option);
            campaignOptionsDao.createCampaignOption(campaign.getCampaignId(), option.getOptionValue());
        }
        System.out.println("after create campaign:");
    }

    public void modifyCampaign(Campaign campaign) throws Exception {
        System.out.println("campaign:" + campaign);
        campaignDao.modifyCampaign(campaign);
        System.out.println("after modify campaign:");
    }

    public void updateCampaignStatus(String campaignId, String status) throws Exception {
        campaignDao.updateCampaignStatus(campaignId, status);
    }

    public void createReward(Reward reward) throws Exception {
        rewardDao.createReward(reward);
    }

    public void pushReward(Reward reward) throws Exception {
        boolean onlyRespondents = !("ALL".equals(reward.getPushFilter()));
        List<User> users = userDao.getUsersForRewardPush(reward.getPushRegion(), onlyRespondents, reward.getPushLimit());
        System.out.println("users:" + users);
        for (User user : users) {
            System.out.println("user:" + user);
            userRewardsDao.pushUserReward(user.getUsername(), reward.getRewardId());
            System.out.println("After DB and before Push:");
            pushNotification(user.getRegistrationId(), reward.getDescription());
            System.out.println("After Push");
        }
    }

    public List<Campaign> getCampaignsByAnchor(Anchor anchor) throws Exception {
        System.out.println("Anchor:" + anchor);
        return campaignDao.getCampaignsByAnchor(anchor.getAnchorName());
    }

    private void pushNotification(String registrationId, String rewardMessage) {
        // Put your Google API Server Key here
        String GOOGLE_SERVER_KEY = "AIzaSyBIffNkTXeDtHMOaJ55GyTEvRRkUL3uR24";
        String MESSAGE_KEY = "message";
        Sender sender = new Sender(GOOGLE_SERVER_KEY);
        Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData(MESSAGE_KEY, rewardMessage).build();
        try {
            Result result = sender.send(message, registrationId, 1);
            System.out.println("pushNotification:" + result.toString());
        } catch (IOException e) {
            System.out.println("pushNotification IO Exception:" + e.getMessage());
        } catch (Exception e) {
            System.out.println("pushNotification Exception:" + e.getMessage());
        }
    }
}