package in.appnow.blurt.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by sonu on 22:42, 24/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AppUtils {
    public static void shareData(Context context, String packageName, String subject, String sharingText) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");// Plain format text
        if (!packageName.equalsIgnoreCase("more"))
            sharingIntent.setPackage(packageName);

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharingText);
        try {
            context.startActivity(sharingIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            context.startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));
        }
    }

    public static void sendMessage(Context context, String sharingMessage) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) //At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); //Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharingMessage);

            if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            context.startActivity(sendIntent);

        } else //For early versions, do what worked for you before.
        {
            Intent viewIntent = new Intent(Intent.ACTION_VIEW);
            viewIntent.setData(Uri.parse("sms:"));
            viewIntent.putExtra("sms_body", sharingMessage);
            context.startActivity(viewIntent);
        }
    }

    public static void openPlayStore(Context context, String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
        ((AppCompatActivity) context).finish();
    }

    public static String getLicenseKey(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("com.blurt.application.key");
        } catch (PackageManager.NameNotFoundException e) {
            ToastUtils.shortToast(context, "Failed to load meta-data, NameNotFound: " + e.getMessage());
            return "";
        } catch (NullPointerException e) {
            ToastUtils.shortToast(context, "Failed to load meta-data, NullPointer:" + e.getMessage());
            return "";
        }
    }

    public static String getWelcomeMessage(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("com.blurt.welcome.message");
        } catch (PackageManager.NameNotFoundException e) {
            ToastUtils.shortToast(context, "Failed to load meta-data, NameNotFound: " + e.getMessage());
            return "";
        } catch (NullPointerException e) {
            ToastUtils.shortToast(context, "Failed to load meta-data, NullPointer:" + e.getMessage());
            return "";
        }
    }

    public static Integer getMetaDataValueForResources(Context context, String metaDataName) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if (ai.metaData != null) {
                return bundle.getInt(metaDataName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            ToastUtils.shortToast(context, "Failed to load meta-data, NameNotFound: " + e.getMessage());
            return getLauncherIcon(context);
        } catch (NullPointerException e) {
            ToastUtils.shortToast(context, "Failed to load meta-data, NullPointer:" + e.getMessage());
            return getLauncherIcon(context);
        }
        return getLauncherIcon(context);
    }

    public static int getLauncherIcon(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return ai.icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
