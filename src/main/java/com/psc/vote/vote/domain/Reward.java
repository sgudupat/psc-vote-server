package com.psc.vote.vote.domain;

import java.util.Date;

public class Reward {

    String rewardId;
    String campaignId;
    String description;
    String imageURL;
    Date startDate;
    Date endDate;
    Date creationDate;
    String pushLimit;
    String pushRegion;
    String pushFilter;

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getPushLimit() {
        return pushLimit;
    }

    public void setPushLimit(String pushLimit) {
        this.pushLimit = pushLimit;
    }

    public String getPushRegion() {
        return pushRegion;
    }

    public void setPushRegion(String pushRegion) {
        this.pushRegion = pushRegion;
    }

    public String getPushFilter() {
        return pushFilter;
    }

    public void setPushFilter(String pushFilter) {
        this.pushFilter = pushFilter;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "rewardId='" + rewardId + '\'' +
                ", campaignId='" + campaignId + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", creationDate=" + creationDate +
                ", pushLimit='" + pushLimit + '\'' +
                ", pushRegion='" + pushRegion + '\'' +
                ", pushFilter='" + pushFilter + '\'' +
                '}';
    }
}