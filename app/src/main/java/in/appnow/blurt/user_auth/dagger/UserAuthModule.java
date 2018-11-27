package in.appnow.blurt.user_auth.dagger;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.rest.APIInterface;
import in.appnow.blurt.user_auth.fragments.login.mvp.LoginModel;
import in.appnow.blurt.user_auth.fragments.login.mvp.LoginPresenter;
import in.appnow.blurt.user_auth.fragments.login.mvp.LoginView;
import in.appnow.blurt.user_auth.fragments.otp.mvp.OTPModel;
import in.appnow.blurt.user_auth.fragments.otp.mvp.OTPPresenter;
import in.appnow.blurt.user_auth.fragments.otp.mvp.OTPView;
import in.appnow.blurt.user_auth.fragments.signup.mvp.SignUpModel;
import in.appnow.blurt.user_auth.fragments.signup.mvp.SignUpPresenter;
import in.appnow.blurt.user_auth.fragments.signup.mvp.SignUpView;
import in.appnow.blurt.user_auth.mvp.UserAuthModel;
import in.appnow.blurt.user_auth.mvp.UserAuthPresenter;
import in.appnow.blurt.user_auth.mvp.UserAuthView;

/**
 * Created by sonu on 18:15, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
@Module
public class UserAuthModule {

    private final AppCompatActivity appCompatActivity;

    public UserAuthModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    /* User Auth Activity MVP Injection */
    @Provides
    @UserAuthScope
    public UserAuthView userAuthView() {
        return new UserAuthView(appCompatActivity);
    }

    @Provides
    @UserAuthScope
    public UserAuthModel userAuthModel(APIInterface apiInterface) {
        return new UserAuthModel(appCompatActivity, apiInterface);
    }

    @Provides
    @UserAuthScope
    public UserAuthPresenter userAuthPresenter(UserAuthView view, UserAuthModel model, PreferenceManger preferenceManger) {
        return new UserAuthPresenter(view, model,preferenceManger);
    }

    /* Login Fragment MVP Injection */
    @Provides
    @UserAuthScope
    public LoginView loginView() {
        return new LoginView(appCompatActivity);
    }

    @Provides
    @UserAuthScope
    public LoginModel loginModel(APIInterface apiInterface) {
        return new LoginModel(appCompatActivity, apiInterface);
    }

    @Provides
    @UserAuthScope
    public LoginPresenter loginPresenter(LoginView view, LoginModel model) {
        return new LoginPresenter(view, model);
    }

    /* OTP Fragment MVP Injection */
    @Provides
    @UserAuthScope
    public OTPView otpView() {
        return new OTPView(appCompatActivity);
    }

    @Provides
    @UserAuthScope
    public OTPModel otpModel(APIInterface apiInterface) {
        return new OTPModel(appCompatActivity, apiInterface);
    }

    @Provides
    @UserAuthScope
    public OTPPresenter otpPresenter(OTPView view, OTPModel model,PreferenceManger preferenceManger) {
        return new OTPPresenter(view, model,preferenceManger);
    }

    /* SignUp Fragment MVP Injection */
    @Provides
    @UserAuthScope
    public SignUpView signUpView() {
        return new SignUpView(appCompatActivity);
    }

    @Provides
    @UserAuthScope
    public SignUpModel signUpModel(APIInterface apiInterface) {
        return new SignUpModel(appCompatActivity, apiInterface);
    }

    @Provides
    @UserAuthScope
    public SignUpPresenter signUpPresenter(SignUpView view, SignUpModel model,PreferenceManger preferenceManger) {
        return new SignUpPresenter(view, model,preferenceManger);
    }

}
