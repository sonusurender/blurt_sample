package in.appnow.blurt.conversation_module.rest_service.models.request;

import com.google.gson.annotations.SerializedName;

import in.appnow.blurt.rest.request.BaseRequestModel;


/**
 * Created by sonu on 11:08, 19/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UserStatusRequest extends BaseRequestModel {
    @SerializedName("status")
    private int status;


    public void setStatus(int status) {
        this.status = status;
    }
}
