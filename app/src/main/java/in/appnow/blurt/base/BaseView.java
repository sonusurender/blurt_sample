package in.appnow.blurt.base;

/**
 * Created by sonu on 11:55, 07/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public interface BaseView {
    void onUnknownError(String error, int requestCode);

    void onTimeout(int requestCode);

    void onNetworkError(int requestCode);

    boolean isNetworkConnected(int requestCode);

    void onConnectionError(int requestCode);
}