package in.appnow.blurt.user_auth.fragments.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.blurt.user_auth.UserAuthActivity;
import in.appnow.blurt.user_auth.fragments.login.mvp.LoginPresenter;
import in.appnow.blurt.user_auth.fragments.login.mvp.LoginView;

/**
 * Created by sonu on 18:47, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class LoginFragment extends Fragment {

    @Inject
    LoginView view;
    @Inject
    LoginPresenter presenter;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((UserAuthActivity) getActivity()).getComponent().inject(this);
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
}
