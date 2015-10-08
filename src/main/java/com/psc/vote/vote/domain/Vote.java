package com.psc.vote.vote.domain;

public class Vote {

    String userName;
    String optionValueId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOptionValueId() {
        return optionValueId;
    }

    public void setOptionValueId(String optionValueId) {
        this.optionValueId = optionValueId;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "userName='" + userName + '\'' +
                ", optionValueId='" + optionValueId + '\'' +
                '}';
    }
}
