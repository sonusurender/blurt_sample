package in.appnow.blurt.user_auth.mvp;

import in.appnow.blurt.base.BasePresenter;
import in.appnow.blurt.helper.PreferenceManger;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by sonu on 18:17, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UserAuthPresenter implements BasePresenter {

    private final UserAuthView view;
    private final UserAuthModel model;
    private final PreferenceManger preferenceManger;
    private CompositeDisposable disposable = new CompositeDisposable();

    public UserAuthPresenter(UserAuthView view, UserAuthModel model, PreferenceManger preferenceManger) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
    }

    @Override
    public void onCreate() {
        if (preferenceManger.getUserDetails() != null) {
            model.replaceSignUpFragment();
        } else {
            model.replaceLoginFragment();
        }
    }
    public void onNotificationClick(){
        model.handleNotificationClick();
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }
}
