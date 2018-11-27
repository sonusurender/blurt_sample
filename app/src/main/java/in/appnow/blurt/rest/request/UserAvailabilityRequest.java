package in.appnow.blurt.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 14:59, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UserAvailabilityRequest {
    @SerializedName("email")
    private String emailId;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
