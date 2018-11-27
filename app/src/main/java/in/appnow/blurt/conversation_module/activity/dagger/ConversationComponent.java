package in.appnow.blurt.conversation_module.activity.dagger;

import dagger.Component;
import in.appnow.blurt.conversation_module.activity.ConversationActivity;
import in.appnow.blurt.dagger.component.AppComponent;

@ConversationScope
@Component(modules = ConversationModule.class,dependencies = AppComponent.class)
public interface ConversationComponent {
    void inject(ConversationActivity conversationActivity);
}
