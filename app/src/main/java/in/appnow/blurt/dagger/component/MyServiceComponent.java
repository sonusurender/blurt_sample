package in.appnow.blurt.dagger.component;

import dagger.Component;
import in.appnow.blurt.conversation_module.background_service.ConversationIntentService;
import in.appnow.blurt.dagger.module.MyServiceModule;
import in.appnow.blurt.dagger.scope.MyServiceScope;

@MyServiceScope
@Component(modules = MyServiceModule.class, dependencies = AppComponent.class)
public interface MyServiceComponent {

    void inject(ConversationIntentService conversationIntentService);
}
