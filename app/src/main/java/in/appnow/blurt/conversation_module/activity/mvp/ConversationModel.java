package in.appnow.blurt.conversation_module.activity.mvp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import in.appnow.blurt.chat_feedback.ChatFeedbackActivity;
import in.appnow.blurt.conversation_module.activity.ConversationActivity;
import in.appnow.blurt.conversation_module.background_service.ConversationIntentService;
import in.appnow.blurt.conversation_module.rest_service.models.request.FetchMessageRequest;
import in.appnow.blurt.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.blurt.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.blurt.conversation_module.utils.ConversationUtils;
import in.appnow.blurt.dao.viewmodel.ConversationViewModel;
import in.appnow.blurt.dialog.ProgressDialogFragment;
import in.appnow.blurt.rest.APIInterface;
import in.appnow.blurt.rest.request.EndChatRequest;
import in.appnow.blurt.rest.request.TypingStatusRequest;
import in.appnow.blurt.rest.response.BaseResponseModel;
import in.appnow.blurt.rest.response.UpdateSocketIdRequest;
import in.appnow.blurt.utils.KeyboardUtils;
import io.reactivex.Observable;

public class ConversationModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;
    private ConversationViewModel conversationViewModel;

    public ConversationModel(AppCompatActivity appCompatActivity, APIInterface apiInterface, ViewModelProvider.Factory viewModeFactory) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
        //instantiate view model
        conversationViewModel = ViewModelProviders.of(appCompatActivity, viewModeFactory).get(ConversationViewModel.class);

    }

    public void getAllMessagesFromServer(FetchMessageRequest requestModel) {
        conversationViewModel.fetchConversationFromServer(requestModel);
    }

    public LiveData<List<ConversationResponse>> getAllMessages() {
        return conversationViewModel.fetchAllConversation(getSessionId());
    }

    public long insertData(ConversationResponse response) {
        return conversationViewModel.insert(response);
    }


    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void onBackArrowPress() {
        KeyboardUtils.hideSoftKeyboard(appCompatActivity);
        appCompatActivity.finish();
    }

    public boolean isExistingChat() {
        return appCompatActivity.getIntent().getBooleanExtra(ConversationActivity.EXTRA_EXISTING_CHAT, false);
    }

    public String getSessionId() {
        return appCompatActivity.getIntent().getStringExtra(ConversationActivity.EXTRA_SESSION_ID);
    }

    public Observable<BaseResponseModel> endCurrentChat(EndChatRequest request) {
        return apiInterface.endChat(request);
    }


    public Observable<BaseResponseModel> updateSocketId(UpdateSocketIdRequest request) {
        return apiInterface.updateSocketId(request);
    }


    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }

    public void openChatFeedbackActivity(String sessionId) {
        ChatFeedbackActivity.openChatFeedbackActivity(appCompatActivity, sessionId,true,"Chat successfully done.");
    }

    public void updateMessageStatus(UpdateMessageRequest updateMessageRequest) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConversationUtils.MESSAGE_STATUS_MODEL, updateMessageRequest);
        Intent intent = new Intent(appCompatActivity, ConversationIntentService.class);
        intent.setAction(ConversationIntentService.MESSAGE_STATUS);
        intent.putExtras(bundle);
        appCompatActivity.startService(intent);
    }

    public void close() {
        appCompatActivity.finish();
    }
}
