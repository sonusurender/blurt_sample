package in.appnow.blurt.user_auth.fragments.otp.mvp;

import android.support.v7.app.AppCompatActivity;

import in.appnow.blurt.R;
import in.appnow.blurt.base.BaseModel;
import in.appnow.blurt.rest.APIInterface;
import in.appnow.blurt.rest.request.UserAvailabilityRequest;
import in.appnow.blurt.rest.request.ValidateUserRequest;
import in.appnow.blurt.rest.response.BaseResponseModel;
import in.appnow.blurt.rest.response.LoginResponseModel;
import in.appnow.blurt.user_auth.fragments.otp.OTPFragment;
import in.appnow.blurt.user_auth.fragments.signup.SignUpFragment;
import in.appnow.blurt.utils.FragmentUtils;

/**
 * Created by sonu on 18:54, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class OTPModel extends BaseModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public OTPModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public io.reactivex.Observable<BaseResponseModel> checkUserAvailability(UserAvailabilityRequest request) {
        return apiInterface.findUserAvailability(request);
    }

    public io.reactivex.Observable<LoginResponseModel> verifyOTP(ValidateUserRequest request) {
        return apiInterface.validateUser(request);
    }

    public void replaceSignUpFragment(boolean isNewUser,String emailId) {
        FragmentUtils.replaceFragmentCommon(appCompatActivity.getSupportFragmentManager(), R.id.user_auth_container, SignUpFragment.newInstance(isNewUser,emailId), FragmentUtils.SIGN_UP_FRAGMENT, false);
    }
}
