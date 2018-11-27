package in.appnow.blurt.conversation_module.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import in.appnow.blurt.R;
import in.appnow.blurt.R2;
import in.appnow.blurt.app.AstroApplication;
import in.appnow.blurt.conversation_module.activity.dagger.ConversationModule;
import in.appnow.blurt.conversation_module.activity.dagger.DaggerConversationComponent;
import in.appnow.blurt.conversation_module.activity.mvp.ConversationActivityView;
import in.appnow.blurt.conversation_module.activity.mvp.ConversationPresenter;
import in.appnow.blurt.conversation_module.chat_interfaces.OnChatToolbarMenuClickListener;
import in.appnow.blurt.conversation_module.utils.ConversationUtils;
import in.appnow.blurt.dialog.DialogHelperClass;
import in.appnow.blurt.fcm.Config;
import in.appnow.blurt.fcm.NotificationUtils;
import in.appnow.blurt.utils.Logger;


/**
 * Created by sonu on 19/12/17.
 */

public class ConversationActivity extends AppCompatActivity implements OnChatToolbarMenuClickListener {

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
                .appComponent(AstroApplication.get(this).component())
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
        NotificationUtils.clearSingleNotification(Config.CHAT_MESSAGE_NOTIFICATION_ID);
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

    @Override
    public void onDeleteOptionClick() {
        //    deleteSelectedMessages();
    }

    @Override
    public void destroyActionMode() {
        // setNullToActionMode();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R2.id.action_refresh:
                //  fetchAllMessages();
                break;
            case R2.id.action_exit:
                if (AstroApplication.getInstance().isInternetConnected(true)) {
                    DialogHelperClass.showMessageOKCancel(ConversationActivity.this, "Are you sure you want to exit from chat.", "Ok", "Cancel", (dialogInterface, i) -> presenter.onEndChat(), null);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
