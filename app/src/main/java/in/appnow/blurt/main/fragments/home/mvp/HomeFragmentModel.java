package in.appnow.blurt.main.fragments.home.mvp;

import android.support.v7.app.AppCompatActivity;

import in.appnow.blurt.rest.APIInterface;

/**
 * Created by sonu on 13:59, 18/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HomeFragmentModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public HomeFragmentModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }
}
