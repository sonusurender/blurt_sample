package in.appnow.blurt.utils;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import in.appnow.blurt.app.Blurt;


/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class ToastUtils {

    public static void shortToast(Context context,@StringRes int text) {
        shortToast(context,context.getResources().getString(text));
    }

    public static void shortToast(Context context,String text) {
        if (TextUtils.isEmpty(text))
            return;
        show(context,text, Toast.LENGTH_SHORT);
    }

    public static void longToast(Context context,@StringRes int text) {
        longToast(context,context.getResources().getString(text));
    }

    public static void longToast(Context context,String text) {
        if (TextUtils.isEmpty(text))
            return;
        show(context,text, Toast.LENGTH_LONG);
    }

    private static Toast makeToast(Context context,String text, @ToastLength int length) {
        return Toast.makeText(context, text, length);
    }

    private static void show(Context context,String text, @ToastLength int length) {
        makeToast(context,text, length).show();
    }

    @IntDef({Toast.LENGTH_LONG, Toast.LENGTH_SHORT})
    public @interface ToastLength {

    }
}