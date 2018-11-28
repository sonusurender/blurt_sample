package in.appnow.blurt.user_auth.fragments.otp.mvp;

import in.appnow.blurt.app.Blurt;
import in.appnow.blurt.base.BasePresenter;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.rest.CallbackWrapper;
import in.appnow.blurt.rest.RestUtils;
import in.appnow.blurt.rest.request.UserAvailabilityRequest;
import in.appnow.blurt.rest.request.ValidateUserRequest;
import in.appnow.blurt.rest.response.BaseResponseModel;
import in.appnow.blurt.rest.response.LoginResponseModel;
import in.appnow.blurt.rest.response.UserProfile;
import in.appnow.blurt.utils.TextUtils;
import in.appnow.blurt.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 18:55, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class OTPPresenter implements BasePresenter {
    private final OTPView view;
    private final OTPModel model;
    private final PreferenceManger preferenceManger;
    private String emailId;
    private CompositeDisposable disposable = new CompositeDisposable();

    public OTPPresenter(OTPView view, OTPModel model, PreferenceManger preferenceManger) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public void onCreate() {
        disposable.add(observeFirstInputTextChange());
        disposable.add(observeSecondInputTextChange());
        disposable.add(observeThirdInputTextChange());
        disposable.add(observeFourthInputTextChange());

        disposable.add(observeResendOTPButtonClick());
        disposable.add(observeLoginButtonClick());
    }

    private Disposable observeFirstInputTextChange() {
        return view.observeFirstInputTextChange().subscribe(text -> {
            view.changeFocus(1);
        });
    }

    private Disposable observeSecondInputTextChange() {
        return view.observeSecondInputTextChange().subscribe(text -> {
            view.changeFocus(2);
        });
    }

    private Disposable observeThirdInputTextChange() {
        return view.observeThirdInputTextChange().subscribe(text -> {
            view.changeFocus(3);
        });
    }

    private Disposable observeFourthInputTextChange() {
        return view.observeFourthInputTextChange().subscribe(text -> {
        });
    }


    private Disposable observeResendOTPButtonClick() {
        return view.observeResendOTPButtonClick()
                .doOnNext(__ -> model.showProgressDialog())
                .map(isValidated -> TextUtils.isEmailIdValid(model.getAppCompatActivity(),emailId) && Blurt.getInstance(model.getAppCompatActivity()).isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isValidated -> {
                    if (isValidated) {
                        UserAvailabilityRequest request = new UserAvailabilityRequest();
                        request.setEmailId(emailId);
                        return model.checkUserAvailability(request);
                    } else {
                        BaseResponseModel responseModel = new BaseResponseModel();
                        responseModel.setErrorStatus(true);
                        return Observable.just(responseModel);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.dismissProgressDialog())
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, OTPView.RESEND_OTP_REQUEST_CODE) {
                    @Override
                    protected void onSuccess(BaseResponseModel baseResponseModel) {
                        if (baseResponseModel != null) {
                            ToastUtils.shortToast(model.getAppCompatActivity(),baseResponseModel.getErrorMsg());
                            if (!baseResponseModel.isErrorStatus()) {
                                view.resetViews();
                            }
                        } else {
                            ToastUtils.shortToast(model.getAppCompatActivity(),"Server error. Please retry.");
                        }
                    }
                });
    }


    private Disposable observeLoginButtonClick() {
        return view.observeLoginButtonClick()
                .doOnNext(__ -> model.showProgressDialog())
                .map(isValidated -> view.validateOTP() && Blurt.getInstance(model.getAppCompatActivity()).isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isValidated -> {
                    if (isValidated) {
                        ValidateUserRequest request = new ValidateUserRequest();
                        request.setEmailId(emailId);
                        request.setOtp(view.getOTP());
                        request.setLicenseKey(RestUtils.getLicenseKey(model.getAppCompatActivity()));
                        return model.verifyOTP(request);
                    } else {
                        LoginResponseModel responseModel = new LoginResponseModel();
                        responseModel.setErrorStatus(true);
                        return Observable.just(responseModel);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.dismissProgressDialog())
                .subscribeWith(new CallbackWrapper<LoginResponseModel>(view, OTPView.VERIFY_OTP_REQUEST_CODE) {
                    @Override
                    protected void onSuccess(LoginResponseModel loginResponseModel) {
                        if (loginResponseModel != null) {
                            ToastUtils.shortToast(model.getAppCompatActivity(),loginResponseModel.getErrorMsg());
                            if (!loginResponseModel.isErrorStatus()) {
                                if (loginResponseModel.getUserProfile().getUserStatus().equalsIgnoreCase(UserProfile.NEW_USER)) {
                                    model.replaceSignUpFragment(true, emailId);
                                } else {
                                    preferenceManger.putUserDetails(loginResponseModel.getUserProfile());
                                    model.replaceSignUpFragment(false, emailId);
                                }
                            } else {
                                ToastUtils.longToast(model.getAppCompatActivity(),loginResponseModel.getErrorMsg());
                            }
                        } else {
                            ToastUtils.shortToast(model.getAppCompatActivity(),"Server error. Please retry.");
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }
}
