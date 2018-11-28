package in.appnow.blurt.dagger.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import in.appnow.blurt.dagger.scope.AppScope;

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @AppScope
    @Provides
    public Context context() {
        return context;
    }
}
