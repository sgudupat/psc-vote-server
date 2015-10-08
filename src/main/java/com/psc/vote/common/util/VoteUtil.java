package com.psc.vote.common.util;

import com.psc.vote.vote.domain.Anchor;
import com.psc.vote.vote.domain.Campaign;
import com.psc.vote.vote.domain.CampaignOption;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class VoteUtil {

    public static String encryptPassword(String password) {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        return passwordEncryptor.encryptPassword(password);
    }

    public static boolean checkPassword(String password, String encryptedPassword) {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        return passwordEncryptor.checkPassword(password, encryptedPassword);
    }

    public static String toJSONOnlyAnchorInfo(List<Anchor> anchors) {
        JSONArray anchorsJSON = new JSONArray();
        for (Anchor anchor : anchors) {
            JSONObject anchorJSON = new JSONObject();
            anchorJSON.put("anchor_id", anchor.getAnchorName());
            anchorJSON.put("client_id", anchor.getClientId());
            anchorJSON.put("creation_date", anchor.getCreationDate());
            anchorJSON.put("price", anchor.getAnchorPrice());
            anchorJSON.put("active", anchor.getStatus());
            anchorsJSON.add(anchorJSON);
        }
        System.out.println("anchorsJSON:" + anchorsJSON.toJSONString());
        return anchorsJSON.toJSONString();
    }

    public static String toJSONAnchors(List<Anchor> anchors) {
        JSONArray anchorsJSON = new JSONArray();
        for (Anchor anchor : anchors) {
            JSONObject anchorJSON = new JSONObject();
            anchorJSON.put("anchor_name", anchor.getAnchorName());
            anchorJSON.put("client_id", anchor.getClientId());
            anchorJSON.put("client_name", anchor.getClientName());
            anchorJSON.put("website_url", anchor.getClientWebsiteAddress());
            anchorJSON.put("client_info", anchor.getClientInfo());
            anchorJSON.put("creation_date", anchor.getCreationDate());
            anchorJSON.put("anchor_price", anchor.getAnchorPrice());
            anchorJSON.put("active", anchor.getStatus());
            JSONArray campaignsJSON = new JSONArray();
            for (Campaign campaign : anchor.getCampaigns()) {
                JSONObject campaignJSON = new JSONObject();
                campaignJSON.put("campaign_id", campaign.getCampaignId());
                campaignJSON.put("question", campaign.getQuestion());
                campaignJSON.put("start_date", campaign.getStartDate());
                campaignJSON.put("end_date", campaign.getEndDate());
                campaignJSON.put("status", campaign.getStatus());
                campaignsJSON.add(campaignJSON);
            }
            anchorJSON.put("campaigns", campaignsJSON.toJSONString());
            anchorsJSON.add(anchorJSON);
        }
        System.out.println("anchorsJSON:" + anchorsJSON.toJSONString());
        return anchorsJSON.toJSONString();
    }

    public static String toJSONCampaign(Campaign campaign) {
        JSONObject campaignJSON = new JSONObject();
        campaignJSON.put("campaign_id", campaign.getCampaignId());
        campaignJSON.put("question", campaign.getQuestion());
        campaignJSON.put("start_date", campaign.getStartDate());
        campaignJSON.put("end_date", campaign.getEndDate());
        campaignJSON.put("status", campaign.getStatus());
        campaignJSON.put("rewardInfo", campaign.getRewardInfo());
        campaignJSON.put("regionCountry", campaign.getRegionCountry());

        JSONArray optionsJSON = new JSONArray();
        for (CampaignOption option : campaign.getOptions()) {
            JSONObject optionJSON = new JSONObject();
            optionJSON.put("option_id", option.getOptionId());
            optionJSON.put("option_value", option.getOptionValue());
            optionsJSON.add(optionJSON);
        }
        campaignJSON.put("options", optionsJSON.toJSONString());

        if (campaign.getVote() != null) {
            campaignJSON.put("userVoted", "Y");
            campaignJSON.put("optionValue", campaign.getVote().getOptionValueId());
        } else {
            campaignJSON.put("userVoted", "N");
        }
        System.out.println("campaignJSON:" + campaignJSON.toJSONString());
        return campaignJSON.toJSONString();
    }

    public static String toJSONCampaigns(List<Campaign> campaigns) {
        JSONArray campaignsJSON = new JSONArray();
        for (Campaign campaign : campaigns) {
            System.out.println("campaign:" + campaign.toString());
            JSONObject campaignJSON = new JSONObject();
            campaignJSON.put("campaign_id", campaign.getCampaignId());
            campaignJSON.put("question", campaign.getQuestion());
            campaignJSON.put("start_date", campaign.getStartDate());
            campaignJSON.put("end_date", campaign.getEndDate());
            campaignJSON.put("status", campaign.getStatus());
            campaignsJSON.add(campaignJSON);
        }
        System.out.println("campaignsJSON:" + campaignsJSON.toJSONString());
        return campaignsJSON.toJSONString();
    }
}