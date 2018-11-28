package in.appnow.blurt.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import in.appnow.blurt.R;


/**
 * Created by sonu on 15:36, 18/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class DeviceUtils {
    public static boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        ToastUtils.longToast(context,R.string.camera_not_supported);
        return false;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
