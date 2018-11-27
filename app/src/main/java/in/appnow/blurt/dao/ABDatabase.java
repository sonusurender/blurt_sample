package in.appnow.blurt.dao;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import in.appnow.blurt.conversation_module.rest_service.models.response.ConversationResponse;


@TypeConverters(DateConverters.class)
@Database(entities = {ConversationResponse.class}, version = ABDatabase.VERSION)
public abstract class ABDatabase extends RoomDatabase {

    static final int VERSION = 1;


    public abstract ConversationDao conversationDao();


    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
