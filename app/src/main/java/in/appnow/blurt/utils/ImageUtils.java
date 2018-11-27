package in.appnow.blurt.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;

import java.io.ByteArrayOutputStream;

import in.appnow.blurt.R;
import in.appnow.blurt.helper.glide.GlideApp;
import in.appnow.blurt.rest.RestUtils;

/**
 * Created by sonu on 14:41, 19/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ImageUtils {


    public static void setDrawableImage(Context context, ImageView imageView, int res) {
        GlideApp.with(context).load(res)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void changeImageColor(ImageView imageView, Context context, int color) {
        imageView.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);
    }

    public static Drawable changeShapeColor(Context context, int drawable, int color) {
        Drawable mDrawable = ContextCompat.getDrawable(context, drawable);
        mDrawable.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(color), PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }

    public static void loadImageUrl(Context context, ImageView imageView, int placeHolder, String imageLink) {
        String imageNewLink = "";
        if (!imageLink.contains("http")) {
            imageNewLink = RestUtils.getEndPoint() + imageLink;
        } else imageNewLink = imageLink;
        GlideApp.with(context).load(imageNewLink)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(imageView);
    }

    public static Bitmap convertImagePathToBitmap(String imagePath, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, new BitmapFactory.Options());
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static String convertBitmapIntoBase64(Bitmap bitmap) {
        return Base64.encodeToString(getBytesFromBitmap(bitmap), Base64.NO_WRAP);
    }

    private static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            int column_index = 0;
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            }
            if (cursor != null) {
                cursor.moveToFirst();
            }
            String string = null;
            if (cursor != null) {
                string = cursor.getString(column_index);
            }
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
