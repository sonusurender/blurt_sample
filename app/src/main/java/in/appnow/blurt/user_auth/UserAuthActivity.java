package in.appnow.blurt.user_auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import javax.inject.Inject;

import in.appnow.blurt.R;
import in.appnow.blurt.app.Blurt;
import in.appnow.blurt.fcm.Config;
import in.appnow.blurt.fcm.NotificationUtils;
import in.appnow.blurt.user_auth.dagger.DaggerUserAuthComponent;
import in.appnow.blurt.user_auth.dagger.UserAuthComponent;
import in.appnow.blurt.user_auth.dagger.UserAuthModule;
import in.appnow.blurt.user_auth.fragments.login.LoginFragment;
import in.appnow.blurt.user_auth.mvp.UserAuthPresenter;
import in.appnow.blurt.user_auth.mvp.UserAuthView;
import in.appnow.blurt.utils.FragmentUtils;

/**
 * Created by sonu on 18:14, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UserAuthActivity extends AppCompatActivity {

    public static void openUserAuthActivity(Context context) {
        Intent intent = new Intent(context, UserAuthActivity.class);
        context.startActivity(intent);
    }

    @Inject
    UserAuthView view;
    @Inject
    UserAuthPresenter presenter;

    private UserAuthComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = DaggerUserAuthComponent.builder()
                .appComponent(Blurt.getInstance(this).component())
                .userAuthModule(new UserAuthModule(this))
                .build();
        component.inject(this);
        setContentView(view);
        presenter.onCreate();
    }

    public UserAuthComponent getComponent() {
        return component;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            Fragment otpFragment = fragmentManager.findFragmentByTag(FragmentUtils.OTP_FRAGMENT);
            if (otpFragment != null) {
                FragmentUtils.replaceFragmentCommon(fragmentManager, R.id.user_auth_container, LoginFragment.newInstance(), FragmentUtils.LOGIN_FRAGMENT, false);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (presenter != null) {
            presenter.onNotificationClick();
        }
    }

    public interface OnMessageNotificationListener {
        public void onNotificationClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationUtils.clearSingleNotification(this, Config.END_CHAT_NOTIFICATION_ID);
    }
}
