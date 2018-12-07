package in.appnow.blurt.fcm;


/**
 * Created by Surender Kumar on 06-04-2017.
 */
public class Config {

    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String MESSAGE_TYPE = "noti_type";

    // flag to identify whether to show single line
    // or multi line text in push notification tray
    public static boolean appendNotificationMessages = true;

    // type of Push
    public static final String CHAT_MESSAGE_PUSH = "MESSAGE";
    public static final String END_CHAT_PUSH = "END_CHAT";
    public static final String MESSAGE_STATUS_UPDATE = "UPDATE_MESSAGE_STATUS";

    // id to handle the notification in the notification tray
    // id to handle the notification in the notification try
    public static final int CHAT_MESSAGE_NOTIFICATION_ID = 100;
    public static final int END_CHAT_NOTIFICATION_ID = 102;
    public static final int GENERAL_NOTIFICATION = 103;

}
