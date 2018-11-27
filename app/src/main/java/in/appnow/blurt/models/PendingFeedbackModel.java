package in.appnow.blurt.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 20:15, 27/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PendingFeedbackModel {
    @SerializedName("fdbk_pending")
    private boolean isFeedbackPending;
    @SerializedName("chat_session_id")
    private String sessionId;
    @SerializedName("timestamp")
    private long startTimeStamp;
    @SerializedName("end_timestamp")
    private long endTimeStamp;

    public PendingFeedbackModel(boolean isFeedbackPending, String sessionId, long startTimeStamp, long endTimeStamp) {
        this.isFeedbackPending = isFeedbackPending;
        this.sessionId = sessionId;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
    }

    public boolean isFeedbackPending() {
        return isFeedbackPending;
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }
}
