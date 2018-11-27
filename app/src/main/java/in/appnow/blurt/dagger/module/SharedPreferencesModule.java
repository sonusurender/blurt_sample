package in.appnow.blurt.dagger.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import in.appnow.blurt.dagger.scope.AppScope;
import in.appnow.blurt.helper.PreferenceManger;


@Module
public class SharedPreferencesModule {
    private final PreferenceManger preferenceManger;

    public SharedPreferencesModule(Application application) {
        preferenceManger = new PreferenceManger(application.getSharedPreferences(PreferenceManger.PREF_KEY, Context.MODE_PRIVATE));
    }

    @Provides
    @AppScope
    PreferenceManger provideSharedPreferences() {
        return preferenceManger;
    }
}
