package in.appnow.blurt.user_auth.mvp;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import in.appnow.blurt.R;
import in.appnow.blurt.fcm.Config;
import in.appnow.blurt.rest.APIInterface;
import in.appnow.blurt.rest.response.UserProfile;
import in.appnow.blurt.user_auth.UserAuthActivity;
import in.appnow.blurt.user_auth.fragments.login.LoginFragment;
import in.appnow.blurt.user_auth.fragments.signup.SignUpFragment;
import in.appnow.blurt.utils.FragmentUtils;

/**
 * Created by sonu on 18:18, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UserAuthModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public UserAuthModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public void replaceLoginFragment() {
        FragmentUtils.replaceFragmentCommon(appCompatActivity.getSupportFragmentManager(), R.id.user_auth_container, LoginFragment.newInstance(), FragmentUtils.LOGIN_FRAGMENT, false);
    }

    public void replaceSignUpFragment() {
        FragmentUtils.replaceFragmentCommon(appCompatActivity.getSupportFragmentManager(), R.id.user_auth_container, SignUpFragment.newInstance(false,""), FragmentUtils.SIGN_UP_FRAGMENT, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleNotificationClick();

            }
        },100);
    }

    public void handleNotificationClick() {
        if (appCompatActivity.getIntent() != null) {
            if (appCompatActivity.getIntent().hasExtra(Config.MESSAGE_TYPE)) {
                String messageType = appCompatActivity.getIntent().getStringExtra(Config.MESSAGE_TYPE);
                switch (messageType) {
                    case Config.CHAT_MESSAGE_PUSH:
                        Fragment fragment = appCompatActivity.getSupportFragmentManager().findFragmentByTag(FragmentUtils.SIGN_UP_FRAGMENT);
                        if (fragment != null) {
                            ((UserAuthActivity.OnMessageNotificationListener) fragment).onNotificationClick();
                        }

                        break;
                }
            }
        }
    }

}
