package in.appnow.blurt.conversation_module.activity.mvp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import in.appnow.blurt.app.Blurt;
import in.appnow.blurt.base.BasePresenter;
import in.appnow.blurt.conversation_module.rest_service.models.request.FetchMessageRequest;
import in.appnow.blurt.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.blurt.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.blurt.conversation_module.utils.ConversationUtils;
import in.appnow.blurt.dao.ABDatabase;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.rest.CallbackWrapper;
import in.appnow.blurt.rest.RestUtils;
import in.appnow.blurt.rest.response.BaseResponseModel;
import in.appnow.blurt.rest.response.UpdateSocketIdRequest;
import in.appnow.blurt.utils.AppUtils;
import in.appnow.blurt.utils.Logger;
import in.appnow.blurt.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;

public class ConversationPresenter implements BasePresenter {
    private static final String TAG = ConversationPresenter.class.getSimpleName();
    private static final long QUEUE_HIDE_DELAY = 2000;
    private final ConversationActivityView view;
    private final ConversationModel model;
    private final PreferenceManger preferenceManger;
    private final ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private boolean typingStarted;
    private boolean isChatMessageStatusUpdated;
    private boolean isConversationGoingOn;
    private boolean isChatMessageFetched;
    private boolean isAgentJoined;
    private String agentId = "";

    public ConversationPresenter(ConversationActivityView view, ConversationModel conversationModel, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = conversationModel;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        setUpSocket();
        disposable.add(observerTextChange());
        disposable.add(observeEmojiButtonClick());
        disposable.add(observeSendButtonClick());
        disposable.add(observeBackArrowPress());
        onEditTextFocusListener();
    }

    private Disposable observerTextChange() {
        return view.observeTextChange().subscribe(text -> {
            text = text.toString().trim();
            view.onChatTextChange(text);
            if (!TextUtils.isEmpty(text) && text.length() == 1) {
                updateTypingStatus(ConversationUtils.TYPING_STATUS);
            } else if (TextUtils.isEmpty(text) && text.length() == 0 && typingStarted) {
                updateTypingStatus(ConversationUtils.NO_TYPING_STATUS);
            }
        });
    }

    private void onEditTextFocusListener() {
        view.typeMessage.setOnFocusChangeListener((view, b) -> {
            if (!b && typingStarted) {
                updateTypingStatus(ConversationUtils.NO_TYPING_STATUS);
            }
        });
    }

    private Disposable observeEmojiButtonClick() {
        return view.observeEmojiButton().subscribe(__ -> view.toggleEmojiPopup());
    }

    private Disposable observeBackArrowPress() {
        return view.observeBackArrowPress().subscribe(__ -> model.onBackArrowPress());
    }

    private Disposable observeSendButtonClick() {
        return view.observeSendButton()
                .map(isValidate -> !TextUtils.isEmpty(view.getTypedMessage()) && Blurt.getInstance(model.getAppCompatActivity()).isInternetConnected(true))
                .subscribe(isValidate -> {
                    if (isValidate) {
                        long timeStamp = System.currentTimeMillis();

                        ConversationResponse response = new ConversationResponse();
                        response.setMessage(view.getTypedMessage());
                        response.setMessageStatus(ConversationUtils.MESSAGE_SEND);
                        response.setSendDateTime(timeStamp);
                        response.setSessionId(model.getSessionId());
                        response.setSenderId(preferenceManger.getUserDetails().getUserId());

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("msg", view.getTypedMessage());
                        jsonObject.put("timestamp", timeStamp);
                        jsonObject.put("chat_id", model.getSessionId());
                        jsonObject.put("sentBy", preferenceManger.getUserDetails().getUserId());
                        jsonObject.put("status", ConversationUtils.MESSAGE_SENT);
                        jsonObject.put("notify", agentId);

                        view.updateTypeMessage("");

                        Logger.DebugLog(TAG, "Send message : " + jsonObject.toString());

                        mSocket.emit(EVENT_SEND_MESSAGE, jsonObject, new Ack() {
                            @Override
                            public void call(Object... args) {
                                Logger.DebugLog(TAG, "Message Send : " + args[0]);
                            }
                        });

                        AsyncTask.execute(() -> abDatabase.conversationDao().insert(response));

                    }
                });
    }

    @SuppressLint("StaticFieldLeak")
    private void addWelcomeMessage() {
        ConversationResponse conversationModel = new ConversationResponse();
        conversationModel.setMessage(AppUtils.getWelcomeMessage(model.getAppCompatActivity()));
        conversationModel.setMessageId(1);
        conversationModel.setMessageStatus(ConversationUtils.MESSAGE_SEND);
        conversationModel.setSenderId("0");//receiver Id
        conversationModel.setSessionId(model.getSessionId());

        conversationModel.setSendDateTime(System.currentTimeMillis());

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                model.insertData(conversationModel);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();


    }

