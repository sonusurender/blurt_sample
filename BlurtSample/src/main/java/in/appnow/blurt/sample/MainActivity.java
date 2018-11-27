package in.appnow.blurt.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import in.appnow.blurt.user_auth.UserAuthActivity;

/**
 * Created by sonu on 14:24, 23/11/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,UserAuthActivity.class));
        finish();
    }
}
