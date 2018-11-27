package in.appnow.blurt.user_auth.fragments.otp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.blurt.user_auth.UserAuthActivity;
import in.appnow.blurt.user_auth.fragments.otp.mvp.OTPPresenter;
import in.appnow.blurt.user_auth.fragments.otp.mvp.OTPView;

/**
 * Created by sonu on 18:50, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class OTPFragment extends Fragment {

    private static final String ARG_EMAIL = "email_id";

    private String emailId;

    @Inject
    OTPView view;
    @Inject
    OTPPresenter presenter;

    public static OTPFragment newInstance(String emailId) {

        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, emailId);
        OTPFragment fragment = new OTPFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emailId = getArguments().getString(ARG_EMAIL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((UserAuthActivity) getActivity()).getComponent().inject(this);
        presenter.setEmailId(emailId);
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
