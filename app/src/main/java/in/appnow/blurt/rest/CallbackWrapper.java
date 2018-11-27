package in.appnow.blurt.rest;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;


import in.appnow.blurt.base.BaseView;
import in.appnow.blurt.rest.response.BaseResponseModel;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by sonu on 11:50, 07/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public abstract class CallbackWrapper<T extends BaseResponseModel> extends DisposableObserver<T> {
    //BaseView is just a reference of a View in MVP
    private WeakReference<BaseView> weakReference;
    private int requestCode;

    public CallbackWrapper(BaseView view, int requestCode) {
        this.weakReference = new WeakReference<>(view);
        this.requestCode = requestCode;
    }

    protected abstract void onSuccess(T t);

    @Override
    public void onNext(T t) {
        //You can return StatusCodes of different cases from your API and handle it here. I usually include these cases on BaseResponse and inherit it from every Response
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        BaseView view = weakReference.get();
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            view.onUnknownError(getErrorMessage(responseBody), requestCode);
        } else if (e instanceof SocketTimeoutException) {
            view.onTimeout(requestCode);
        } else if (e instanceof IOException) {
            view.onNetworkError(requestCode);
        } else {
            view.onUnknownError(e.getMessage(), requestCode);
        }
    }

    @Override
    public void onComplete() {

    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
