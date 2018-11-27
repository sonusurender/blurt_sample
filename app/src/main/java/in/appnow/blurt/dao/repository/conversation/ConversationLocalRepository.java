package in.appnow.blurt.dao.repository.conversation;


import android.arch.lifecycle.LiveData;

import java.util.List;

import in.appnow.blurt.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.blurt.dao.repository.BaseRepository;

public interface ConversationLocalRepository extends BaseRepository<ConversationResponse> {
    LiveData<List<ConversationResponse>> fetchAllConversation(String sessionId);

    ConversationResponse fetchSingleMessage(long messageId);

    ConversationResponse fetchSingleMessageForTempId(long msgTempId);

    int updateMessageStatus(String status, long msgId);

    void deleteChatTable();
}
