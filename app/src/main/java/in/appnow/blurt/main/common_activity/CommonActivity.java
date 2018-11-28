package in.appnow.blurt.main.common_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.blurt.R;
import in.appnow.blurt.R2;
import in.appnow.blurt.app.Blurt;
import in.appnow.blurt.main.WebViewFragment;
import in.appnow.blurt.main.common_activity.dagger.CommonActivityComponent;
import in.appnow.blurt.main.common_activity.dagger.DaggerCommonActivityComponent;
import in.appnow.blurt.utils.FragmentUtils;

/**
 * Created by sonu on 15:26, 19/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class CommonActivity extends AppCompatActivity {

    private static final String ARG_TITLE = "title";
    private static final String ARG_FRAGMENT_TAG = "frg_tag";
    @BindView(R2.id.common_toolbar)
    Toolbar toolbar;

    private CommonActivityComponent component;

    public static void openCommonActivity(Context context, String title, String fragmentToReplace) {
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra(ARG_TITLE, title);
        intent.putExtra(ARG_FRAGMENT_TAG, fragmentToReplace);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = DaggerCommonActivityComponent.builder()
                .appComponent(Blurt.getInstance(this).component())
                // .commonActivityModule(new CommonActivityModule(this))
                .build();
        component.inject(this);

        setContentView(R.layout.activity_common);
        ButterKnife.bind(this);
        setUpToolbar();
        loadFragment();
    }

    public CommonActivityComponent getComponent() {
        return component;
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(view -> CommonActivity.super.onBackPressed());
        toolbar.setTitle(getIntent().getStringExtra(ARG_TITLE));
    }

    private void loadFragment() {
        if (getIntent() != null && getIntent().hasExtra(ARG_FRAGMENT_TAG)) {
            String fragmentTag = getIntent().getStringExtra(ARG_FRAGMENT_TAG);
            if (fragmentTag == null)
                return;
            if (fragmentTag.equalsIgnoreCase(FragmentUtils.WEB_VIEW_FRAGMENT)) {
                FragmentUtils.replaceFragmentCommon(getSupportFragmentManager(), R.id.common_frame_container, WebViewFragment.newInstance("https://www.astrobuddy.guru/privacy-policy.php", "", false), fragmentTag, false);
            }
        }
    }
}
