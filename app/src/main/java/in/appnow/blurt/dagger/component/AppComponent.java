package in.appnow.blurt.dagger.component;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import dagger.Component;
import in.appnow.blurt.dagger.module.AppModule;
import in.appnow.blurt.dagger.module.NetworkModule;
import in.appnow.blurt.dagger.module.RoomModule;
import in.appnow.blurt.dagger.module.SharedPreferencesModule;
import in.appnow.blurt.dagger.scope.AppScope;
import in.appnow.blurt.dao.ABDatabase;
import in.appnow.blurt.helper.PreferenceManger;
import in.appnow.blurt.rest.APIInterface;


@AppScope
@Component(modules = {AppModule.class, NetworkModule.class, RoomModule.class, SharedPreferencesModule.class})
public interface AppComponent {

    Context context();

    APIInterface apiInterface();

    ABDatabase abDatabase();

    ViewModelProvider.Factory provideViewModelFactory();

    PreferenceManger preferenceManger();

}
