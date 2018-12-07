package in.appnow.blurt.conversation_module.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import in.appnow.blurt.app.Blurt;
import in.appnow.blurt.conversation_module.activity.dagger.ConversationModule;
import in.appnow.blurt.conversation_module.activity.dagger.DaggerConversationComponent;
import in.appnow.blurt.conversation_module.activity.mvp.ConversationActivityView;
import in.appnow.blurt.conversation_module.activity.mvp.ConversationPresenter;
import in.appnow.blurt.conversation_module.utils.ConversationUtils;
import in.appnow.blurt.fcm.Config;
import in.appnow.blurt.fcm.NotificationUtils;


/**
 * Created by sonu on 19/12/17.
 */

public class ConversationActivity extends AppCompatActivity{

    private static final String TAG = ConversationActivity.class.getSimpleName();

    public static final String EXTRA_SESSION_ID = "session_id";
    public static final String EXTRA_EXISTING_CHAT = "existing_chat";

    @Inject
    ConversationActivityView view;
    @Inject
    ConversationPresenter presenter;

    public static void openActivity(Context context, String sessionId, boolean isExistingChat) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra(EXTRA_SESSION_ID, sessionId);
        intent.putExtra(EXTRA_EXISTING_CHAT, isExistingChat);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerConversationComponent.builder()
                .appComponent(Blurt.getInstance(this).component())
                .conversationModule(new ConversationModule(this))
                .build().inject(this);
        setContentView(view);
        presenter.onCreate();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (presenter.onBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetUnreadMessageCounter();
        NotificationUtils.clearSingleNotification(this,Config.CHAT_MESSAGE_NOTIFICATION_ID);
    }

    private void resetUnreadMessageCounter() {
        // TODO: 10/04/18 Implement dagger preference manager

        //PreferenceManger.putInt(PreferenceManger.MESSAGE_UNREAD_COUNTER, 0);
    }

    @Override
    protected void onPause() {
        presenter.updateTypingStatus(ConversationUtils.NO_TYPING_STATUS);
        super.onPause();
    }
}
