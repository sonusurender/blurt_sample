package in.appnow.blurt.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sonu on 15:37, 18/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static File getMainOutputDirectory(Context context) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Blurt");
        //If File is not present create directory
        if (!file.exists()) {
            Logger.DebugLog(TAG, "Main File not exist : " + file.getAbsolutePath());
            if (file.mkdir()) {
                Logger.DebugLog(TAG, "Main Create file : " + file.getAbsolutePath());

            } else {
                Logger.DebugLog(TAG, "Main Create file Failed : " + file.getAbsolutePath());

            }
        } else {
            Logger.DebugLog(TAG, "Main File already exist : " + file.getAbsolutePath());

        }
        return file;
    }

    public static File getCameraOutputFile(Context context) {
        File mainDirectoryPath = getMainOutputDirectory(context);
        if (mainDirectoryPath != null) {
            File cameraOutputFile = new File(mainDirectoryPath, String.format("%s.jpg", System.currentTimeMillis()));
            if (!cameraOutputFile.exists()) {
                Logger.DebugLog(TAG, "Camera File not exist : " + cameraOutputFile.getAbsolutePath());
                if (cameraOutputFile.mkdir()) {
                    Logger.DebugLog(TAG, "Camera Create file : " + cameraOutputFile.getAbsolutePath());

                } else {
                    Logger.DebugLog(TAG, "Camera Create file Failed : " + cameraOutputFile.getAbsolutePath());

                }
            } else {
                Logger.DebugLog(TAG, "Camera File already exist : " + cameraOutputFile.getAbsolutePath());

            }
            return cameraOutputFile;


        }
        return null;
    }

    public static File createImageFile(Context context) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getPictureDirectory(context);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        //   mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static File getPictureDirectory(Context context) {
        File storageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Blurt");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        return storageDir;
    }

}
