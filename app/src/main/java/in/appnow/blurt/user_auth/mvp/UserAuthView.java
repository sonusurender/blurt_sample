package in.appnow.blurt.user_auth.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import in.appnow.blurt.R;
import in.appnow.blurt.base.BaseView;

/**
 * Created by sonu on 18:18, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UserAuthView extends FrameLayout implements BaseView {

    public UserAuthView(@NonNull AppCompatActivity context) {
        super(context);
        inflate(context, R.layout.activity_user_auth, this);
        ButterKnife.bind(this, this);
    }

    @Override
    public void onUnknownError(String error, int requestCode) {

    }

    @Override
    public void onTimeout(int requestCode) {

    }

    @Override
    public void onNetworkError(int requestCode) {

    }

    @Override
    public boolean isNetworkConnected(int requestCode) {
        return false;
    }

    @Override
    public void onConnectionError(int requestCode) {

    }
}
