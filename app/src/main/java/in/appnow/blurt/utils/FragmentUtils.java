package in.appnow.blurt.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Sonu on 21/08/17.
 */

public class FragmentUtils {
    public static final String HOME_FRAGMENT = "home_fragment";
    public static final String HOROSCOPE_FRAGMENT = "horoscope_fragment";
    public static final String MY_ACCOUNT_FRAGMENT = "my_account_fragment";
    public static final String ADD_CREDIT_FRAGMENT = "add_credit_fragment";
    public static final String REFERRAL_CODE_FRAGMENT = "referral_code_fragment";
    public static final String MY_PROFILE_FRAGMENT = "my_profile_fragment";
    public static final String PROGRESS_DIALOG_FRAGMENT = "progress_dialog_fragment";
    public static final String FORGOT_PASSWORD_FRAGMENT = "forgot_password_fragment";
    public static final String HOROSCOPE_DETAIL_FRAGMENT = "horoscope_detail_fragment";
    public static final String LOGIN_FRAGMENT = "login_fragment";
    public static final String CHANGE_PASSWORD_FRAGMENT = "change_password_fragment";
    public static final String UPGRADE_PLAN_FRAGMENT = "upgrade_plan_fragment";
    public static final String WEB_VIEW_FRAGMENT = "web_view_fragment";
    public static final String CHAT_TOPICS_FRAGMENT = "chat_topics_fragment";
    public static final String ACCOUNT_HELP_FRAGMENT = "account_help_fragment";
    public static final String MYTH_BUSTER_FRAGMENT = "myth_buster_fragment";
    public static final String YOUR_CHART_FRAGMENT = "your_chart_fragment";
    public static final String CHAT_HISTORY_FRAGMENT = "chat_history_fragment";
    public static final String SINGLE_CHAT_HISTORY_FRAGMENT = "single_chat_history_fragment";
    public static final String CONTACT_US_FRAGMENT = "contact_us_fragment";
    public static final String OTP_FRAGMENT = "otp_fragment";
    public static final String SIGN_UP_FRAGMENT = "sign_up_fragment";


    public static void onChangeFragment(FragmentManager fragmentManager, int frameId, Fragment fragment, String tag) {
        FragmentUtils.replaceFragmentCommon(fragmentManager, frameId, fragment, tag, false);
    }

    public static void replaceFragmentCommon(FragmentManager fragmentManager, int containerID, Fragment fragment, String tag, boolean isAddtoBackStack) {
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);
        boolean fragmentPopped = fragmentManager
                .popBackStackImmediate(tag, 0);
        Logger.DebugLog("Fragment Utils", "Is popped Out : " + tag + " - " + fragmentPopped);
        if (!fragmentPopped && fragmentByTag == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(containerID, fragment, tag);
            if (isAddtoBackStack) {
                fragmentTransaction.addToBackStack(tag);
            }
            fragmentTransaction.commit();
        } else {
            int index = fragmentManager.getBackStackEntryCount() - 1;
            if (index >= 0) {
                FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
                String foundTag = backEntry.getName();
                Logger.ErrorLog("Replace", "TAG : " + foundTag);
                if (!tag.equals(HOME_FRAGMENT) && !tag.equals(foundTag))
                    fragmentManager.popBackStackImmediate(foundTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                else if (tag.equals(HOME_FRAGMENT))
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    public static void addFragmentCommon(FragmentManager fragmentManager, int containerID, Fragment fragment, String tag, boolean isAddtoBackStack) {
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);
        if (fragmentByTag == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().add(containerID, fragment, tag);
            if (isAddtoBackStack) {
                fragmentTransaction.addToBackStack(tag);
            }
            fragmentTransaction.commit();
        } else {
            int index = fragmentManager.getBackStackEntryCount() - 1;
            if (index >= 0) {
                FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
                String foundTag = backEntry.getName();
                Logger.ErrorLog("Replace", "TAG : " + foundTag);
                if (!tag.equals(HOME_FRAGMENT) && !tag.equals(foundTag))
                    fragmentManager.popBackStackImmediate(foundTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                else if (tag.equals(HOME_FRAGMENT))
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    public static void popFragmentFromBackStack(FragmentManager fragmentManager, String tag) {
        int index = fragmentManager.getBackStackEntryCount() - 1;
        if (index >= 0) {
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
            String foundTag = backEntry.getName();
            if (foundTag.equals(tag))
                fragmentManager.popBackStackImmediate();
        }
    }
}

