package in.appnow.blurt.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 12:10, 21/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class StartChatResponse extends BaseResponseModel {

    @SerializedName("response")
    private ChatResponse chatResponse;

    public StartChatResponse() {
    }

    public ChatResponse getChatResponse() {
        return chatResponse;
    }

    public void setChatResponse(ChatResponse chatResponse) {
        this.chatResponse = chatResponse;
    }

    public  class ChatResponse {

        @SerializedName("chat_session_id")
        private String chatSessionId;
        @SerializedName("existing_chat")
        private boolean existingChat;
        @SerializedName("chat_status")
        private String chatStatus;

        public ChatResponse() {
        }

        public String getChatSessionId() {
            return chatSessionId;
        }

        public void setChatSessionId(String chatSessionId) {
            this.chatSessionId = chatSessionId;
        }

        public boolean isExistingChat() {
            return existingChat;
        }

        public void setExistingChat(boolean existingChat) {
            this.existingChat = existingChat;
        }

        public String getChatStatus() {
            return chatStatus;
        }

        public void setChatStatus(String chatStatus) {
            this.chatStatus = chatStatus;
        }
    }


}
