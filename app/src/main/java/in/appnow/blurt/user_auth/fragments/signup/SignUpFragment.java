package in.appnow.blurt.user_auth.fragments.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.blurt.base.BaseFragment;
import in.appnow.blurt.main.MainActivity;
import in.appnow.blurt.user_auth.UserAuthActivity;
import in.appnow.blurt.user_auth.fragments.signup.mvp.SignUpPresenter;
import in.appnow.blurt.user_auth.fragments.signup.mvp.SignUpView;

/**
 * Created by sonu on 18:56, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SignUpFragment extends Fragment implements UserAuthActivity.OnMessageNotificationListener {

    private static final String ARG_EMAIL_ID = "email_id";
    private static final String ARG_IS_NEW_USER = "is_new_user";
    @Inject
    SignUpView view;
    @Inject
    SignUpPresenter presenter;

    private String emailId;
    private boolean isNewUser;

    public static SignUpFragment newInstance(boolean isNewUser, String emailId) {

        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(emailId))
            args.putString(ARG_EMAIL_ID, emailId);
        args.putBoolean(ARG_IS_NEW_USER, isNewUser);
        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_EMAIL_ID))
                emailId = getArguments().getString(ARG_EMAIL_ID);
            isNewUser = getArguments().getBoolean(ARG_IS_NEW_USER, true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((UserAuthActivity) getActivity()).getComponent().inject(this);
        presenter.setUpData(isNewUser, emailId);
        presenter.onCreate();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void onNotificationClick() {
        if (presenter != null)
            presenter.doStartChat();
    }
}
