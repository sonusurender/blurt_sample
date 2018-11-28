package in.appnow.blurt.user_auth.fragments.login.mvp;

import in.appnow.blurt.app.Blurt;
import in.appnow.blurt.base.BasePresenter;
import in.appnow.blurt.rest.CallbackWrapper;
import in.appnow.blurt.rest.request.UserAvailabilityRequest;
import in.appnow.blurt.rest.response.BaseResponseModel;
import in.appnow.blurt.utils.TextUtils;
import in.appnow.blurt.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 18:49, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class LoginPresenter implements BasePresenter {
    private final LoginView view;
    private LoginModel model;

    private CompositeDisposable disposable = new CompositeDisposable();

    public LoginPresenter(LoginView view, LoginModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        disposable.add(observeContinueButtonClick());
    }

    private Disposable observeContinueButtonClick() {
        return view.observeContinueButtonClick()
                .doOnNext(__ -> model.showProgressDialog())
                .map(isValidated -> TextUtils.isEmailIdValid(model.getAppCompatActivity(),view.getEmailId()) && Blurt.getInstance(model.getAppCompatActivity()).isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isValidated -> {
                    if (isValidated) {
                        UserAvailabilityRequest request = new UserAvailabilityRequest();
                        request.setEmailId(view.getEmailId());
                        return model.checkUserAvailability(request);
                    } else {
                        BaseResponseModel responseModel = new BaseResponseModel();
                        responseModel.setErrorStatus(true);
                        return Observable.just(responseModel);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.dismissProgressDialog())
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, LoginView.CHECK_USER_REQUEST_CODE) {
                    @Override
                    protected void onSuccess(BaseResponseModel baseResponseModel) {
                        if (baseResponseModel != null) {
                            ToastUtils.shortToast(model.getAppCompatActivity(),baseResponseModel.getErrorMsg());
                            if (!baseResponseModel.isErrorStatus()) {
                                model.replaceOTPFragment(view.getEmailId());
                            }
                        } else {
                            ToastUtils.shortToast(model.getAppCompatActivity(),"Server error. Please retry.");
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        disposable.clear();
    }
}
