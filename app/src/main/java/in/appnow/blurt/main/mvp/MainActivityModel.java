package in.appnow.blurt.main.mvp;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import in.appnow.blurt.R;
import in.appnow.blurt.conversation_module.activity.ConversationActivity;
import in.appnow.blurt.dialog.DialogHelperClass;
import in.appnow.blurt.dialog.ProgressDialogFragment;
import in.appnow.blurt.models.NavigationModel;
import in.appnow.blurt.rest.APIInterface;
import in.appnow.blurt.rest.request.StartChatRequest;
import in.appnow.blurt.rest.response.StartChatResponse;
import in.appnow.blurt.utils.FragmentUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 11:03, 13/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MainActivityModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;
    private static final int ICONS[] = {R.drawable.ic_home_black_24dp, R.drawable.ic_person_black_24dp, R.drawable.ic_assignment_black_24dp, R.drawable.ic_settings_black_24dp, R.drawable.ic_perm_contact_calendar_black_24dp, R.drawable.ic_info_black_24dp};


    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public MainActivityModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public List<NavigationModel> getNavigationList() {
        String navigationMenus[] = appCompatActivity.getResources().getStringArray(R.array.navigation_menu);
        List<NavigationModel> navigationModelList = new ArrayList<>();

        for (int i = 0; i < navigationMenus.length; i++) {
            NavigationModel navigationModel = new NavigationModel(ICONS[i], navigationMenus[i], "");
            navigationModelList.add(navigationModel);
        }

        return navigationModelList;
    }

    public Observable<StartChatResponse> startChat(StartChatRequest model) {
        return apiInterface.startChat(model);
    }

    public void startConversationActivity(int currentQueue, String sessionId, boolean isExistingChat) {
       // ConversationActivity.openActivity(appCompatActivity, currentQueue, sessionId, isExistingChat);
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());
    }

}
