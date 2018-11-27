package in.appnow.blurt.rest.response;

import com.google.gson.annotations.SerializedName;

import in.appnow.blurt.rest.request.BaseRequestModel;

/**
 * Created by sonu on 13:25, 25/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpdateSocketIdRequest extends BaseRequestModel {
    @SerializedName("socket_id")
    private String socketId;

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }
}