    @Override
    public void onDestroy() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off(EVENT_NOTIFICATION, onHandlerCallback);
            mSocket.off(EVENT_MESSAGES, onNewMessage);
        }
        disposable.clear();
    }


    private void fetchAllChatMessages() {
        //  this.conversationViewModel = conversationViewModel;
        model.getAllMessages().observe(model.getAppCompatActivity(), conversationResponses -> {

            if (conversationResponses == null) {
                return;
            }

            if (conversationResponses.isEmpty()) {
                //fetchData(topicItemViewModel);
                if (isConversationGoingOn) {
                    //End chat happened
                    isConversationGoingOn = false;
                   // model.close();
                     model.openChatFeedbackActivity(model.getSessionId());
                    return;
                }
                addWelcomeMessage();

            } else {
                isConversationGoingOn = true;
                view.setData(conversationResponses, preferenceManger);
                //checkIfAnyMessageStatusIsNotRead(conversationResponses);
                // model.updateEndChatMenu(!view.isChatEmpty());
            }
            // }
        });
    }


    public boolean onBackPress() {
        return view.dismissEmojiPopup();
    }

    public void openChatFeedbackScreen() {
        model.openChatFeedbackActivity(model.getSessionId());
    }


    private Disposable chatTopLabelHideDelay() {
        return Observable.timer(QUEUE_HIDE_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> {
                    view.showHideQueueLabel(View.GONE);
                });

    }

    /* =====   Update queue label Methods ENDS!!!!  ===== */


    public void updateTypingStatus(int typingStatus) {
        typingStarted = typingStatus == ConversationUtils.TYPING_STATUS;
        if (mSocket != null) {
            try {
                String typing = typingStatus == ConversationUtils.TYPING_STATUS ? "TYPING" : "NOT_TYPING";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("chat_id", model.getSessionId());
                jsonObject.put("TYPE", typing);
                jsonObject.put("sender", preferenceManger.getUserDetails().getUserId());
                mSocket.emit(EVENT_NOTIFICATION, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void checkIfAnyMessageStatusIsNotRead(List<ConversationResponse> conversationResponses) {
        if (isChatMessageStatusUpdated)
            return;
        AsyncTask.execute(() -> {
            isChatMessageStatusUpdated = true;
            if (conversationResponses != null && conversationResponses.size() > 0) {
                UpdateMessageRequest request = new UpdateMessageRequest();
                List<Long> messageIds = new ArrayList<>();
                for (ConversationResponse response : conversationResponses) {
                    if (response != null) {
                        if ((TextUtils.isEmpty(response.getMessageStatus()) || response.getMessageStatus().equalsIgnoreCase(ConversationUtils.MESSAGE_RECEIVED) || response.getMessageStatus().equalsIgnoreCase(ConversationUtils.MESSAGE_SENT))) {
                            messageIds.add(response.getMessageId());
                        }
                    }
                }
                if (messageIds.size() > 0) {
                    request.setMessageIds(messageIds);
                    request.setUserId(preferenceManger.getUserDetails().getUserId());
                    request.setSessionId(model.getSessionId());
                    request.setMessageStatus(ConversationUtils.MESSAGE_READ);
                    model.updateMessageStatus(request);
                }
            }
        });

    }

    private static final String EVENT_START_CHAT_ROOM = "startChatRoom";
    private static final String EVENT_SEND_MESSAGE = "send_msg";
    private static final String EVENT_NOTIFICATION = "notification";
    private static final String EVENT_MESSAGES = "messages";

    private Socket mSocket;

    {
        try {
            @SuppressLint("BadHostnameVerifier")
            HostnameVerifier hostnameVerifier = (hostname, session) -> true;
            @SuppressLint("TrustAllX509TrustManager")
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};
            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();

            IO.Options opts = new IO.Options();
            opts.timeout = -1;
            //opts.hostnameVerifier = hostnameVerifier;
            //opts.sslContext = sslContext;
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            mSocket = IO.socket(RestUtils.getEndPoint(), opts);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpSocket() {

        mSocket.on(Socket.EVENT_PING, args -> {
            if (args.length > 0) {
                JSONObject data = (JSONObject) args[0];
                String ping;
                try {
                    ping = data.getString("beat");
                } catch (JSONException e) {
                    return;
                }
                if (ping.equals("1")) {
                    mSocket.emit(Socket.EVENT_PONG, "pong");
                }
            }
        });

        mSocket.on(Socket.EVENT_CONNECT, args -> disposable.add(updateSocketId(mSocket.id())));
        mSocket.on(Socket.EVENT_ERROR, args -> Logger.ErrorLog(TAG, "EVENT ERROR : " + args[0].toString()));

        mSocket.on(Socket.EVENT_RECONNECT, args -> Logger.ErrorLog(TAG, "EVENT_RECONNECT: " + args[0].toString()));
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> Logger.ErrorLog(TAG, "EVENT_CONNECT_TIMEOUT: " + args[0].toString()));

        mSocket.on(Socket.EVENT_RECONNECT_ERROR, args -> Logger.ErrorLog(TAG, "EVENT_RECONNECT_ERROR: " + args[0].toString()));

        mSocket.on(Socket.EVENT_RECONNECT_FAILED, args -> Logger.ErrorLog(TAG, "EVENT_RECONNECT_FAILED: " + args[0].toString()));

        mSocket.on(EVENT_NOTIFICATION, onHandlerCallback);
        mSocket.on(EVENT_MESSAGES, onNewMessage);
        mSocket.connect();

    }

    private void joinChatRoom() {
        Object[] obj = new Object[]{model.getSessionId(), preferenceManger.getUserDetails().getUserId()};
        mSocket.emit(EVENT_START_CHAT_ROOM, obj, args -> Logger.DebugLog(TAG, "START CHAT ROOM : " + args[0].toString()));
    }

    private Emitter.Listener onHandlerCallback = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.DebugLog(TAG, "EVENT NOTIFICATION : " + args[0].toString());
            try {
                JSONObject data = (JSONObject) args[0];
                String sessionId = "", type = "", sender = "";
                if (data.has("chat_id")) {
                    sessionId = data.getString("chat_id");
                }
                if (data.has("TYPE")) {
                    type = data.getString("TYPE");
                }
                if (data.has("sender")) {
                    sender = data.getString("sender");
                }
                String finalType = type;
                String finalSender = sender;
                model.getAppCompatActivity().runOnUiThread(() -> {
                    switch (finalType) {
                        case "WAITING_TO_JOIN":
                            if (!isAgentJoined) {
                                view.updateQueueLabelText("Waiting to join by handler.");
                            }
                            break;
                        case "AGENT_JOINED":
                            if (!isAgentJoined) {
                                view.updateQueueLabelText("Agent joined.");
                                disposable.add(chatTopLabelHideDelay());
                                isAgentJoined = true;
                            }
                            String agent_Id = "";
                            if (data.has("agent_id")) {
                                try {
                                    agent_Id = data.getString("agent_id");
                                    agentId = agent_Id;
                                }
                                catch (Exception ignored){

                                }
                            }
                            view.setHandlerStatus("Online");
                            break;
                        case "USER_DISCONNECTED":
                            view.setHandlerStatus("Offline");
                            break;
                        case "TYPING":
                            if (isCurrentUser(finalSender)) {
                                view.updateTypingStatus(ConversationUtils.TYPING_STATUS);
                            }
                            break;
                        case "NOT_TYPING":
                            if (isCurrentUser(finalSender)) {
                                view.updateTypingStatus(ConversationUtils.NO_TYPING_STATUS);
                            }
                            break;
                        case "END_CHAT":
                            AsyncTask.execute(() -> abDatabase.conversationDao().deleteChatTable());
                            ToastUtils.longToast(model.getAppCompatActivity(),"Chat finished");
                            break;
                    }
                });


            } catch (Exception e) {
            }
        }
    };

    private boolean isCurrentUser(String senderId) {
        return !senderId.equalsIgnoreCase(preferenceManger.getUserDetails().getUserId());
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.DebugLog(TAG, "EVENT NEW MESSAGE : " + args[0].toString());
            try {
                JSONObject data = (JSONObject) args[0];
                String sessionId = "", message = "", sender = "";
                long timeStamp = 0;
                if (data.has("chat_id")) {
                    sessionId = data.getString("chat_id");
                }
                if (data.has("msg")) {
                    message = data.getString("msg");
                }
                if (data.has("timestamp")) {
                    timeStamp = data.getLong("timestamp");
                }
                if (data.has("sentBy")) {
                    sender = data.getString("sentBy");
                }
                if (!sender.equalsIgnoreCase(preferenceManger.getUserDetails().getUserId())) {
                    ConversationResponse response = new ConversationResponse();
                    response.setMessage(message);
                    response.setMessageStatus(ConversationUtils.MESSAGE_RECEIVED);
                    response.setSendDateTime(timeStamp);
                    response.setSessionId(sessionId);
                    response.setSenderId(sender);
                    AsyncTask.execute(() -> abDatabase.conversationDao().insert(response));
                } else {
                    long finalTimeStamp = timeStamp;
                    AsyncTask.execute(() -> abDatabase.conversationDao().updateMessageStatusForTimeStamp(ConversationUtils.MESSAGE_SENT, finalTimeStamp));

                }
            } catch (Exception ignored) {
            }
        }
    };

    private Disposable updateSocketId(String socketId) {
        UpdateSocketIdRequest request = new UpdateSocketIdRequest();
        request.setUserId(preferenceManger.getUserDetails().getUserId());
        request.setSocketId(socketId);
        return model.updateSocketId(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, 1) {
                    @Override
                    protected void onSuccess(BaseResponseModel data) {
                            joinChatRoom();
                            if (!isChatMessageFetched) {
                                fetchAllChatMessages();
                                FetchMessageRequest requestModel = new FetchMessageRequest();
                                requestModel.setSessionId(model.getSessionId());
                                model.getAllMessagesFromServer(requestModel);
                                isChatMessageFetched = true;
                            }
                        }
                });
    }


}
