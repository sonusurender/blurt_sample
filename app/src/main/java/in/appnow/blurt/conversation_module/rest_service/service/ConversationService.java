package in.appnow.blurt.conversation_module.rest_service.service;

import android.content.Context;
import android.support.annotation.NonNull;

import in.appnow.blurt.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.blurt.interfaces.APICallback;
import in.appnow.blurt.rest.APIInterface;
import in.appnow.blurt.rest.response.BaseResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sonu on 18/12/17.
 */

public class ConversationService {

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
}
