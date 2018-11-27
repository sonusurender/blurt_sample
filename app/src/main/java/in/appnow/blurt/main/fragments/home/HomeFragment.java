package in.appnow.blurt.main.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.blurt.base.BaseFragment;
import in.appnow.blurt.main.MainActivity;
import in.appnow.blurt.main.fragments.home.mvp.HomeFragmentPresenter;
import in.appnow.blurt.main.fragments.home.mvp.HomeFragmentView;

/**
 * Created by sonu on 13:58, 18/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HomeFragment extends BaseFragment {

    @Inject
    HomeFragmentView view;
    @Inject
    HomeFragmentPresenter presenter;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            updateToolbarTitle("Home");
        } catch (Exception ignored) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getComponent().inject(this);
        presenter.onCreate();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
