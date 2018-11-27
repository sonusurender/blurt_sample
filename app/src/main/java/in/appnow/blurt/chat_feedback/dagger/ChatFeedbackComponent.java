package in.appnow.blurt.chat_feedback.dagger;

import dagger.Component;
import in.appnow.blurt.chat_feedback.ChatFeedbackActivity;
import in.appnow.blurt.dagger.component.AppComponent;


/**
 * Created by sonu on 17:26, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
@ChatFeedbackScope
@Component(modules = ChatFeedbackModule.class, dependencies = AppComponent.class)
public interface ChatFeedbackComponent {
    void inject(ChatFeedbackActivity activity);
}
