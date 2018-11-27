package in.appnow.blurt.network;

import java.net.UnknownHostException;

import in.appnow.blurt.R;
import in.appnow.blurt.utils.ToastUtils;


/**
 * Created by sonu on 14:52, 30/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ResponseErrorHandler {

    private static final String TAG = ResponseErrorHandler.class.getSimpleName();


    public ResponseErrorHandler() {
    }

    public void onLoadingFailed(Throwable error, String defaultErrorMessage) {
        if (error instanceof UnknownHostException) {
            ToastUtils.shortToast(R.string.no_internet_connection);
        } else {
            ToastUtils.shortToast(defaultErrorMessage);
        }
    }
}
