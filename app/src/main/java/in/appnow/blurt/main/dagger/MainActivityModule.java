package in.appnow.blurt.main.dagger;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.blurt.dao.ABDatabase;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.main.fragments.home.mvp.HomeFragmentModel;
import in.appnow.blurt.main.fragments.home.mvp.HomeFragmentPresenter;
import in.appnow.blurt.main.fragments.home.mvp.HomeFragmentView;
import in.appnow.blurt.main.mvp.MainActivityModel;
import in.appnow.blurt.main.mvp.MainActivityPresenter;
import in.appnow.blurt.main.mvp.MainActivityView;
import in.appnow.blurt.rest.APIInterface;

/**
 * Created by sonu on 11:49, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
@Module
public class MainActivityModule {

    private final AppCompatActivity appCompatActivity;

    public MainActivityModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    /* Main Activity MVP Injection */
    @Provides
    @MainActivityScope
    public MainActivityView view(PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MainActivityView(appCompatActivity, preferenceManger, abDatabase);
    }

    @Provides
    @MainActivityScope
    public MainActivityModel mainActivityModel(APIInterface apiInterface) {
        return new MainActivityModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public MainActivityPresenter mainActivityPresenter(MainActivityView view, MainActivityModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MainActivityPresenter(view, model, preferenceManger, abDatabase);
    }

    @Provides
    @MainActivityScope
    public HomeFragmentView homeFragmentView() {
        return new HomeFragmentView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public HomeFragmentModel homeFragmentModel(APIInterface apiInterface) {
        return new HomeFragmentModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public HomeFragmentPresenter homeFragmentPresenter(HomeFragmentView view, HomeFragmentModel model) {
        return new HomeFragmentPresenter(view, model);
    }


}
