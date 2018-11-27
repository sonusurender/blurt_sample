package in.appnow.blurt.user_auth.fragments.login.mvp;

import android.support.v7.app.AppCompatActivity;

import in.appnow.blurt.R;
import in.appnow.blurt.base.BaseModel;
import in.appnow.blurt.rest.APIInterface;
import in.appnow.blurt.rest.request.UserAvailabilityRequest;
import in.appnow.blurt.rest.response.BaseResponseModel;
import in.appnow.blurt.user_auth.fragments.login.LoginFragment;
import in.appnow.blurt.user_auth.fragments.otp.OTPFragment;
import in.appnow.blurt.utils.FragmentUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 18:49, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class LoginModel extends BaseModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public LoginModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<BaseResponseModel> checkUserAvailability(UserAvailabilityRequest request) {
        return apiInterface.findUserAvailability(request);
    }

    public void replaceOTPFragment(String emailId) {
        FragmentUtils.replaceFragmentCommon(appCompatActivity.getSupportFragmentManager(), R.id.user_auth_container, OTPFragment.newInstance(emailId), FragmentUtils.OTP_FRAGMENT, false);
    }
}
