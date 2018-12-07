package in.appnow.blurt.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import in.appnow.blurt.R;
import in.appnow.blurt.app.Blurt;
import in.appnow.blurt.rest.RestUtils;
import in.appnow.blurt.utils.AppUtils;
import in.appnow.blurt.utils.Logger;


/**
 * Created by Sajeev on 06-04-2017.
 */
public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();
    public static String CHAT_UPDATES_CHANNEL = "chat_updates";

    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(String title, String message, Intent intent, int notificationID, int icon, String channedId) {
        showNotificationMessage(title, message, intent, null, notificationID, icon, channedId);
    }

    public void showNotificationMessage(final String title, final String message, Intent intent, String imageUrl, int notificationID, int icon, String channelId) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext, channelId);
        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (!TextUtils.isEmpty(imageUrl)) {
            Logger.DebugLog(TAG, "Image URL Not empty");


            Bitmap bitmap = getBitmapFromURL(RestUtils.getEndPoint() + imageUrl);
            Logger.DebugLog(TAG, "Image URL" + RestUtils.getEndPoint() + imageUrl);

            if (bitmap != null) {
                Logger.DebugLog(TAG, "Bitmap is not null");

                showBigNotification(bitmap, mBuilder, icon, title, message, resultPendingIntent, alarmSound, notificationID);
            } else {
                Logger.DebugLog(TAG, "Bitmap is null");

                showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound, notificationID);
            }

        } else {
            showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound, notificationID);
        }
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound, int notificationID) {

        //Multiline notification style
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(message);

        Notification notification = mBuilder
                .setSmallIcon(icon)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(message)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setWhen(System.currentTimeMillis())
                .setStyle(bigTextStyle)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), AppUtils.getLauncherIcon(mContext)))
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);
    }


    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound, int notificationID) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder
                .setTicker(title)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), AppUtils.getLauncherIcon(mContext)))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            Logger.ErrorLog(TAG, "Bitmap string" + strURL);

            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound

    /**
     * Method checks if the com.firebasenotification.app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotification(Context context, int notificationID) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationID);
    }

    // Clears notification tray messages
    public static void clearSingleNotification(Context context,int notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null)
            notificationManager.cancel(notificationId);
    }

}
