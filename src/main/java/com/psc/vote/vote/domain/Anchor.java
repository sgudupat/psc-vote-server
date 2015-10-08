package com.psc.vote.vote.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Anchor {

    String anchorName;
    BigDecimal anchorPrice;
    Date creationDate;
    String status;

    String clientId;
    String clientName;
    String clientWebsiteAddress;
    String clientInfo;
    List<Campaign> campaigns;

    public String getAnchorName() {
        return anchorName;
    }

    public void setAnchorName(String anchorName) {
        this.anchorName = anchorName;
    }

    public BigDecimal getAnchorPrice() {
        return anchorPrice;
    }

    public void setAnchorPrice(BigDecimal anchorPrice) {
        this.anchorPrice = anchorPrice;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getClientWebsiteAddress() {
        return clientWebsiteAddress;
    }

    public void setClientWebsiteAddress(String clientWebsiteAddress) {
        this.clientWebsiteAddress = clientWebsiteAddress;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public List<Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    @Override
    public String toString() {
        return "Anchor{" +
                "anchorName='" + anchorName + '\'' +
                ", anchorPrice=" + anchorPrice +
                ", creationDate=" + creationDate +
                ", status='" + status + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientWebsiteAddress='" + clientWebsiteAddress + '\'' +
                ", clientInfo='" + clientInfo + '\'' +
                ", campaigns=" + campaigns +
                '}';
    }
}