package com.psc.vote.user.domain;

public class User {

    String username;
    String mobile;
    String password;
    String gender;
    String age;
    String deviceId;
    String pushNotification;
    String registrationId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(String pushNotifications) {
        this.pushNotification = pushNotifications;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", pushNotification='" + pushNotification + '\'' +
                ", registrationId='" + registrationId + '\'' +
                '}';
    }
}