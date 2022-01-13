package com.apps.onlinegroceriesapps.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseToken {

    public interface FirebaseTokenInterfaces {
        void onTokenComplete(String token);
    }

    Context context;
    FirebaseToken.FirebaseTokenInterfaces firebaseTokenInterfaces;

    public FirebaseToken(Context context, FirebaseToken.FirebaseTokenInterfaces firebaseTokenInterfaces) {
        this.context = context;
        this.firebaseTokenInterfaces = firebaseTokenInterfaces;
    }

    public void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("LoginActivity", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        firebaseTokenInterfaces.onTokenComplete(task.getResult());
                    }
                });
    }
}
