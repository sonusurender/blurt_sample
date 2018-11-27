package in.appnow.blurt.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 15:26, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ValidateUserRequest extends UserAvailabilityRequest {
    @SerializedName("otp")
    private String otp;

    @SerializedName("lc")
    private String licenseKey;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }
}
