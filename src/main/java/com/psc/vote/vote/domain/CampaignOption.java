package com.psc.vote.vote.domain;

public class CampaignOption {

    String optionId;
    String optionValue;

    public CampaignOption() {
    }

    public CampaignOption(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    @Override
    public String toString() {
        return "CampaignOption{" +
                "optionId='" + optionId + '\'' +
                ", optionValue='" + optionValue + '\'' +
                '}';
    }
}