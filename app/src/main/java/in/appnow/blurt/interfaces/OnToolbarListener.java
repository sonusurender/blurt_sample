package in.appnow.blurt.interfaces;

/**
 * Created by sonu on 17:34, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public interface OnToolbarListener {
    public void onBackButtonChange(boolean isEnable);

    public void onToolbarTitleChange(String title);

    public void onBackButtonPress();
}
