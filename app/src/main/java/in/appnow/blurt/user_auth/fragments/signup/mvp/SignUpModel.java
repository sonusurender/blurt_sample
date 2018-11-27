package in.appnow.blurt.user_auth.fragments.signup.mvp;

import android.support.v7.app.AppCompatActivity;

import in.appnow.blurt.base.BaseModel;
import in.appnow.blurt.conversation_module.activity.ConversationActivity;
import in.appnow.blurt.rest.APIInterface;
import in.appnow.blurt.rest.request.BaseRequestModel;
import in.appnow.blurt.rest.request.CreateAccountRequest;
import in.appnow.blurt.rest.request.FCMRequestModel;
import in.appnow.blurt.rest.response.BaseResponseModel;
import in.appnow.blurt.rest.response.LoginResponseModel;
import in.appnow.blurt.rest.response.StartChatResponse;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by sonu on 18:57, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SignUpModel extends BaseModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public SignUpModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }


    public Observable<LoginResponseModel> createAccount(CreateAccountRequest request) {
        return apiInterface.createCustomerAccount(request);
    }

    public Observable<StartChatResponse> startChat(BaseRequestModel request) {
        return apiInterface.startChat(request);
    }

    public void openChatActivity(String chatSessionId, boolean existingChat) {
        ConversationActivity.openActivity(appCompatActivity, chatSessionId, existingChat);
    }

    public Observable<BaseResponseModel> updateFCMToken(FCMRequestModel requestModel) {
        return apiInterface.updateFCMKey(requestModel);
    }
}
