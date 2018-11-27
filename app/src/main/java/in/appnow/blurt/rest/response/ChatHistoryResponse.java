package in.appnow.blurt.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonu on 13:18, 31/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatHistoryResponse extends BaseResponseModel {
    @SerializedName("conversation_count")
    private int conversationCount;
    @SerializedName("conversations")
    private List<Conversations> conversationsList;

    public int getConversationCount() {
        return conversationCount;
    }

    public List<Conversations> getConversationsList() {
        return conversationsList;
    }

    public class Conversations implements Parcelable {
        @SerializedName("id")
        private String conversationId;
        @SerializedName("session_id")
        private String sessionId;
        @SerializedName("start_timestamp")
        private long startTimestamp;
        @SerializedName("end_timestamp")
        private long endTimestamp;
        @SerializedName("topics")
        private int topicsCount;
        @SerializedName("fdbk_rating")
        private float feedbackRating;
        @SerializedName("fdbk_comment")
        private String feedbackComment;
        @SerializedName("message_count")
        private int messageCount;
        @SerializedName("messages")
        private List<Messages> messagesList;

        protected Conversations(Parcel in) {
            conversationId = in.readString();
            sessionId = in.readString();
            startTimestamp = in.readLong();
            endTimestamp = in.readLong();
            feedbackRating = in.readFloat();
            feedbackComment = in.readString();
            messageCount = in.readInt();
            topicsCount = in.readInt();
            messagesList = in.createTypedArrayList(Messages.CREATOR);
        }

        public final Creator<Conversations> CREATOR = new Creator<Conversations>() {
            @Override
            public Conversations createFromParcel(Parcel in) {
                return new Conversations(in);
            }

            @Override
            public Conversations[] newArray(int size) {
                return new Conversations[size];
            }
        };

        public String getConversationId() {
            return conversationId;
        }

        public String getSessionId() {
            return sessionId;
        }

        public long getEndTimestamp() {
            return endTimestamp;
        }

        public long getStartTimestamp() {
            return startTimestamp;
        }

        public int getTopicsCount() {
            return topicsCount;
        }

        public float getFeedbackRating() {
            return feedbackRating;
        }

        public String getFeedbackComment() {
            return feedbackComment;
        }

        public int getMessageCount() {
            return messageCount;
        }

        public List<Messages> getMessagesList() {
            return messagesList;
        }

        @Override
        public String toString() {
            return "Conversations{" +
                    "conversationId='" + conversationId + '\'' +
                    ", sessionId='" + sessionId + '\'' +
                    ", startTimestamp=" + startTimestamp +
                    ", endTimestamp=" + endTimestamp +
                    ", topicsCount=" + topicsCount +
                    ", feedbackRating=" + feedbackRating +
                    ", feedbackComment='" + feedbackComment + '\'' +
                    ", messageCount=" + messageCount +
                    ", messagesList=" + messagesList +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(conversationId);
            parcel.writeString(sessionId);
            parcel.writeLong(startTimestamp);
            parcel.writeLong(endTimestamp);
            parcel.writeFloat(feedbackRating);
            parcel.writeString(feedbackComment);
            parcel.writeInt(messageCount);
            parcel.writeTypedList(messagesList);
            parcel.writeInt(topicsCount);
        }
    }

    @Override
    public String toString() {
        return "ChatHistoryResponse{" +
                "conversationCount=" + conversationCount +
                ", conversationsList=" + conversationsList +
                '}';
    }
}
