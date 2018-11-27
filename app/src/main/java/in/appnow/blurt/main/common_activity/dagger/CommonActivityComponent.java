package in.appnow.blurt.main.common_activity.dagger;

import dagger.Component;
import in.appnow.blurt.dagger.component.AppComponent;
import in.appnow.blurt.main.common_activity.CommonActivity;

/**
 * Created by sonu on 16:08, 19/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
@CommonActivityScope
@Component(modules = CommonActivityModule.class, dependencies = AppComponent.class)
public interface CommonActivityComponent {
    void inject(CommonActivity commonActivity);
}
