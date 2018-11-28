package in.appnow.blurt.conversation_module.activity.mvp;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.util.Collections;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.appnow.blurt.R;
import in.appnow.blurt.R2;
import in.appnow.blurt.base.BaseView;
import in.appnow.blurt.conversation_module.adapters.ConversationMessageAdapter;
import in.appnow.blurt.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.blurt.conversation_module.utils.ConversationUtils;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.utils.ImageUtils;
import in.appnow.blurt.utils.ToastUtils;
import io.reactivex.Observable;

@SuppressLint("ViewConstructor")
public class ConversationActivityView extends FrameLayout implements BaseView {

    private static final String TAG = ConversationActivityView.class.getSimpleName();
    @BindView(R2.id.conversation_toolbar)
    Toolbar toolbar;
    @BindView(R2.id.conversation_recycler_view)
    RecyclerView conversationRecyclerView;
    @BindView(R2.id.conversation_toolbar_title)
    TextView toolbarTitle;
    @BindView(R2.id.conversation_toolbar_subtitle)
    TextView toolbarSubtitle;
    @BindView(R2.id.conversation_toolbar_user_icon)
    CircleImageView toolbarUserIcon;
    @BindView(R2.id.conversation_enter_a_message)
    EmojiEditText typeMessage;
    @BindView(R2.id.conversation_send_button)
    ImageView sendButton;
    @BindView(R2.id.emojiButton)
    ImageView emojiButton;
    @BindView(R2.id.no_chats_label)
    TextView emptyChatsLabel;
    @BindView(R2.id.conversation_progress_bar)
    ProgressBar conversationProgressBar;
    @BindView(R2.id.conversation_type_layout)
    LinearLayout conversationTypeLayout;
    @BindView(R2.id.conversation_navigation_layout)
    LinearLayout backArrowLayout;
    @BindView(R2.id.chat_queue_sequence_label)
    TextView queueSequenceLabel;

    @BindString(R2.string.chat_welcome_message)
    String welcomeMessage;
    @BindString(R2.string.chat_wait_message)
    String waitMessage;
    @BindString(R2.string.chat_sequence)
    String chatSequenceString;
    @BindString(R2.string.astrobuddy_join_message)
    String handlerJoinString;
    @BindString(R2.string.typing_status)
    String typingStatusString;
    @BindString(R2.string.online_status)
    String onlineStatus;
    @BindString(R2.string.offline_status)
    String offlineStatus;
    @BindString(R2.string.away_status)
    String awayStatus;

    @BindString(R2.string.unknown_error)
    String unknownErrorString;

    private EmojiPopup emojiPopup;

    private AppCompatActivity appCompatActivity;

    private String handlerStatus;

    public final ConversationMessageAdapter adapter = new ConversationMessageAdapter(getContext());

    public ConversationActivityView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        inflate(getContext(), R.layout.activity_conversation, this);
        ButterKnife.bind(this);
        appCompatActivity.setSupportActionBar(toolbar);
        setUpEmojiconPopup();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setAdapter(adapter);
    }

    public void setHandlerStatus(String handlerStatus) {
        this.handlerStatus = handlerStatus;
        updateTypingStatus(ConversationUtils.NO_TYPING_STATUS);
    }


    public Observable<Object> observeSendButton() {
        return RxView.clicks(sendButton);
    }

    public Observable<Object> observeEmojiButton() {
        return RxView.clicks(emojiButton);
    }

    public Observable<Object> observeBackArrowPress() {
        return RxView.clicks(backArrowLayout);
    }

    public Observable<CharSequence> observeTextChange() {
        return RxTextView.textChanges(typeMessage);
    }

    public void onChatTextChange(CharSequence charSequence) {
        String message = charSequence.toString().trim();
        changeSendButtonImageColor(!TextUtils.isEmpty(message));
    }

    private void changeSendButtonImageColor(boolean isMessageEmpty) {
        if (isMessageEmpty) {
            ImageUtils.changeImageColor(sendButton, appCompatActivity, R.color.colorPrimary);
        } else {
            ImageUtils.changeImageColor(sendButton, appCompatActivity, R.color.white_transparent_70);
        }
    }
    public void updateTypeMessage(String text) {
        typeMessage.setText(text);
    }

    public String getTypedMessage() {
        return typeMessage.getText().toString().trim();
    }


    public void setData(List<ConversationResponse> conversationList, PreferenceManger preferenceManger) {
        Collections.sort(conversationList, (conversationResponse, t1) -> (int) conversationResponse.getSendDateTime() - (int) t1.getSendDateTime());
        adapter.setPreferenceManger(preferenceManger);
        adapter.swapData(conversationList);
        scrollToBottom();
    }

    public void setMessage(String message) {
        ToastUtils.longToast(appCompatActivity,message);
    }

    private void setUpEmojiconPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(this).setOnEmojiBackspaceClickListener(v -> {

        }).setOnEmojiClickListener((emoji, imageView) -> {

        }).setOnEmojiPopupDismissListener(() -> {
            changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_tag_faces_black_24dp);
        }).setOnSoftKeyboardCloseListener(() -> {

        }).setOnSoftKeyboardOpenListener(keyBoardHeight -> {

        }).setOnEmojiPopupShownListener(() -> {
            changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_keyboard_black_24dp);
        }).build(typeMessage);
    }

    public boolean dismissEmojiPopup() {
        if (emojiPopup != null && emojiPopup.isShowing()) {
            emojiPopup.dismiss();
            return false;
        }
        return true;
    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId) {
        iconToBeChanged.setImageResource(drawableResourceId);
    }

    public void toggleEmojiPopup() {
        emojiPopup.toggle();
    }

    private void scrollToBottom() {
        if (adapter.getConversationModelArrayList() != null && adapter.getConversationModelArrayList().size() > 1) {
            conversationRecyclerView.smoothScrollToPosition(adapter.getConversationModelArrayList().size());
        }
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }


    /* =====   Update queue label Methods STARTED!!!!  ===== */

    public void updateQueueLabelText(String text) {
        queueSequenceLabel.setText(text);
       showHideQueueLabel(View.VISIBLE);
    }

    public void showHideQueueLabel(int visibility) {
        queueSequenceLabel.setVisibility(visibility);
    }

    /* =====   Update queue label Methods ENDS!!!!  ===== */

    public void updateTypingStatus(int typingStatus) {
        if (typingStatus == ConversationUtils.TYPING_STATUS) {
            toolbarSubtitle.setText(typingStatusString);
            toolbarSubtitle.setVisibility(View.VISIBLE);
        } else {
            if (!TextUtils.isEmpty(handlerStatus)) {
                toolbarSubtitle.setText(handlerStatus);
                toolbarSubtitle.setVisibility(View.VISIBLE);
            } else {
                toolbarSubtitle.setVisibility(View.GONE);
            }
        }
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
