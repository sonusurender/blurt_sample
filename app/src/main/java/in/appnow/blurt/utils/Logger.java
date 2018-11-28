package in.appnow.blurt.utils;

import android.util.Log;

import in.appnow.blurt.BuildConfig;
import in.appnow.blurt.app.Blurt;


/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class Logger {

    public static void ErrorLog(String tag, String message) {
        if (Blurt.getDebug()) {
            Log.e(tag, message);
        }
    }
    public static void DebugLog(String tag, String message) {
        if (Blurt.getDebug()) {
            Log.d(tag, message);
        }
    }
}
