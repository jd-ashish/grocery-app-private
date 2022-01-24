package com.apps.onlinegroceriesworld.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.apps.onlinegroceriesworld.activity.screen.AddressSelectActivity;
import com.apps.onlinegroceriesworld.api.CommanApiCall.SettingCommonApiCall;
import com.apps.onlinegroceriesworld.api.network.response.AppSettingsResponse;
import com.apps.onlinegroceriesworld.databinding.ActivitySplashscreenBinding;
import com.apps.onlinegroceriesworld.utils.GenericDelay;
import com.apps.onlinegroceriesworld.utils.UserPrefs;
import com.google.gson.Gson;

public class SplashscreenActivity extends AppCompatActivity {

    ActivitySplashscreenBinding binding;
    UserPrefs userPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userPrefs = new UserPrefs(this);


        new SettingCommonApiCall(this, new SettingCommonApiCall.SettingCommonApiCallBack() {
            @Override
            public void CallBack(AppSettingsResponse body) {
                System.out.println("SplashscreenActivitySplashscreenActivity "+new Gson().toJson(body));
                userPrefs.setAppSettingsPreferenceObject(body);
                new GenericDelay(() -> {
                    if(userPrefs.getAuthPreferenceObjectJson(UserPrefs.users)!=null){
                        if(userPrefs.getIsAddress().equals("no")){
                            startActivity(new Intent(SplashscreenActivity.this, AddressSelectActivity.class));
                        }else{
                            startActivity(new Intent(SplashscreenActivity.this, MainActivity.class));
                        }
                        finishAffinity();
                    }else{
                        startActivity(new Intent(SplashscreenActivity.this, MainActivity.class));
                        finishAffinity();
                    }
                }).Use();
            }
        }).getSettings();

    }
}