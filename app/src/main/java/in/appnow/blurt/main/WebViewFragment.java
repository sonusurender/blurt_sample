package in.appnow.blurt.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.appnow.blurt.R;
import in.appnow.blurt.base.BaseFragment;
import in.appnow.blurt.dialog.ProgressDialogFragment;


/**
 * Created by sonu on 11:25, 16/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class WebViewFragment extends BaseFragment {


    private static final String ARG_URL = "url";
    private static final String ARG_TITLE = "title";
    private static final String ARG_BACK_BUTTON = "back_button";

    private String url, title;
    private boolean isBackButtonShow;
    private Context context;

    public static WebViewFragment newInstance(String url, String title, boolean isBackButtonShow) {

        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_TITLE, title);
        args.putBoolean(ARG_BACK_BUTTON, isBackButtonShow);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
            title = getArguments().getString(ARG_TITLE);
            isBackButtonShow = getArguments().getBoolean(ARG_BACK_BUTTON, false);
            updateToolbarTitle(title);
            showHideBackButton(isBackButtonShow);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.web_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView webView = view.findViewById(R.id.web);
        // webView.loadUrl("file:///android_asset/load.html");
        webView.loadUrl(url);
        //webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ProgressDialogFragment.dismissProgress(((AppCompatActivity) context).getSupportFragmentManager());
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            ProgressDialogFragment.showProgress(((AppCompatActivity) context).getSupportFragmentManager());
        }

    }

    @Override
    public void onDetach() {
        if (isBackButtonShow)
            showHideBackButton(false);
        super.onDetach();
    }

}
