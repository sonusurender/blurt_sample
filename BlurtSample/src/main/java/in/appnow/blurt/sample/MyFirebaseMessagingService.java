package in.appnow.blurt.sample;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.appnow.blurt.fcm.BlurtPushReceiver;
import in.appnow.blurt.utils.Logger;


/**
 * Created by Sonu Kumar on 06-04-2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        new BlurtPushReceiver().updatePushNotificationToken(getApplicationContext(), refreshedToken);
        // sending reg id to your server
        //sendRegistrationToServer(refreshedToken);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.ErrorLog(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Logger.DebugLog(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            //ToastUtils.longToast("Notification:\n"+remoteMessage.getNotification().getBody());

        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Logger.DebugLog(TAG, "Data Payload: " + remoteMessage.getData().toString());
            // ToastUtils.longToast("Data:\n"+remoteMessage.getData().toString());
            try {
                //JSONObject json = new JSONObject(remoteMessage.getData().toString());
                if (BlurtPushReceiver.isBlurtPushNotification(remoteMessage.getData())) {
                    BlurtPushReceiver.handleDataMessage(getApplicationContext(), remoteMessage.getData());
                    return;
                }
            } catch (Exception e) {
                Logger.ErrorLog(TAG, "Exception: " + e.getMessage());
            }
        }
    }
}
