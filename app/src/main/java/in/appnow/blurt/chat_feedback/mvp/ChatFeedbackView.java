package in.appnow.blurt.chat_feedback.mvp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.blurt.R;
import in.appnow.blurt.R2;
import in.appnow.blurt.base.BaseView;
import in.appnow.blurt.utils.ImageUtils;
import in.appnow.blurt.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 17:28, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatFeedbackView extends FrameLayout implements BaseView {

    @BindView(R2.id.chat_feedback_message_label)
    TextView feedbackMessageLabel;
    @BindView(R2.id.feedback_rating_bar)
    RatingBar feedbackRating;
    @BindView(R2.id.enter_chat_feedback)
    EditText enterChatFeedback;
    @BindView(R2.id.chat_feedback_submit_button)
    Button submitButton;
    @BindString(R2.string.unknown_error)
    String unknownErrorString;

    public ChatFeedbackView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        inflate(appCompatActivity, R.layout.activity_chat_feedback, this);
        ButterKnife.bind(this);
    }

    public Observable<Object> observeSubmitButton() {
        return RxView.clicks(submitButton);
    }

    public void updateMessageLabel(String message) {
        feedbackMessageLabel.setText(message);
    }

    public String getChatFeedback() {
        return enterChatFeedback.getText().toString().trim();
    }

    public float getChatRating() {
        return feedbackRating.getRating();
    }

    public boolean isRatingSelected() {
        if (getChatRating() == 0) {
            ToastUtils.shortToast(getContext(),"Please select rating.");
            return false;
        }
        return true;
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
