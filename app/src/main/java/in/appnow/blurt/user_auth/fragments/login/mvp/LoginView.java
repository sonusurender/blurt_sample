package in.appnow.blurt.user_auth.fragments.login.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.blurt.R;
import in.appnow.blurt.R2;
import in.appnow.blurt.base.BaseView;
import in.appnow.blurt.utils.TextUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 18:48, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class LoginView extends FrameLayout implements BaseView {

    protected static final int CHECK_USER_REQUEST_CODE = 1;

    @BindView(R2.id.login_email)
    EditText enterEmailId;
    @BindView(R2.id.login_continue_button)
    AppCompatButton loginContinueButton;

    public LoginView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.fragment_email_layout, this);
        ButterKnife.bind(this, this);
    }

    public Observable<Object> observeContinueButtonClick() {
        return RxView.clicks(loginContinueButton);
    }

    public String getEmailId() {
        return TextUtils.getText(enterEmailId);
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
