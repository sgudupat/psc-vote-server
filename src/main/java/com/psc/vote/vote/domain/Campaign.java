package com.psc.vote.vote.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Campaign {

    String campaignId;
    String anchorId;
    String question;
    Date startDate;
    Date endDate;
    String imageURL;
    String regionCountry;
    String rewardInfo;
    BigDecimal campaignPrice;
    String status;
    List<CampaignOption> options;
    Vote vote;

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getRegionCountry() {
        return regionCountry;
    }

    public void setRegionCountry(String regionCountry) {
        this.regionCountry = regionCountry;
    }

    public String getRewardInfo() {
        return rewardInfo;
    }

    public void setRewardInfo(String rewardInfo) {
        this.rewardInfo = rewardInfo;
    }

    public BigDecimal getCampaignPrice() {
        return campaignPrice;
    }

    public void setCampaignPrice(BigDecimal campaignPrice) {
        this.campaignPrice = campaignPrice;
    }

    public List<CampaignOption> getOptions() {
        return options;
    }

    public void setOptions(List<CampaignOption> options) {
        this.options = options;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "campaignId='" + campaignId + '\'' +
                ", anchorId='" + anchorId + '\'' +
                ", question='" + question + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", imageURL='" + imageURL + '\'' +
                ", regionCountry='" + regionCountry + '\'' +
                ", rewardInfo='" + rewardInfo + '\'' +
                ", campaignPrice=" + campaignPrice +
                ", status='" + status + '\'' +
                ", options=" + options +
                ", vote=" + vote +
                '}';
    }
}