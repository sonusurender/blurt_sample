package in.appnow.blurt.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;


import java.util.List;

import javax.inject.Inject;

import in.appnow.blurt.app.Blurt;
import in.appnow.blurt.base.BaseActivity;
import in.appnow.blurt.fcm.Config;
import in.appnow.blurt.fcm.NotificationUtils;
import in.appnow.blurt.interfaces.OnToolbarListener;
import in.appnow.blurt.main.dagger.DaggerMainActivityComponent;
import in.appnow.blurt.main.dagger.MainActivityComponent;
import in.appnow.blurt.main.dagger.MainActivityModule;
import in.appnow.blurt.main.mvp.MainActivityPresenter;
import in.appnow.blurt.main.mvp.MainActivityView;
import in.appnow.blurt.user_auth.UserAuthActivity;
import in.appnow.blurt.utils.FragmentUtils;


public class MainActivity extends BaseActivity implements OnToolbarListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject
    MainActivityView view;
    @Inject
    MainActivityPresenter presenter;

    private MainActivityComponent component;

    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = DaggerMainActivityComponent.builder()
                .appComponent(Blurt.getInstance(this).component())
                .mainActivityModule(new MainActivityModule(this))
                .build();
        component.inject(this);

        setContentView(view);
        presenter.onCreate();
        handleNotificationClick();

        UserAuthActivity.openUserAuthActivity(this);

    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    public MainActivityComponent getComponent() {
        return component;
    }

    @Override
    public void onBackPressed() {
        onBackButtonPressed();
    }

    private void onBackButtonPressed() {
        if (!view.isDrawerOpen()) {
            Fragment addCreditFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.ADD_CREDIT_FRAGMENT);
            Fragment upgradePlanFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.UPGRADE_PLAN_FRAGMENT);
            Fragment changePasswordFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.CHANGE_PASSWORD_FRAGMENT);
            Fragment accountHelpFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.ACCOUNT_HELP_FRAGMENT);
            Fragment chatHistoryFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.CHAT_HISTORY_FRAGMENT);
            Fragment singleChatHistoryFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.SINGLE_CHAT_HISTORY_FRAGMENT);
            if (chatHistoryFragment != null) {
                view.doReplaceFragment(1);
            } else if (upgradePlanFragment != null || addCreditFragment != null || accountHelpFragment != null) {
                view.doReplaceFragment(3);
            } else if (changePasswordFragment != null) {
                view.doReplaceFragment(4);
            } else {
                Fragment homeFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.HOROSCOPE_FRAGMENT);
                // Fragment chartFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.YOUR_CHART_FRAGMENT);
                if (homeFragment == null) {
                    // if (chartFragment != null) {
                    // }
                    view.doReplaceFragment(0);
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackButtonPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackButtonChange(boolean isEnable) {
        try {
            if (view != null)
                view.enableViews(isEnable);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onToolbarTitleChange(String title) {
        try {
            if (!TextUtils.isEmpty(title))
                view.setToolbarTitle(title);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onBackButtonPress() {
        onBackPressed();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleNotificationClick();
    }

    private void handleNotificationClick() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Config.MESSAGE_TYPE)) {
            String notificationType = intent.getStringExtra(Config.MESSAGE_TYPE);
            switch (notificationType) {
                case Config.PICK_REQUEST_PUSH:
                case Config.CHAT_MESSAGE_PUSH:
                    if (Blurt.getInstance(this).isInternetConnected(true))
                        presenter.startChat(false);
                    break;
                case Config.TRANSACTION_REPORT_PUSH:
                case Config.END_CHAT_PUSH:
                    //open my account page
                    view.doReplaceFragment(3);
                    break;

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        NotificationUtils.clearSingleNotification(this,Config.GENERAL_NOTIFICATION);
    }

}
