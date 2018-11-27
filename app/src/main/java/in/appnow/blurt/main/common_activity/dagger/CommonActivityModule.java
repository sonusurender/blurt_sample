package in.appnow.blurt.main.common_activity.dagger;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;

/**
 * Created by sonu on 11:49, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
@Module
public class CommonActivityModule {

    private final AppCompatActivity appCompatActivity;

    public CommonActivityModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

}
