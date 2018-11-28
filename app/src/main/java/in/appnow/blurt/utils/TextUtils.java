package in.appnow.blurt.utils;

import android.content.Context;
import android.text.InputFilter;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Scope;

/**
 * Created by sonu on 16:07, 25/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TextUtils {

    public static boolean isNumberExist(String value) {
        Pattern numberPat = Pattern.compile("\\d+");
        Matcher matcher = numberPat.matcher(value);
        return matcher.find();
    }

    public static void setTextMaxLength(EditText editText, int maxLength) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public static boolean isEmailIdValid(Context context,String target) {
        if (!android.text.TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            return true;
        }else {
            ToastUtils.shortToast(context,"Please enter valid email address.");
            return false;
        }
    }

    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

}
