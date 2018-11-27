package in.appnow.blurt.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 16:38, 24/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatFeedbackRequest extends BaseRequestModel {
    @SerializedName("chat_session_id")
    private String chatSessionId;
    @SerializedName("fdbk_rating")
    private int rating;
    @SerializedName("fdbk_comment")
    private String comment;

    public ChatFeedbackRequest(String chatSessionId, int rating, String comment) {
        this.chatSessionId = chatSessionId;
        this.rating = rating;
        this.comment = comment;
    }
}
