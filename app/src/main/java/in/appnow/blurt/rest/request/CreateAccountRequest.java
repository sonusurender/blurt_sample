package in.appnow.blurt.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 15:26, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class CreateAccountRequest extends UserAvailabilityRequest {

    @SerializedName("lc")
    private String licenseKey;
    @SerializedName("av")
    private String appVersion;
    @SerializedName("dt")
    private int deviceType;
    @SerializedName("locale")
    private String locale;
    @SerializedName("name")
    private String name;

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
