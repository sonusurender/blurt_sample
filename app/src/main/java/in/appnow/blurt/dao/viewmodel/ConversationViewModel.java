package in.appnow.blurt.dao.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.util.List;

import in.appnow.blurt.conversation_module.rest_service.models.request.FetchMessageRequest;
import in.appnow.blurt.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.blurt.dao.repository.conversation.ConversationLocalRepository;
import in.appnow.blurt.dao.repository.conversation.ConversationRemoteRepository;
import in.appnow.blurt.utils.Logger;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ConversationViewModel extends ViewModel {

    private static final String TAG = ConversationViewModel.class.getSimpleName();
    private ConversationLocalRepository conversationLocalRepository;
    private ConversationRemoteRepository conversationRemoteRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    ConversationViewModel(ConversationLocalRepository conversationLocalRepository, ConversationRemoteRepository conversationRemoteRepository) {
        this.conversationLocalRepository = conversationLocalRepository;
        this.conversationRemoteRepository = conversationRemoteRepository;
    }

    public LiveData<List<ConversationResponse>> fetchAllConversation(String sessionId) {
        return conversationLocalRepository.fetchAllConversation(sessionId);
    }

    public long insert(ConversationResponse obj) {
        return conversationLocalRepository.insert(obj);
    }

    public long[] insert(List<ConversationResponse> list) {
        return conversationLocalRepository.insert(list);
    }

    public int update(ConversationResponse obj) {
        return conversationLocalRepository.update(obj);
    }

    public int delete(ConversationResponse obj) {
        return conversationLocalRepository.delete(obj);
    }

    public int delete(List<ConversationResponse> list) {
        return conversationLocalRepository.delete(list);
    }


    public void fetchConversationFromServer(FetchMessageRequest requestModel) {
        //add observable to CompositeDisposable so that it can be dispose when ViewModel is ready to be destroyed
        //Call retrofit client on background thread and update database with response from service using Room
        compositeDisposable.add(Observable.just(1)
                .subscribeOn(Schedulers.computation())
                .flatMap(i -> conversationRemoteRepository.fetchAllConversation(requestModel))
                .subscribeOn(Schedulers.io())
                .subscribe(chatHistoryResponse -> {
                            if (chatHistoryResponse.getConversationModelList() != null && chatHistoryResponse.getConversationModelList().size() > 0) {
                                AsyncTask.execute(() -> {
                                    Logger.DebugLog(TAG, "Fetching completed : " + chatHistoryResponse.getConversationModelList().toString());
                                    conversationLocalRepository.deleteChatTable();
                                    long[] ids = conversationLocalRepository.insert(chatHistoryResponse.getConversationModelList());
                                    Logger.DebugLog(TAG, "Data inserted : "  + ids.length);

                                });

                            }
                        },
                        throwable -> Logger.ErrorLog("MainActivity", "exception getting coupons : " + throwable)));

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //prevents memory leaks by disposing pending observable objects
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}
