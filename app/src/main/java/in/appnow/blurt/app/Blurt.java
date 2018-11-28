package in.appnow.blurt.app;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.text.TextUtils;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import in.appnow.blurt.R;
import in.appnow.blurt.dagger.component.AppComponent;
import in.appnow.blurt.dagger.component.DaggerAppComponent;
import in.appnow.blurt.dagger.module.AppModule;
import in.appnow.blurt.dagger.module.RoomModule;
import in.appnow.blurt.dagger.module.SharedPreferencesModule;
import in.appnow.blurt.dao.ABDatabase;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.network.CheckInternetConnection;
import in.appnow.blurt.rest.RestUtils;
import in.appnow.blurt.utils.ToastUtils;

/**
 * Created by Surender Kumar on 28/03/18.
 * Copyright © 2018 Surender Kumar. All rights reserved.
 */


public class Blurt {

    private static Blurt instance;
    private static AppComponent appComponent;
    private Context context;
    private static boolean isDebug = false;

    private Blurt(Context context) {
        this.context = context;
    }

    private static void initAppComponent(Context context) {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(context))
                    .roomModule(new RoomModule(context))
                    .sharedPreferencesModule(new SharedPreferencesModule(context))
                    .build();
        }
    }

    public static Blurt init(Context context) {
        instance = getInstance(context);
        // This line needs to be executed before any usage of EmojiTextView, EmojiEditText or EmojiButton.
        EmojiManager.install(new GoogleEmojiProvider());
        initAppComponent(context);
        RestUtils.getLicenseKey(context);
        return instance;
    }

    public static Blurt getInstance(Context context) {
        if (instance == null) {
            instance = new Blurt(context.getApplicationContext());
        }
        return instance;
    }

    public AppComponent component() {
        return appComponent;
    }


    public boolean isInternetConnected(boolean showToast) {
        if (new CheckInternetConnection(context).isConnected())
            return true;
        else {
            if (showToast)
                ToastUtils.longToast(context, R.string.no_internet_connection);
            return false;
        }
    }

    public PreferenceManger getPreferenceManager() {
        return new PreferenceManger(context.getSharedPreferences(PreferenceManger.PREF_KEY, Context.MODE_PRIVATE));
    }

    public ABDatabase getDatabase() {
        return Room.databaseBuilder(context,
                ABDatabase.class,
                "blurt.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static void enableDebug(boolean isDebug) {
        Blurt.isDebug = isDebug;
    }

    public static boolean getDebug() {
        return isDebug;
    }

    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// The user-visible name of the channel.
        CharSequence name = "Chat Updates";
// The user-visible description of the channel.
        String description = "This channel is use to send you chat updates regarding this app.";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHAT_UPDATES_CHANNEL, name, importance);
// Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        //mChannel.setShowBadge(false);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);
    }
*/

}
