package in.appnow.blurt.main.fragments.home.mvp;

import in.appnow.blurt.base.BasePresenter;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by sonu on 13:59, 18/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HomeFragmentPresenter implements BasePresenter {

    private final HomeFragmentView view;
    private final HomeFragmentModel model;
    private CompositeDisposable disposable = new CompositeDisposable();

    public HomeFragmentPresenter(HomeFragmentView view, HomeFragmentModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        disposable.clear();
    }
}
