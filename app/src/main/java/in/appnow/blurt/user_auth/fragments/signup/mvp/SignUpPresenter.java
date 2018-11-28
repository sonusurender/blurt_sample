package in.appnow.blurt.user_auth.fragments.signup.mvp;

import android.support.annotation.Nullable;

import in.appnow.blurt.BuildConfig;
import in.appnow.blurt.app.Blurt;
import in.appnow.blurt.base.BasePresenter;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.rest.CallbackWrapper;
import in.appnow.blurt.rest.RestUtils;
import in.appnow.blurt.rest.request.BaseRequestModel;
import in.appnow.blurt.rest.request.CreateAccountRequest;
import in.appnow.blurt.rest.request.FCMRequestModel;
import in.appnow.blurt.rest.response.BaseResponseModel;
import in.appnow.blurt.rest.response.LoginResponseModel;
import in.appnow.blurt.rest.response.StartChatResponse;
import in.appnow.blurt.rest.response.UserProfile;
import in.appnow.blurt.utils.Constants;
import in.appnow.blurt.utils.LocaleUtils;
import in.appnow.blurt.utils.TextUtils;
import in.appnow.blurt.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 18:57, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SignUpPresenter implements BasePresenter {
    private final SignUpView view;
    private final SignUpModel model;
    private final PreferenceManger preferenceManger;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String emailId;

    public SignUpPresenter(SignUpView view, SignUpModel model, PreferenceManger preferenceManger) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
    }

    public void setUpData(boolean isNewUser, @Nullable String emailId) {
        this.emailId = emailId;
        view.updateViews(isNewUser, emailId, preferenceManger.getUserDetails());
    }

    @Override
    public void onCreate() {
        updateFCMToken();
        disposable.add(observeContinueButtonClick());
        disposable.add(observeStartChatButtonClick());
    }

    private Disposable observeContinueButtonClick() {
        return view.observeContinueButtonClick()
                .doOnNext(__ -> model.showProgressDialog())
                .map(isValidated -> TextUtils.isEmailIdValid(model.getAppCompatActivity(),view.getEmailId()) && view.isUserNameValid() && Blurt.getInstance(model.getAppCompatActivity()).isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isValidated -> {
                    if (isValidated) {
                        CreateAccountRequest request = new CreateAccountRequest();
                        request.setEmailId(view.getEmailId());
                        request.setName(view.getUserName());
                        request.setDeviceType(Constants.DEVICE_TYPE);
                        request.setAppVersion(BuildConfig.VERSION_NAME);
                        request.setLicenseKey(RestUtils.getLicenseKey(model.getAppCompatActivity()));
                        request.setLocale(LocaleUtils.prepareLocaleForServer(model.getAppCompatActivity()));
                        return model.createAccount(request);
                    } else {
                        LoginResponseModel loginResponseModel = new LoginResponseModel();
                        loginResponseModel.setErrorStatus(true);
                        return Observable.just(loginResponseModel);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.dismissProgressDialog())
                .subscribeWith(new CallbackWrapper<LoginResponseModel>(view, SignUpView.CREATE_ACCOUNT_REQUEST_CODE) {
                    @Override
                    protected void onSuccess(LoginResponseModel loginResponseModel) {
                        if (loginResponseModel != null) {
                            ToastUtils.shortToast(model.getAppCompatActivity(),loginResponseModel.getErrorMsg());
                            if (!loginResponseModel.isErrorStatus()) {
                                if (loginResponseModel.getUserProfile().getUserStatus().equalsIgnoreCase(UserProfile.USER_FOUND)) {
                                    preferenceManger.putUserDetails(loginResponseModel.getUserProfile());
                                    view.updateViews(false, emailId, loginResponseModel.getUserProfile());
                                    updateFCMToken();
                                }
                            }
                        } else {
                            ToastUtils.shortToast(model.getAppCompatActivity(),"Server error. Please retry.");
                        }
                    }
                });


    }

    private Disposable observeStartChatButtonClick() {
        return view.observeStartChatButtonClick()
                .doOnNext(__ -> model.showProgressDialog())
                .map(isValidated -> Blurt.getInstance(model.getAppCompatActivity()).isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isValidated -> {
                    if (isValidated) {
                        BaseRequestModel request = new BaseRequestModel();
                        request.setUserId(preferenceManger.getUserDetails().getUserId());
                        return model.startChat(request);
                    } else {
                        return Observable.just(null);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.dismissProgressDialog())
                .subscribeWith(new CallbackWrapper<StartChatResponse>(view, SignUpView.CREATE_ACCOUNT_REQUEST_CODE) {
                    @Override
                    protected void onSuccess(StartChatResponse startChatResponse) {
                        if (startChatResponse != null) {
                            if (!startChatResponse.isErrorStatus()) {
                                StartChatResponse.ChatResponse response = startChatResponse.getChatResponse();
                                model.openChatActivity(response.getChatSessionId(), response.isExistingChat());
                            }
                        } else {
                            ToastUtils.shortToast(model.getAppCompatActivity(),"Server error. Please retry.");
                        }
                    }
                });


    }

    public void doStartChat() {
        if (Blurt.getInstance(model.getAppCompatActivity()).isInternetConnected(true)) {
            disposable.add(observeStartChat());
        }
    }

    private Disposable observeStartChat() {
        model.showProgressDialog();
        BaseRequestModel request = new BaseRequestModel();
        request.setUserId(preferenceManger.getUserDetails().getUserId());
        return model.startChat(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.dismissProgressDialog())
                .subscribeWith(new CallbackWrapper<StartChatResponse>(view, SignUpView.CREATE_ACCOUNT_REQUEST_CODE) {
                    @Override
                    protected void onSuccess(StartChatResponse startChatResponse) {
                        if (startChatResponse != null) {
                            if (!startChatResponse.isErrorStatus()) {
                                StartChatResponse.ChatResponse response = startChatResponse.getChatResponse();
                                model.openChatActivity(response.getChatSessionId(), response.isExistingChat());
                            }
                        } else {
                            ToastUtils.shortToast(model.getAppCompatActivity(),"Server error. Please retry.");
                        }
                    }
                });


    }

    private void updateFCMToken() {
        if (preferenceManger.getUserDetails() != null && !preferenceManger.getBooleanValue(PreferenceManger.FCM_UPDATED) && Blurt.getInstance(model.getAppCompatActivity()).isInternetConnected(false)) {
            disposable.add(updateFCMTokenTask());
        }
    }

    private Disposable updateFCMTokenTask() {
        model.showProgressDialog();
        FCMRequestModel requestModel = new FCMRequestModel();
        requestModel.setUserId(preferenceManger.getUserDetails().getUserId());
        requestModel.setFcmToken(preferenceManger.getStringValue(PreferenceManger.FCM_TOKEN));
        requestModel.setRole("CUSTOMER");
        requestModel.setDeviceType(Constants.DEVICE_TYPE);
        return model.updateFCMToken(requestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.dismissProgressDialog())
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, SignUpView.UPDATE_TOKEN_REQUEST_CODE) {
                    @Override
                    protected void onSuccess(BaseResponseModel baseResponseModel) {
                        if (baseResponseModel != null) {
                            if (!baseResponseModel.isErrorStatus()) {
                                preferenceManger.putBoolean(PreferenceManger.FCM_UPDATED, true);
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }
}
