package in.appnow.blurt.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import in.appnow.blurt.R;
import in.appnow.blurt.dagger.component.AppComponent;
import in.appnow.blurt.dagger.component.DaggerAppComponent;
import in.appnow.blurt.dagger.module.AppModule;
import in.appnow.blurt.dagger.module.RoomModule;
import in.appnow.blurt.dagger.module.SharedPreferencesModule;
import in.appnow.blurt.network.CheckInternetConnection;
import in.appnow.blurt.utils.ToastUtils;
import io.fabric.sdk.android.Fabric;

import static in.appnow.blurt.fcm.NotificationUtils.CHAT_UPDATES_CHANNEL;

/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class AstroApplication extends MultiDexApplication {

    private static AstroApplication instance;
    private AppComponent appComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;


        // This line needs to be executed before any usage of EmojiTextView, EmojiEditText or EmojiButton.
        EmojiManager.install(new GoogleEmojiProvider());


        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this))
                .sharedPreferencesModule(new SharedPreferencesModule(this))
                .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
    }

    public static AstroApplication getInstance() {
        return instance;
    }

    public static AstroApplication get(AppCompatActivity activity) {
        return (AstroApplication) activity.getApplication();
    }

    public static AstroApplication get(Service service) {
        return (AstroApplication) service.getApplication();
    }

    public AppComponent component() {
        return appComponent;
    }


    public boolean isInternetConnected(boolean showToast) {
        if (new CheckInternetConnection(this).isConnected())
            return true;
        else {
            if (showToast)
                ToastUtils.longToast(R.string.no_internet_connection);
            return false;
        }
    }


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

}
