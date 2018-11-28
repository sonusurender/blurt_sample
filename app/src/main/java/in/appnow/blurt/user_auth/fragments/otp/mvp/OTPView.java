package in.appnow.blurt.user_auth.fragments.otp.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.blurt.R;
import in.appnow.blurt.R2;
import in.appnow.blurt.base.BaseView;
import in.appnow.blurt.utils.TextUtils;
import in.appnow.blurt.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 18:52, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class OTPView extends FrameLayout implements BaseView {

    protected static final int RESEND_OTP_REQUEST_CODE = 1;
    protected static final int VERIFY_OTP_REQUEST_CODE = 2;

    @BindView(R2.id.otp_value_one)
    AppCompatEditText otpInputOne;
    @BindView(R2.id.otp_value_two)
    AppCompatEditText otpInputTwo;
    @BindView(R2.id.otp_value_three)
    AppCompatEditText otpInputThree;
    @BindView(R2.id.otp_value_four)
    AppCompatEditText otpInputFour;
    @BindView(R2.id.resend_otp)
    AppCompatButton resentOTPButton;
    @BindView(R2.id.login)
    AppCompatButton loginButton;

    public OTPView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.fragment_otp_verify_layout, this);
        ButterKnife.bind(this, this);
    }

    public Observable<Object> observeResendOTPButtonClick() {
        return RxView.clicks(resentOTPButton);
    }

    public Observable<Object> observeLoginButtonClick() {
        return RxView.clicks(loginButton);
    }

    public String getOTP() {
        return TextUtils.getText(otpInputOne) + TextUtils.getText(otpInputTwo) + TextUtils.getText(otpInputThree) + TextUtils.getText(otpInputFour);
    }

    public Observable<CharSequence> observeFirstInputTextChange() {
        return RxTextView.textChanges(otpInputOne);
    }

    public Observable<CharSequence> observeSecondInputTextChange() {
        return RxTextView.textChanges(otpInputTwo);
    }

    public Observable<CharSequence> observeThirdInputTextChange() {
        return RxTextView.textChanges(otpInputThree);
    }
    public Observable<CharSequence> observeFourthInputTextChange() {
        return RxTextView.textChanges(otpInputFour);
    }

    public void changeFocus(int position){
        switch (position){
            case 1:
                otpInputTwo.requestFocus();
                break;
            case 2:
                otpInputThree.requestFocus();
                break;
            case 3:
                otpInputFour.requestFocus();
                break;

        }
    }


    public void resetViews() {
        otpInputOne.setText("");
        otpInputTwo.setText("");
        otpInputThree.setText("");
        otpInputFour.setText("");
    }

    public boolean validateOTP() {
        if (!android.text.TextUtils.isEmpty(getOTP()) && getOTP().length() == 4) {
            return true;
        }
        ToastUtils.shortToast(getContext(),"Please enter valid OTP.");
        return false;
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
