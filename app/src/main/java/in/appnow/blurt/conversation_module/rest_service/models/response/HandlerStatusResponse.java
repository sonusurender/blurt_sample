package in.appnow.blurt.conversation_module.rest_service.models.response;

import com.google.gson.annotations.SerializedName;

import in.appnow.blurt.rest.response.BaseResponseModel;


/**
 * Created by sonu on 11:47, 19/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HandlerStatusResponse extends BaseResponseModel {
    @SerializedName("result")
    private String handlerStatus;

    public String getHandlerStatus() {
        return handlerStatus;
    }
}
