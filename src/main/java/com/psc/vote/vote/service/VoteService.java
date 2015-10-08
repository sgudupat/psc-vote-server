package com.psc.vote.vote.service;

import com.psc.vote.vote.domain.Anchor;
import com.psc.vote.vote.domain.Campaign;
import com.psc.vote.vote.domain.Reward;
import com.psc.vote.vote.manager.VoteManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class VoteService {

    @Autowired
    VoteManager voteManager;

    public List<Anchor> getAnchorsByName(String anchorName) {
        return voteManager.getAnchorsByName(anchorName);
    }

    public List<Anchor> recentUserViewedAnchors(String userName) {
        return voteManager.recentUserViewedAnchors(userName);
    }

    public List<Anchor> getAnchorsByClientId(String clientId) throws Exception {
        return voteManager.getAnchorsByClientId(clientId);
    }

    public Campaign getCampaign(String campaignId, String userName) throws Exception {
        return voteManager.getCampaign(campaignId, userName);
    }

    public boolean checkAnchorAvailability(String anchorId) {
        return voteManager.checkAnchorAvailability(anchorId);
    }

    public void submitVote(String username, String optionId) throws Exception {
        voteManager.submitVote(username, optionId);
    }

    public void resubmitVote(String username, String optionId, String oldOptionId) throws Exception {
        voteManager.resubmitVote(username, optionId, oldOptionId);
    }

    public List<Map<String, String>> displayStats(String campaignId) throws Exception {
        return voteManager.displayStats(campaignId);
    }

    public void createAnchor(Anchor anchor) throws Exception {
        voteManager.createAnchor(anchor);
    }

    public void deleteAnchor(String anchorId) throws Exception {
        voteManager.deleteAnchor(anchorId);
    }

    public void createCampaign(Campaign campaign) throws Exception {
        System.out.println("campaign:" + campaign);
        //Validate Campaign (Only one campaign must exist for an anchor in a date range)
        boolean validCampaign = voteManager.validateCampaign(campaign);
        if (!validCampaign) {
            throw new Exception("Campaign not in valid date range");
        }
        voteManager.createCampaign(campaign);
    }

    public void modifyCampaign(Campaign campaign) throws Exception {
        System.out.println("campaign:" + campaign);
        voteManager.modifyCampaign(campaign);
    }

    public void updateCampaignStatus(String campaignId, String status) throws Exception {
        voteManager.updateCampaignStatus(campaignId, status);
    }

    public void createReward(Reward reward) throws Exception {
        voteManager.createReward(reward);
    }

    public void pushReward(Reward reward) throws Exception {
        System.out.println("Reward for Push:" + reward.toString());
        voteManager.pushReward(reward);
    }

    public List<Campaign> getCampaignsByAnchor(Anchor anchor) throws Exception {
        System.out.println("Anchor:" + anchor);
        return voteManager.getCampaignsByAnchor(anchor);
    }
}
