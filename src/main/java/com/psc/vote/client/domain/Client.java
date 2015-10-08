package com.psc.vote.client.domain;

import java.util.Date;

public class Client {
    private String clientId;
    private String clientName;
    private String password;
    private String emailAddress;
    private Date creationDate;
    private String websiteURL;
    private String about;
    private String operatingCountry;
    private String passwordCode;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getOperatingCountry() {
        return operatingCountry;
    }

    public void setOperatingCountry(String operatingCountry) {
        this.operatingCountry = operatingCountry;
    }

    public String getPasswordCode() {
        return passwordCode;
    }

    public void setPasswordCode(String passwordCode) {
        this.passwordCode = passwordCode;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", password='" + password + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", creationDate=" + creationDate +
                ", websiteURL='" + websiteURL + '\'' +
                ", about='" + about + '\'' +
                ", operatingCountry='" + operatingCountry + '\'' +
                ", passwordCode='" + passwordCode + '\'' +
                '}';
    }
}