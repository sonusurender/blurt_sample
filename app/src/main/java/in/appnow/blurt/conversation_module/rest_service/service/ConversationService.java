package in.appnow.blurt.conversation_module.rest_service.service;

import android.content.Context;
import android.support.annotation.NonNull;

import in.appnow.blurt.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.blurt.conversation_module.rest_service.models.request.UserStatusRequest;
import in.appnow.blurt.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.blurt.conversation_module.rest_service.models.response.HandlerStatusResponse;
import in.appnow.blurt.interfaces.APICallback;
import in.appnow.blurt.rest.APIInterface;
import in.appnow.blurt.rest.request.BaseRequestModel;
import in.appnow.blurt.rest.request.TypingStatusRequest;
import in.appnow.blurt.rest.response.BaseResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sonu on 18/12/17.
 */

public class ConversationService {

    public static void sendMessage(final Context context, APIInterface apiInterface, ConversationResponse conversationModel, final APICallback apiCallback, final int requestCode) {

        Call<ConversationResponse> call = apiInterface.sendMessage(conversationModel);
        call.enqueue(new Callback<ConversationResponse>() {
            @Override
            public void onResponse(@NonNull Call<ConversationResponse> call, @NonNull Response<ConversationResponse> response) {
                apiCallback.onResponse(call, response, requestCode, conversationModel);
            }

            @Override
            public void onFailure(@NonNull Call<ConversationResponse> call, @NonNull Throwable t) {
                apiCallback.onFailure(call, t, requestCode, conversationModel);

            }
        });
    }

   public static void submitMessageStatus(final Context context, APIInterface apiInterface, UpdateMessageRequest updateMessageRequest, final APICallback apiCallback, final int requestCode) {

       Call<BaseResponseModel> call = apiInterface.updateMessageStatus(updateMessageRequest);
        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponseModel> call, @NonNull Response<BaseResponseModel> response) {
                apiCallback.onResponse(call, response, requestCode, updateMessageRequest);
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponseModel> call, @NonNull Throwable t) {
                apiCallback.onFailure(call, t, requestCode, updateMessageRequest);

            }
        });
    }

    public static void submitTypingStatus(final Context context, APIInterface apiInterface, TypingStatusRequest request, final APICallback apiCallback, final int requestCode) {


        Call<BaseResponseModel> call = apiInterface.updateTypingStatus(request);
        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponseModel> call, @NonNull Response<BaseResponseModel> response) {
                apiCallback.onResponse(call, response, requestCode, request);
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponseModel> call, @NonNull Throwable t) {
                apiCallback.onFailure(call, t, requestCode, request);

            }
        });
    }
    public static void submitUserStatus(final Context context, APIInterface apiInterface, UserStatusRequest request, final APICallback apiCallback, final int requestCode) {


        Call<BaseResponseModel> call = apiInterface.updateUserStatus(request);
        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponseModel> call, @NonNull Response<BaseResponseModel> response) {
                apiCallback.onResponse(call, response, requestCode, request);
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponseModel> call, @NonNull Throwable t) {
                apiCallback.onFailure(call, t, requestCode, request);

            }
        });
    }
    public static void getHandlerStatus(final Context context, APIInterface apiInterface, BaseRequestModel request, final APICallback apiCallback, final int requestCode) {


        Call<HandlerStatusResponse> call = apiInterface.getHandlerStatus(request);
        call.enqueue(new Callback<HandlerStatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<HandlerStatusResponse> call, @NonNull Response<HandlerStatusResponse> response) {
                apiCallback.onResponse(call, response, requestCode, request);
            }

            @Override
            public void onFailure(@NonNull Call<HandlerStatusResponse> call, @NonNull Throwable t) {
                apiCallback.onFailure(call, t, requestCode, request);

            }
        });
    }

   /* public static void fetchAllMessages(final Context context, int pageNumber, final APICallback apiCallback, final int requestCode) {

        if (!Blurt.getInstance().isInternetConnected(false)) {
            apiCallback.onNoNetwork(requestCode);
            return;
        }

        final FetchAllMessageRequest fetchAllMessageRequest = new FetchAllMessageRequest();
       // fetchAllMessageRequest.setCompanyId(Integer.parseInt(PreferenceManger.getCompanyId()));
        fetchAllMessageRequest.setUserId(PreferenceManger.getUserId());
        fetchAllMessageRequest.setPageNumber(pageNumber);

        Call<FetchAllMessageResponse> call = apiService.getAllMessages(fetchAllMessageRequest);
        call.enqueue(new Callback<FetchAllMessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchAllMessageResponse> call, @NonNull Response<FetchAllMessageResponse> response) {
                apiCallback.onResponse(call, response, requestCode, fetchAllMessageRequest);
            }

            @Override
            public void onFailure(@NonNull Call<FetchAllMessageResponse> call, @NonNull Throwable t) {
                apiCallback.onFailure(call, t, requestCode, fetchAllMessageRequest);

            }
        });
    }
*/
}
