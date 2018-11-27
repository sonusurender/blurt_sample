package in.appnow.blurt.user_auth.dagger;

import dagger.Component;
import in.appnow.blurt.dagger.component.AppComponent;
import in.appnow.blurt.user_auth.UserAuthActivity;
import in.appnow.blurt.user_auth.fragments.login.LoginFragment;
import in.appnow.blurt.user_auth.fragments.otp.OTPFragment;
import in.appnow.blurt.user_auth.fragments.signup.SignUpFragment;

/**
 * Created by sonu on 18:15, 20/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
@UserAuthScope
@Component(modules = UserAuthModule.class, dependencies = AppComponent.class)
public interface UserAuthComponent {
    void inject(UserAuthActivity authActivity);
    void inject(LoginFragment loginFragment);
    void inject(SignUpFragment signUpFragment);
    void inject(OTPFragment otpFragment);

}
