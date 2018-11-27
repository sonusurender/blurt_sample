package in.appnow.blurt.main.dagger;

import dagger.Component;
import in.appnow.blurt.dagger.component.AppComponent;
import in.appnow.blurt.main.MainActivity;
import in.appnow.blurt.main.fragments.home.HomeFragment;

/**
 * Created by sonu on 11:48, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
@MainActivityScope
@Component(modules = MainActivityModule.class, dependencies = AppComponent.class)
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(HomeFragment homeFragment);
}
