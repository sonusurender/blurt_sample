package in.appnow.blurt.conversation_module.activity.dagger;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.blurt.conversation_module.activity.mvp.ConversationActivityView;
import in.appnow.blurt.conversation_module.activity.mvp.ConversationModel;
import in.appnow.blurt.conversation_module.activity.mvp.ConversationPresenter;
import in.appnow.blurt.dao.ABDatabase;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.rest.APIInterface;

@Module
public class ConversationModule {
    private final AppCompatActivity appCompatActivity;

    public ConversationModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    @ConversationScope
    public ConversationActivityView view() {
        return new ConversationActivityView(appCompatActivity);
    }

    @Provides
    @ConversationScope
    public ConversationModel conversationModel(APIInterface apiInterface, ViewModelProvider.Factory viewModeFactory) {
        return new ConversationModel(appCompatActivity, apiInterface,viewModeFactory);
    }

    @Provides
    @ConversationScope
    public ConversationPresenter conversationPresenter(ConversationActivityView view, ConversationModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new ConversationPresenter(view, model,preferenceManger,abDatabase);
    }
}
