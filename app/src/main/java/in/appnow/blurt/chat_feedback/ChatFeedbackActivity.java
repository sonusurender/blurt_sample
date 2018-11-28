package in.appnow.blurt.chat_feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import in.appnow.blurt.app.Blurt;
import in.appnow.blurt.base.BaseActivity;
import in.appnow.blurt.chat_feedback.dagger.ChatFeedbackModule;
import in.appnow.blurt.chat_feedback.dagger.DaggerChatFeedbackComponent;
import in.appnow.blurt.chat_feedback.mvp.ChatFeedbackPresenter;
import in.appnow.blurt.chat_feedback.mvp.ChatFeedbackView;


/**
 * Created by sonu on 17:25, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatFeedbackActivity extends BaseActivity {

    public static final String ARG_MESSAGE = "message";
    public static final String ARG_SESSION_ID = "session_id";
    @Inject
    ChatFeedbackView view;
    @Inject
    ChatFeedbackPresenter presenter;

    public static void openChatFeedbackActivity(Context context, String sessionId, boolean isFinish, String message) {
        Intent intent = new Intent(context, ChatFeedbackActivity.class);
        intent.putExtra(ARG_MESSAGE, message);
        intent.putExtra(ARG_SESSION_ID, sessionId);
        context.startActivity(intent);
        if (isFinish)
            ((AppCompatActivity) context).finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerChatFeedbackComponent.builder()
                .appComponent(Blurt.getInstance(this).component())
                .chatFeedbackModule(new ChatFeedbackModule(this))
                .build().inject(this);
        setContentView(view);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
