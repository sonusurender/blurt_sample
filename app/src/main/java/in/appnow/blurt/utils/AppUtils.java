package in.appnow.blurt.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
}
