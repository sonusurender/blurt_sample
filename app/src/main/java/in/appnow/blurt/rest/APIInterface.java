package in.appnow.blurt.rest;

import in.appnow.blurt.conversation_module.rest_service.models.request.FetchMessageRequest;
import in.appnow.blurt.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.blurt.conversation_module.rest_service.models.request.UserStatusRequest;
import in.appnow.blurt.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.blurt.conversation_module.rest_service.models.response.FetchAllMessageResponse;
import in.appnow.blurt.conversation_module.rest_service.models.response.HandlerStatusResponse;
import in.appnow.blurt.rest.request.BaseRequestModel;
import in.appnow.blurt.rest.request.ChatFeedbackRequest;
import in.appnow.blurt.rest.request.CreateAccountRequest;
import in.appnow.blurt.rest.request.EndChatRequest;
import in.appnow.blurt.rest.request.FCMRequestModel;
import in.appnow.blurt.rest.request.StartChatRequest;
import in.appnow.blurt.rest.request.TypingStatusRequest;
import in.appnow.blurt.rest.request.UserAvailabilityRequest;
import in.appnow.blurt.rest.request.ValidateUserRequest;
import in.appnow.blurt.rest.response.BaseResponseModel;
import in.appnow.blurt.rest.response.ChatHistoryResponse;
import in.appnow.blurt.rest.response.LoginResponseModel;
import in.appnow.blurt.rest.response.StartChatResponse;
import in.appnow.blurt.rest.response.UpdateSocketIdRequest;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */

public interface APIInterface {

    @POST("droid/api/findUserAvailablity")
    Observable<BaseResponseModel> findUserAvailability(@Body UserAvailabilityRequest request);

    @POST("droid/api/validateUser")
    Observable<LoginResponseModel> validateUser(@Body ValidateUserRequest request);

    @POST("droid/api/createAccCust")
    Observable<LoginResponseModel> createCustomerAccount(@Body CreateAccountRequest request);

    @POST("droid/api/updateFcmKey")
    Observable<BaseResponseModel> updateFCMKey(@Body FCMRequestModel requestModel);

    @POST("droid/api/getHandlerID")
    Observable<BaseResponseModel> getHandlerId(@Body BaseRequestModel requestModel);


    /* Chat APIs */
    @POST("droid/api/startChat")
    Observable<StartChatResponse> startChat(@Body BaseRequestModel requestModel);

    @POST("droid/api/updateSocketId")
    Observable<BaseResponseModel> updateSocketId(@Body UpdateSocketIdRequest socketIdRequest);

    @POST("droid/api/send_message_client")
    Call<ConversationResponse> sendMessage(@Body ConversationResponse conversationResponse);

    @POST("droid/api/end_chat_client")
    Observable<BaseResponseModel> endChat(@Body EndChatRequest endChatRequest);

    @POST("abphp/api/v1/submit-chat-feedback.php")
    Observable<BaseResponseModel> submitChatFeedback(@Body ChatFeedbackRequest chatFeedbackRequest);

    @POST("droid/api/getAllmsgsBysessionId")
    Observable<FetchAllMessageResponse> getAllMessagesBySessionId(@Body FetchMessageRequest baseRequestModel);

    @POST("abphp/api/v1/get-chat-history.php")
    Observable<ChatHistoryResponse> getChatHistory(@Body BaseRequestModel requestModel);

    @POST("droid/api/update_message_status_client")
    Call<BaseResponseModel> updateMessageStatus(@Body UpdateMessageRequest request);

    @POST("droid/api/typing_status_user")
    Call<BaseResponseModel> updateTypingStatus(@Body TypingStatusRequest request);

    @POST("droid/api/user_avail_status")
    Call<BaseResponseModel> updateUserStatus(@Body UserStatusRequest request);

    @POST("droid/api/check_avail_status")
    Call<HandlerStatusResponse> getHandlerStatus(@Body BaseRequestModel requestModel);



}
