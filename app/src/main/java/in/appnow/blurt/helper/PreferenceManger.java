package in.appnow.blurt.helper;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import in.appnow.blurt.rest.response.UserProfile;

/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class PreferenceManger {

    public static final String FCM_UPDATED = "fcm_updated";
    public static final String FCM_TOKEN = "fcm_token";

    public static final String AUTH_TOKEN = "auth_token";
    public static final String LOGIN_DETAILS = "login_details";

    public static final String PREF_KEY = "blurt_preference";


    private SharedPreferences mSharedPreferences;

    public PreferenceManger(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    private SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(key);
        editor.apply();
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.apply();
    }


    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public boolean getBooleanValue(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public int getIntegerValue(String key) {
        return mSharedPreferences.getInt(key, 0);
    }


    public void putUserDetails(UserProfile user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        putString(LOGIN_DETAILS, json);
    }

    public UserProfile getUserDetails() {
        Gson gson = new Gson();
        String json = getStringValue(LOGIN_DETAILS);
        return gson.fromJson(json, UserProfile.class);
    }


    public void logoutUser() {
        putUserDetails(null);
        putString(AUTH_TOKEN,"");
        putBoolean(FCM_UPDATED,false);
    }

}
