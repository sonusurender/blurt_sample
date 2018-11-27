package in.appnow.blurt.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 14:48, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class BaseRequestModel {
    @SerializedName("user_id")
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
