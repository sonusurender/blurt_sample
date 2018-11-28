package in.appnow.blurt.main.mvp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.appnow.blurt.BuildConfig;
import in.appnow.blurt.R;
import in.appnow.blurt.R2;
import in.appnow.blurt.adapters.NavigationListAdapter;
import in.appnow.blurt.base.BaseView;
import in.appnow.blurt.dao.ABDatabase;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.main.MainActivity;
import in.appnow.blurt.main.fragments.home.HomeFragment;
import in.appnow.blurt.models.NavigationModel;
import in.appnow.blurt.rest.response.UserProfile;
import in.appnow.blurt.utils.FragmentUtils;
import in.appnow.blurt.utils.ImageUtils;
import in.appnow.blurt.utils.ToastUtils;

/**
 * Created by sonu on 12:59, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MainActivityView extends FrameLayout implements BaseView, AdapterView.OnItemClickListener {
    private static final String TAG = MainActivityView.class.getSimpleName();
    private ActionBarDrawerToggle toggle;
    @BindView(R2.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R2.id.am_drawer)
    DrawerLayout drawerLayout;
    @BindView(R2.id.am_navigation)
    NavigationView navigationView;
    @BindView(R2.id.side_menu_list_view)
    ListView sideMenuListView;
    @BindView(R2.id.app_version_label)
    TextView appVersionLabel;
    @BindView(R2.id.image_view)
    ImageView bgImageView;

    @BindString(R2.string.unknown_error)
    String unknownErrorString;

    private final AppCompatActivity appCompatActivity;
    private FragmentManager fragmentManager;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    private final PreferenceManger preferenceManger;
    private final ABDatabase abDatabase;

    private final NavigationListAdapter adapter = new NavigationListAdapter(getContext());

    public MainActivityView(@NonNull AppCompatActivity appCompatActivity, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
        inflate(appCompatActivity, R.layout.activity_main, this);
        ButterKnife.bind(this);
        ImageUtils.setDrawableImage(appCompatActivity, bgImageView, R.drawable.background);
        fragmentManager = appCompatActivity.getSupportFragmentManager();
        setUpNavigationView();
        appCompatActivity.getSupportActionBar().setTitle("ASTROBUDDY");
        doReplaceFragment(0);

        sideMenuListView.setOnItemClickListener(this);

        appVersionLabel.setText("Version : " + BuildConfig.VERSION_NAME);
    }

    private void setUpNavigationView() {
        appCompatActivity.setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(appCompatActivity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        sideMenuListView.setAdapter(adapter);
    }

    public void setUpNavigationList(List<NavigationModel> navigationModelList) {
        adapter.swapData(navigationModelList);
    }

    public void doReplaceFragment(int position) {
        isDrawerOpen();
        updateAdapterSelection(position);
        switch (position) {
            case 0:
                //Home
                replaceFragment(HomeFragment.newInstance(), FragmentUtils.HOROSCOPE_FRAGMENT);
                break;

        }
    }

    @SuppressLint("StaticFieldLeak")
    private void doLogout() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                //delete chat table
                abDatabase.conversationDao().deleteChatTable();

                //clear use session
                preferenceManger.logoutUser();

                //clear tip of the day
                preferenceManger.putString(PreferenceManger.TIP_OF_THE_DAY, "");

                preferenceManger.putPendingFeedback(null);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ToastUtils.shortToast(appCompatActivity,"Logout Success.");
                // appCompatActivity.startActivity(new Intent(appCompatActivity, LoginActivity.class));
                appCompatActivity.finish();
            }
        }.execute();
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        FragmentUtils.onChangeFragment(fragmentManager, R.id.container_view, fragment, fragmentTag);
    }


    public void updateAdapterSelection(int position) {
        adapter.setSelectedPosition(position);
    }

    public boolean isDrawerOpen() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    public void enableViews(boolean enable) {
        try {

            // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
            // when you enable on one, you disable on the other.
            // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
            if (enable) {
                // Remove hamburger
                toggle.setDrawerIndicatorEnabled(false);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                // Show back button
                appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
                // clicks are disabled i.e. the UP button will not work.
                // We need to add a listener, as in below, so DrawerToggle will forward
                // click events to this listener.
                if (!mToolBarNavigationListenerIsRegistered) {
                    toggle.setToolbarNavigationClickListener(v -> {
                        // Doesn't have to be onBackPressed
                        appCompatActivity.onBackPressed();
                    });

                    mToolBarNavigationListenerIsRegistered = true;
                }

            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                // Remove back button
                appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                // Show hamburger
                toggle.setDrawerIndicatorEnabled(true);
                // Remove the/any drawer toggle listener
                toggle.setToolbarNavigationClickListener(null);
                mToolBarNavigationListenerIsRegistered = false;
            }
        } catch (Exception e) {
        }

        // So, one may think "Hmm why not simplify to:
        // .....
        // getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        // mDrawer.setDrawerIndicatorEnabled(!enable);
        // ......
        // To re-iterate, the order in which you enable and disable views IS important #dontSimplify.
    }

    public void setToolbarTitle(String title) {
        try {
            if (toolbar == null || TextUtils.isEmpty(title))
                return;
            toolbar.setTitle(title);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        doReplaceFragment(i);
    }

    @Override
    public void onUnknownError(String error, int requestCode) {

    }

    @Override
    public void onTimeout(int requestCode) {

    }

    @Override
    public void onNetworkError(int requestCode) {

    }

    @Override
    public boolean isNetworkConnected(int requestCode) {
        return false;
    }

    @Override
    public void onConnectionError(int requestCode) {

    }
}
