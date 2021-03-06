package com.asu.mc.digitalassist.main.activities;

import android.content.Intent;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.asu.mc.digitalassist.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.firebase.ui.auth.ui.FlowParameters;
import com.firebase.ui.auth.ui.email.RecoverPasswordActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";
    private static final int RC_SIGN_IN = 123;// Sign In Request Code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        if (auth.getCurrentUser() != null) {
//            AuthUI.getInstance()
//                    .signOut(this)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        public void onComplete(@NonNull Task<Void> task) {
////                            startActivity(new Intent(LoginActivity.this, LoginActivity.class));
////                            finish();
//                        }
//                    });
//
//        }
        //     else{
        AuthUI.SignInIntentBuilder signInIntentBuilder = AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(true);
        ArrayList<AuthUI.IdpConfig> authProviders = new ArrayList<>();
        authProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
        authProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
        //authProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());
        startActivityForResult(signInIntentBuilder.setProviders(authProviders).build(), 123);
        //   }
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent data) {

        super.onActivityResult(requestCode, responseCode, data);
        View parentView = findViewById(android.R.id.content);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            Log.d("provider type: ",response.getProviderType());
            if (responseCode == ResultCodes.OK) {
                String name = auth.getCurrentUser().getDisplayName();
                Log.d("Name: ",name);
                startActivity(UserProfileActivity.createIntent(this, response));
                finish();
                return;
            } else {
                if (response == null) {
                    Snackbar.make(parentView, "SIGN_IN_CANCELLED", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(parentView, "NO_INTERNET_CONNECTION", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Snackbar.make(parentView, "UNKNOWN_ERROR", Snackbar.LENGTH_LONG).show();
                    return;
                }

            }
            Snackbar.make(parentView, "UNKNOWN_SIGNIN_RESPONSE", Snackbar.LENGTH_LONG).show();
        }
    }
}
