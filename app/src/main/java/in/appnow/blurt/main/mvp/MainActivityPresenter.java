package in.appnow.blurt.main.mvp;

import in.appnow.blurt.app.AstroApplication;
import in.appnow.blurt.base.BasePresenter;
import in.appnow.blurt.conversation_module.activity.mvp.ConversationPresenter;
import in.appnow.blurt.dao.ABDatabase;
import in.appnow.blurt.dialog.DialogHelperClass;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.rest.CallbackWrapper;
import in.appnow.blurt.rest.request.StartChatRequest;
import in.appnow.blurt.rest.response.StartChatResponse;
import in.appnow.blurt.utils.ToastUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 11:03, 13/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MainActivityPresenter implements BasePresenter {

    private static final String TAG = ConversationPresenter.class.getSimpleName();
    private static final long SIDE_MENU_DELAY = 2000;
    private final MainActivityView view;
    private final MainActivityModel model;
    private final PreferenceManger preferenceManger;
    private final ABDatabase abDatabase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public MainActivityPresenter(MainActivityView view, MainActivityModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        setNavigationAdapter();
    }

    private Disposable openChatActivity(boolean isNotify) {
        model.showProgressBar();
        StartChatRequest requestModel = new StartChatRequest();
        requestModel.setNotifyUser(isNotify);
        requestModel.setUserId(preferenceManger.getUserDetails().getUserId());
        return model.startChat(requestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<StartChatResponse>(view,1) {
                    @Override
                    protected void onSuccess(StartChatResponse data) {
                        if (data != null) {
                            if (isNotify) {
                                ToastUtils.shortToast(data.getErrorMsg());
                            } else {
                                if (!data.isErrorStatus()) {

                                } else {
                                    ToastUtils.shortToast(data.getErrorMsg());
                                }
                            }
                        }
                    }
                });
    }

    private void showNotifyMeDialog(String message) {
        DialogHelperClass.showMessageOKCancel(model.getAppCompatActivity(), message, "Yes", "No", (dialogInterface, i) -> {
            if (AstroApplication.getInstance().isInternetConnected(true)) {
                startChat(true);
            }

        }, null);
    }


    public void startChat(boolean isNotify) {
        disposable.add(openChatActivity(isNotify));
    }

    private void setNavigationAdapter() {
        view.setUpNavigationList(model.getNavigationList());
    }


    @Override
    public void onDestroy() {
        disposable.clear();
    }
}
