package in.appnow.blurt.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import in.appnow.blurt.interfaces.OnToolbarListener;

/**
 * Created by sonu on 12:05, 18/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class BaseFragment extends Fragment {

    private Context context;

    private OnToolbarListener onToolbarBackButtonListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            onToolbarBackButtonListener = (OnToolbarListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateToolbarTitle(String title) {
        if (onToolbarBackButtonListener != null)
            onToolbarBackButtonListener.onToolbarTitleChange(title);
    }

    public void showHideBackButton(boolean show) {
        if (onToolbarBackButtonListener != null) {
            onToolbarBackButtonListener.onBackButtonChange(show);
        }
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void forceBackPress() {
        if (onToolbarBackButtonListener != null) {
            onToolbarBackButtonListener.onBackButtonPress();
        }
    }
}
