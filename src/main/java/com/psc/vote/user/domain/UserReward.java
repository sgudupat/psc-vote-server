package com.psc.vote.user.domain;

import com.psc.vote.vote.domain.Reward;

import java.util.Date;

public class UserReward extends Reward {

    String username;
    String rewardId;
    String userViewed;
    String userAvailed;
    Date availDate;
    Date rewardPushDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getRewardId() {
        return rewardId;
    }

    @Override
    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public String getUserViewed() {
        return userViewed;
    }

    public void setUserViewed(String userViewed) {
        this.userViewed = userViewed;
    }

    public String getUserAvailed() {
        return userAvailed;
    }

    public void setUserAvailed(String userAvailed) {
        this.userAvailed = userAvailed;
    }

    public Date getAvailDate() {
        return availDate;
    }

    public void setAvailDate(Date availDate) {
        this.availDate = availDate;
    }

    public Date getRewardPushDate() {
        return rewardPushDate;
    }

    public void setRewardPushDate(Date rewardPushDate) {
        this.rewardPushDate = rewardPushDate;
    }

    @Override
    public String toString() {
        return "UserReward{" +
                "username='" + username + '\'' +
                ", rewardId='" + rewardId + '\'' +
                ", userViewed='" + userViewed + '\'' +
                ", userAvailed='" + userAvailed + '\'' +
                ", availDate=" + availDate +
                ", rewardPushDate=" + rewardPushDate +
                '}';
    }
}