package in.appnow.blurt.user_auth.fragments.signup.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.blurt.R;
import in.appnow.blurt.R2;
import in.appnow.blurt.base.BaseView;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.main.common_activity.CommonActivity;
import in.appnow.blurt.rest.response.UserProfile;
import in.appnow.blurt.utils.FragmentUtils;
import in.appnow.blurt.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 18:56, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SignUpView extends FrameLayout implements BaseView {

    public static final int UPDATE_TOKEN_REQUEST_CODE = 2;
    protected static final int CREATE_ACCOUNT_REQUEST_CODE = 1;
    @BindView(R2.id.signup_email)
    AppCompatEditText enterEmailId;
    @BindView(R2.id.signup_name)
    AppCompatEditText enterName;
    @BindView(R2.id.signup_continue_button)
    AppCompatButton signUpContinueButton;
    @BindView(R2.id.terms_condition)
    AppCompatTextView termsConditionsLabel;
    @BindView(R2.id.signup_layout)
    RelativeLayout signUpLayout;

    @BindView(R2.id.welcome_back_layout)
    RelativeLayout welcomeLayout;
    @BindView(R2.id.signup_user_name)
    AppCompatTextView signUpUserName;
    @BindView(R2.id.re_terms_condition)
    AppCompatTextView welcomeTermsConditionsLabel;
    @BindView(R2.id.start_chat_button)
    AppCompatButton startChatButton;


    public SignUpView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.fragment_signup_layout, this);
        ButterKnife.bind(this, this);
        setPrivacyPolicySpannable(context);
    }

    private void setPrivacyPolicySpannable(Context context) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "By starting chat, you agree to Blurt's");
        spanTxt.append(" Terms & Conditions");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                CommonActivity.openCommonActivity(context, "Terms & Conditions", FragmentUtils.WEB_VIEW_FRAGMENT);
            }
        }, spanTxt.length() - "Terms & Conditions".length(), spanTxt.length(), 0);
        spanTxt.append(" and Privacy Policy.");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                CommonActivity.openCommonActivity(context, "Privacy Policy", FragmentUtils.WEB_VIEW_FRAGMENT);
            }
        }, spanTxt.length() - " Privacy Policy".length(), spanTxt.length(), 0);
        termsConditionsLabel.setMovementMethod(LinkMovementMethod.getInstance());
        termsConditionsLabel.setText(spanTxt, TextView.BufferType.SPANNABLE);

        welcomeTermsConditionsLabel.setMovementMethod(LinkMovementMethod.getInstance());
        welcomeTermsConditionsLabel.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }


    public void updateViews(boolean isNewUser, @Nullable String emailId, @Nullable UserProfile userProfile) {
        if (isNewUser) {
            welcomeLayout.setVisibility(View.GONE);
            signUpLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(emailId)) {
                enterEmailId.setText(emailId);
                enterEmailId.setEnabled(false);
            }
        } else {
            welcomeLayout.setVisibility(View.VISIBLE);
            signUpLayout.setVisibility(View.GONE);
            if (userProfile != null) {
                signUpUserName.setText("Hi , " + userProfile.getName());
            }
        }
    }

    public Observable<Object> observeContinueButtonClick() {
        return RxView.clicks(signUpContinueButton);
    }

    public Observable<Object> observeStartChatButtonClick() {
        return RxView.clicks(startChatButton);
    }

    public String getEmailId() {
        return in.appnow.blurt.utils.TextUtils.getText(enterEmailId);
    }

    public String getUserName() {
        return in.appnow.blurt.utils.TextUtils.getText(enterName);
    }

    public boolean isUserNameValid() {
        if (!TextUtils.isEmpty(getUserName())) {
            return true;
        }
        ToastUtils.shortToast(getContext(),"Please enter your name.");
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
