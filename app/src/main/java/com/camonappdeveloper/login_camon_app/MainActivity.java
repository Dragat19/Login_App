package com.camonappdeveloper.login_camon_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "XazSRoId2qrEBCmcr6QXq6c82";
    private static final String TWITTER_SECRET = "ikEoNvfjO9G8dziUxYrHbcbnCVRmWy69WOSmCjTFccutHhCEiv";
    private TwitterLoginButton twitterLoginButton;
    private TextView status;
    //facebook
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        status = (TextView) findViewById(R.id.status);
        status.setText("Status: Ready");

        twitterLoginButton.setCallback(new LoginHandler());

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                status.setText("User ID: "
                        + loginResult.getAccessToken().getUserId()
                        + "\n" + "Auth Token: "
                        + loginResult.getAccessToken().getToken()
                );
            }

            @Override
            public void onCancel() {
                status.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                status.setText("Login attempt failed.");
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(twitterLoginButton != null){
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }

        if (loginButton != null){
            callbackManager.onActivityResult(requestCode, resultCode, data);        }

    }


    private class LoginHandler extends Callback<TwitterSession> {
        @Override
        public void success(Result<TwitterSession> twitterSessionResult) {

            String output = "Status: " +
                    "Your login was successful " +
                    twitterSessionResult.data.getUserName() +
                    "\nAuth Token Received: " +
                    twitterSessionResult.data.getAuthToken().token;

            status.setText(output);

        }

        @Override
        public void failure(TwitterException e) {
            status.setText("Status: Login Failed");
        }
    }
}
