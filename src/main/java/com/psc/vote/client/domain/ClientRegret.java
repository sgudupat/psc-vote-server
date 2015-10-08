package com.psc.vote.client.domain;

public class ClientRegret {

    private String deviceId;
    private String regretDetail;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRegretDetail() {
        return regretDetail;
    }

    public void setRegretDetail(String regretDetail) {
        this.regretDetail = regretDetail;
    }

    @Override
    public String toString() {
        return "ClientRegret{" +
                "deviceId='" + deviceId + '\'' +
                ", regretDetail='" + regretDetail + '\'' +
                '}';
    }
}
