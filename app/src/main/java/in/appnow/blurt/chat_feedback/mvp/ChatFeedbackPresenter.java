package in.appnow.blurt.chat_feedback.mvp;


import in.appnow.blurt.app.AstroApplication;
import in.appnow.blurt.base.BasePresenter;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.rest.CallbackWrapper;
import in.appnow.blurt.rest.request.ChatFeedbackRequest;
import in.appnow.blurt.rest.response.BaseResponseModel;
import in.appnow.blurt.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 17:29, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatFeedbackPresenter implements BasePresenter {

    private final ChatFeedbackView view;
    private final ChatFeedbackModel model;
    private final PreferenceManger preferenceManger;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public ChatFeedbackPresenter(ChatFeedbackView view, ChatFeedbackModel model, PreferenceManger preferenceManger) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
    }

    @Override
    public void onCreate() {
        view.updateMessageLabel(model.getMessage());
        disposable.add(observeDoneButtonClick());
    }


    private Disposable observeDoneButtonClick() {
        return view.observeSubmitButton()
                .doOnNext(__ -> model.showProgress())
                .map(isValidate -> view.isRatingSelected() && AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isValidate -> {
                    if (isValidate) {
                        ChatFeedbackRequest request = new ChatFeedbackRequest(model.getSessionId(), (int) view.getChatRating(), view.getChatFeedback());
                        request.setUserId(preferenceManger.getUserDetails().getUserId());
                        return model.sendChatFeedback(request);
                    } else {
                        BaseResponseModel responseModel = new BaseResponseModel();
                        responseModel.setErrorStatus(true);
                        return Observable.just(responseModel);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgress())
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view,1) {
                    @Override
                    protected void onSuccess(BaseResponseModel data) {
                        if (data != null) {
                            if (!data.isErrorStatus()) {
                                preferenceManger.putPendingFeedback(null);
                                model.closeActivity();
                            }
                            ToastUtils.shortToast(data.getErrorMsg());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        disposable.clear();
    }
}
