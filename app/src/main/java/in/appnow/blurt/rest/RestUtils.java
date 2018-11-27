package in.appnow.blurt.rest;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import in.appnow.blurt.BuildConfig;
import in.appnow.blurt.utils.ToastUtils;

/**
 * Created by sonu on 12:16, 13/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class RestUtils {

    public static String getEndPoint() {
        return BuildConfig.DEBUG ? BuildConfig.PRODUCTION_END_POINT : BuildConfig.PRODUCTION_END_POINT;
    }

    public static String getCertFileName() {
        return BuildConfig.DEBUG ? "server.crt" : "server.crt";
    }

    public static String getLicenseKey(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return String.valueOf(bundle.getInt("blurt_api_key"));
        } catch (PackageManager.NameNotFoundException e) {
            ToastUtils.shortToast("Failed to load meta-data, NameNotFound: " + e.getMessage());
            return "";
        } catch (NullPointerException e) {
            ToastUtils.shortToast("Failed to load meta-data, NullPointer:" + e.getMessage());
            return "";
        }
    }
}
