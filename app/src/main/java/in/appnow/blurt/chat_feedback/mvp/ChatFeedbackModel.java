package in.appnow.blurt.chat_feedback.mvp;

import android.support.v7.app.AppCompatActivity;

import in.appnow.blurt.chat_feedback.ChatFeedbackActivity;
import in.appnow.blurt.dialog.ProgressDialogFragment;
import in.appnow.blurt.rest.APIInterface;
import in.appnow.blurt.rest.request.ChatFeedbackRequest;
import in.appnow.blurt.rest.response.BaseResponseModel;
import io.reactivex.Observable;


/**
 * Created by sonu on 17:28, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatFeedbackModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public ChatFeedbackModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    /* public int getTopicsDebited() {
             return appCompatActivity.getIntent().getIntExtra(ChatFeedbackActivity.ARG_TOPIC, 0);
         }*/
    public Observable<BaseResponseModel> sendChatFeedback(ChatFeedbackRequest request) {
        return apiInterface.submitChatFeedback(request);
    }

    public String getSessionId() {
        return appCompatActivity.getIntent().getStringExtra(ChatFeedbackActivity.ARG_SESSION_ID);
    }
    public String getMessage() {
        return appCompatActivity.getIntent().getStringExtra(ChatFeedbackActivity.ARG_MESSAGE);
    }

    public void closeActivity() {
        appCompatActivity.finish();
        //appCompatActivity.onBackPressed();
    }

    public void showProgress() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgress() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());
    }
}
