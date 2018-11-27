package in.appnow.blurt.dao.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import in.appnow.blurt.dagger.scope.AppScope;
import in.appnow.blurt.dao.repository.conversation.ConversationLocalRepository;
import in.appnow.blurt.dao.repository.conversation.ConversationRemoteRepository;


@AppScope
public class CustomViewModelFactory implements ViewModelProvider.Factory {

    private final ConversationLocalRepository conversationLocalRepository;
    private final ConversationRemoteRepository conversationRemoteRepository;

    @Inject
    public CustomViewModelFactory(ConversationLocalRepository conversationLocalRepository, ConversationRemoteRepository conversationRemoteRepository) {
        this.conversationLocalRepository = conversationLocalRepository;
        this.conversationRemoteRepository = conversationRemoteRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ConversationViewModel.class)) {
            return (T) new ConversationViewModel(conversationLocalRepository,conversationRemoteRepository);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}
