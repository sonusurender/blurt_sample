package in.appnow.blurt.dagger.module;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import in.appnow.blurt.dagger.scope.AppScope;
import in.appnow.blurt.dao.ABDatabase;
import in.appnow.blurt.dao.ConversationDao;
import in.appnow.blurt.dao.repository.conversation.ConversationLocalDataSource;
import in.appnow.blurt.dao.repository.conversation.ConversationLocalRepository;
import in.appnow.blurt.dao.repository.conversation.ConversationRemoteDataSource;
import in.appnow.blurt.dao.repository.conversation.ConversationRemoteRepository;
import in.appnow.blurt.dao.viewmodel.CustomViewModelFactory;
import in.appnow.blurt.rest.APIInterface;

@Module
public class RoomModule {

    private final ABDatabase abDatabase;

    public RoomModule(Context context) {
        this.abDatabase = Room.databaseBuilder(context,
                ABDatabase.class,
                "blurt.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @AppScope
    @Provides
    ABDatabase providesRoomDatabase() {
        return abDatabase;
    }

    @AppScope
    @Provides
    ConversationDao providesConversationDao(ABDatabase demoDatabase) {
        return demoDatabase.conversationDao();
    }


    @AppScope
    @Provides
    ConversationLocalRepository conversationLocalRepository(ConversationDao conversationDao) {
        return new ConversationLocalDataSource(conversationDao);
    }

    @AppScope
    @Provides
    ConversationRemoteRepository conversationRemoteRepository(APIInterface apiInterface) {
        return new ConversationRemoteDataSource(apiInterface);
    }

    @Provides
    @AppScope
    ViewModelProvider.Factory provideViewModelFactory(ConversationLocalRepository conversationLocalRepository, ConversationRemoteRepository conversationRemoteRepository) {
        return new CustomViewModelFactory(conversationLocalRepository, conversationRemoteRepository);
    }

}
