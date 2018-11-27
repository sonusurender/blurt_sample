package in.appnow.blurt.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 00:38, 27/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class FCMRequestModel {

    @SerializedName("uuid")
    private String userId;
    @SerializedName("fcm_token")
    private String fcmToken;
    @SerializedName("dv")
    private int deviceType;
    @SerializedName("role")
    private String role;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
