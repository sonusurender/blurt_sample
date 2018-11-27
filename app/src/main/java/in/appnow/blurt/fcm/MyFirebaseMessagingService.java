package in.appnow.blurt.fcm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import in.appnow.blurt.R;
import in.appnow.blurt.app.AstroApplication;
import in.appnow.blurt.conversation_module.activity.ConversationActivity;
import in.appnow.blurt.conversation_module.background_service.ConversationIntentService;
import in.appnow.blurt.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.blurt.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.blurt.conversation_module.utils.ConversationUtils;
import in.appnow.blurt.dagger.component.DaggerMyServiceComponent;
import in.appnow.blurt.dao.ABDatabase;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.main.MainActivity;
import in.appnow.blurt.models.PendingFeedbackModel;
import in.appnow.blurt.rest.response.UserProfile;
import in.appnow.blurt.user_auth.UserAuthActivity;
import in.appnow.blurt.utils.Logger;
import in.appnow.blurt.utils.StringUtils;


/**
 * Created by Sajeev on 06-04-2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Inject
    PreferenceManger preferenceManger;
    @Inject
    ABDatabase abDatabase;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        //sendRegistrationToServer(refreshedToken);

    }

    private void storeRegIdInPref(String token) {
        preferenceManger.putString(PreferenceManger.FCM_TOKEN, token);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerMyServiceComponent.builder()
                .appComponent(AstroApplication.get(this).component())
                .build()
                .inject(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.ErrorLog(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Logger.DebugLog(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            //ToastUtils.longToast("Notification:\n"+remoteMessage.getNotification().getBody());

            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Logger.DebugLog(TAG, "Data Payload: " + remoteMessage.getData().toString());
            // ToastUtils.longToast("Data:\n"+remoteMessage.getData().toString());
            try {
                //JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(remoteMessage.getData());
            } catch (Exception e) {
                Logger.ErrorLog(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            //app is in foreground, broadcast the push message


        } else {
            // If app is in background, firebase itself handles the notification
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void handleDataMessage(Map<String, String> dataPayload) {
        try {

            UserProfile user = preferenceManger.getUserDetails();

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
            String sessionId = "";
            Logger.DebugLog(TAG, "title: " + title + "\n" + "message: " + body + "\n" + "msg_type : " + msg_type);

            //create main activity intent
            Intent resultIntent = new Intent(getApplicationContext(), UserAuthActivity.class);

            //put message type as extra in intent to open respective screen
            resultIntent.putExtra(Config.MESSAGE_TYPE, msg_type);

            switch (msg_type) {
                case Config.CHAT_MESSAGE_PUSH:
                    //send notification to user
                    showNotificationMessage(getApplicationContext(), title, body, resultIntent, Config.CHAT_MESSAGE_NOTIFICATION_ID,R.mipmap.ic_launcher, NotificationUtils.CHAT_UPDATES_CHANNEL);

                    break;
                case Config.END_CHAT_PUSH:
                    //send notification to user
                    showNotificationMessage(getApplicationContext(), title, body, resultIntent, Config.END_CHAT_NOTIFICATION_ID,R.mipmap.ic_launcher, NotificationUtils.CHAT_UPDATES_CHANNEL);

                    AsyncTask.execute(() -> {
                        abDatabase.conversationDao().deleteChatTable();
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
                                abDatabase.conversationDao().updateChatMessageStatus(messageStatus, Long.parseLong(messageIdList.get(i)));
                            }
                        });
                    }

                    break;
                case Config.TRANSACTION_REPORT_PUSH:
                    //send notification to user
                    showNotificationMessage(getApplicationContext(), title, body, resultIntent, Config.GENERAL_NOTIFICATION,R.mipmap.ic_launcher, NotificationUtils.CHAT_UPDATES_CHANNEL);

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent, int notificationID, int icon, String channelId) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, notificationID, icon, channelId);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, Intent intent, String imageUrl, int notificationID, int icon, String channelId) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, imageUrl, notificationID, icon, channelId);
    }

    private void updateMessageStatus(UpdateMessageRequest updateMessageRequest) {
        try {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ConversationUtils.MESSAGE_STATUS_MODEL, updateMessageRequest);
            Intent intent = new Intent(this, ConversationIntentService.class);
            intent.setAction(ConversationIntentService.MESSAGE_STATUS);
            intent.putExtras(bundle);
            startService(intent);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

    }
}
