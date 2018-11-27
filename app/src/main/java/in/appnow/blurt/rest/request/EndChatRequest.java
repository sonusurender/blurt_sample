package in.appnow.blurt.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 11:53, 21/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class EndChatRequest extends BaseRequestModel {
    @SerializedName("chat_session_id")
    private String chatSessionId;

    public void setChatSessionId(String chatSessionId) {
        this.chatSessionId = chatSessionId;
    }
}
