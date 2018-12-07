package in.appnow.blurt.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 16:38, 24/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatFeedbackRequest extends BaseRequestModel {
    @SerializedName("chat_id")
    private String chatId;
    @SerializedName("rating")
    private int rating;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
