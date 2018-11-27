package in.appnow.blurt.fcm;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Sajeev on 06-04-2017.
 */
public class Config {

    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String IMAGE_FILE = "img_file";
    public static final String MESSAGE_TYPE = "noti_type";
    public static final String EVENT_ID = "event_id";
    public static final String USER_ID = "user_id";
    public static final String MESSAGE_DATA = "message_data";


    // flag to identify whether to show single line
    // or multi line text in push notification tray
    public static boolean appendNotificationMessages = true;

    // type of Push
    public static final String PICK_REQUEST_PUSH = "PICK_REQUEST";
    public static final String CHAT_MESSAGE_PUSH = "MESSAGE";
    public static final String END_CHAT_PUSH = "END_CHAT";
    public static final String SEND_MESSAGE_PUSH = "send_message_push";
    public static final String MESSAGE_STATUS_UPDATE = "UPDATE_MESSAGE_STATUS";
    public static final String HANDLER_STATUS_PUSH = "HANDLER_AVAIL_STATUS";
    public static final String HANDLER_TYPING_STATUS_PUSH = "HANDLER_TYPING_STATUS";
    public static final String TRANSACTION_REPORT_PUSH = "TRANSACTION_REPORT";


    // id to handle the notification in the notification tray
// id to handle the notification in the notification try
    public static final int CHAT_MESSAGE_NOTIFICATION_ID = 100;
    public static final int PICK_REQUEST_NOTIFICATION_ID = 101;
    public static final int END_CHAT_NOTIFICATION_ID = 102;
    public static final int GENERAL_NOTIFICATION = 103;

    public static final int MESSAGE_NOTIFICATION_ID_BIG_IMAGE = 103;


    public static void doSubscriptionTopic(String topicName, boolean subscribe) {
        if (subscribe)
            FirebaseMessaging.getInstance().subscribeToTopic(topicName);
        else
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName);

    }


}
