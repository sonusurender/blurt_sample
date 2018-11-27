package in.appnow.blurt.dao.repository.conversation;

import javax.inject.Inject;

import in.appnow.blurt.conversation_module.rest_service.models.request.FetchMessageRequest;
import in.appnow.blurt.conversation_module.rest_service.models.response.FetchAllMessageResponse;
import in.appnow.blurt.rest.APIInterface;
import io.reactivex.Observable;


public class ConversationRemoteDataSource implements ConversationRemoteRepository {

    private final APIInterface apiInterface;

    @Inject
    public ConversationRemoteDataSource(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    @Override
    public Observable<FetchAllMessageResponse> fetchAllConversation(FetchMessageRequest requestModel) {
        return apiInterface.getAllMessagesBySessionId(requestModel);
    }
}
