package in.appnow.blurt.fcm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.appnow.blurt.R;
import in.appnow.blurt.app.Blurt;
import in.appnow.blurt.conversation_module.background_service.ConversationIntentService;
import in.appnow.blurt.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.blurt.conversation_module.utils.ConversationUtils;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.rest.response.UserProfile;
import in.appnow.blurt.user_auth.UserAuthActivity;
import in.appnow.blurt.utils.AppUtils;
import in.appnow.blurt.utils.Logger;
import in.appnow.blurt.utils.StringUtils;


/**
 * Created by Surender Kumar on 06-04-2017.
 */
public class BlurtPushReceiver {

    private static final String TAG = BlurtPushReceiver.class.getSimpleName();
    private static final String NOTIFICATION_SMALL_ICON_METADATA = "com.blurt.small_notification_icon";
    private static final String BLURT_PREFIX = "blurtping";

    public BlurtPushReceiver() {
    }

    public void updatePushNotificationToken(Context context, String token) {
        Blurt.getInstance(context).getPreferenceManager().putString(PreferenceManger.FCM_TOKEN, token);
    }

    public static boolean isBlurtPushNotification(Map<String, String> data) {
        //This is to identify collapse key sent in notification..
        if (data == null || data.isEmpty()) {
            return false;
        }
        String payLoad = data.toString();
        Logger.DebugLog(TAG, "Received Notification");
        return payLoad != null && payLoad.contains(BLURT_PREFIX);
        //return true;
    }

    @SuppressLint("StaticFieldLeak")
    public static void handleDataMessage(Context context, Map<String, String> dataPayload) {
        try {

            UserProfile user = Blurt.getInstance(context).getPreferenceManager().getUserDetails();

            if (user == null)
                return;
            //get user id
            String loggedInUserId = user.getUserId();

            //return if no user is logged in
            if (TextUtils.isEmpty(loggedInUserId))
                return;

            Logger.DebugLog(TAG, "logged in user id : " + loggedInUserId);
            Iterator myVeryOwnIterator = dataPayload.keySet().iterator();
            JSONObject jsonObject = new JSONObject();
            while (myVeryOwnIterator.hasNext()) {
                String key = (String) myVeryOwnIterator.next();
                String value = dataPayload.get(key);
                Logger.DebugLog(TAG, "Data : " + key + " : " + value + "\n");
                jsonObject.put(key, value);
            }

            String title = jsonObject.getString(Config.TITLE);
            String body = jsonObject.getString(Config.BODY);
            String msg_type = jsonObject.getString(Config.MESSAGE_TYPE);
            Logger.DebugLog(TAG, "title: " + title + "\n" + "message: " + body + "\n" + "msg_type : " + msg_type);

            //create main activity intent
            Intent resultIntent = new Intent(context, UserAuthActivity.class);

            //put message type as extra in intent to open respective screen
            resultIntent.putExtra(Config.MESSAGE_TYPE, msg_type);

            switch (msg_type) {
                case Config.CHAT_MESSAGE_PUSH:
                    //send notification to user
                    showNotificationMessage(context, title, body, resultIntent, Config.CHAT_MESSAGE_NOTIFICATION_ID, AppUtils.getMetaDataValueForResources(context, NOTIFICATION_SMALL_ICON_METADATA), NotificationUtils.CHAT_UPDATES_CHANNEL);

                    break;
                case Config.END_CHAT_PUSH:
                    //send notification to user
                    showNotificationMessage(context, title, body, resultIntent, Config.END_CHAT_NOTIFICATION_ID, AppUtils.getMetaDataValueForResources(context, ""), NotificationUtils.CHAT_UPDATES_CHANNEL);

                    AsyncTask.execute(() -> {
                        Blurt.getInstance(context).getDatabase().conversationDao().deleteChatTable();
                    });
                    break;

                case Config.MESSAGE_STATUS_UPDATE:
                    String messageIds = jsonObject.getString("message_ids");
                    String messageStatus = jsonObject.getString("status");
                    if (!TextUtils.isEmpty(messageIds)) {
                        messageIds = messageIds.replace("[", "").replace("]", "");
                        Logger.DebugLog(TAG, "Messg ids: " + messageIds);
                        List<String> messageIdList = StringUtils.convertStringToStringArrayList(messageIds);
                        Logger.DebugLog(TAG, "Mesge array sisxe:  " + messageIdList.size());
                        AsyncTask.execute(() -> {
                            for (int i = 0; i < messageIdList.size(); i++) {
                                Blurt.getInstance(context).getDatabase().conversationDao().updateChatMessageStatus(messageStatus, Long.parseLong(messageIdList.get(i)));
                            }
                        });
                    }

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Showing notification with text only
     */
    private static void showNotificationMessage(Context context, String title, String message, Intent intent, int notificationID, int icon, String channelId) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, notificationID, icon, channelId);
    }

    /**
     * Showing notification with text and image
     */
    private static void showNotificationMessageWithBigImage(Context context, String title, String message, Intent intent, String imageUrl, int notificationID, int icon, String channelId) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, imageUrl, notificationID, icon, channelId);
    }

    private void updateMessageStatus(Context context, UpdateMessageRequest updateMessageRequest) {
        try {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ConversationUtils.MESSAGE_STATUS_MODEL, updateMessageRequest);
            Intent intent = new Intent(context, ConversationIntentService.class);
            intent.setAction(ConversationIntentService.MESSAGE_STATUS);
            intent.putExtras(bundle);
            context.startService(intent);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

    }
}
