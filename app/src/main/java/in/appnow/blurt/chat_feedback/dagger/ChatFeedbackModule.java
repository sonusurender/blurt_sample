package in.appnow.blurt.chat_feedback.dagger;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.blurt.chat_feedback.mvp.ChatFeedbackModel;
import in.appnow.blurt.chat_feedback.mvp.ChatFeedbackPresenter;
import in.appnow.blurt.chat_feedback.mvp.ChatFeedbackView;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.rest.APIInterface;

/**
 * Created by sonu on 17:25, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
@Module
public class ChatFeedbackModule {

    private final AppCompatActivity appCompatActivity;

    public ChatFeedbackModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    @ChatFeedbackScope
    public ChatFeedbackView chatFeedbackView() {
        return new ChatFeedbackView(appCompatActivity);
    }

    @Provides
    @ChatFeedbackScope
    public ChatFeedbackModel chatFeedbackModel(APIInterface apiInterface) {
        return new ChatFeedbackModel(appCompatActivity,apiInterface);
    }

    @Provides
    @ChatFeedbackScope
    public ChatFeedbackPresenter chatFeedbackPresenter(ChatFeedbackView view, ChatFeedbackModel model, PreferenceManger preferenceManger) {
        return new ChatFeedbackPresenter(view, model,preferenceManger);
    }
}
