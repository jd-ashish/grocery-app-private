package com.apps.onlinegroceriesapps.activity.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.apps.onlinegroceriesapps.api.ApiClient;
import com.apps.onlinegroceriesapps.api.CommanApiCall.LoginByPhoneApiCall;
import com.apps.onlinegroceriesapps.api.CommanApiCall.interfaces.LoginByPhoneApiCallInterfaces;
import com.apps.onlinegroceriesapps.api.network.services.LoginPhoneApiInterfaces;
import com.apps.onlinegroceriesapps.databinding.ActivityLoginPhoneBinding;
import com.apps.onlinegroceriesapps.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesapps.utils.FirebaseToken;
import com.apps.onlinegroceriesapps.utils.GenericDelay;
import com.apps.onlinegroceriesapps.utils.LoadingSpinner;
import com.apps.onlinegroceriesapps.utils.UserPrefs;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPhoneActivity extends AppCompatActivity {

    ActivityLoginPhoneBinding binding;
    private LoginPhoneApiInterfaces apiService;
    UserResponseHelper helper;
    LoadingSpinner loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();

        clickMethod();

    }

    private void initialize() {
        helper = new UserResponseHelper(this);
        loadingSpinner = new LoadingSpinner(this);
        apiService = ApiClient.getClient().create(LoginPhoneApiInterfaces.class);
    }

    private void clickMethod() {
        binding.txtLogin.setOnClickListener(view -> {
            new LoginByPhoneApiCall(this, new LoginByPhoneApiCallInterfaces() {
                @Override
                public void onResult(CommonGlobalMessageModel body) {
                    if (!body.isError()) {
                        helper.showSuccess(body.getMessage());
                        new FirebaseToken(LoginPhoneActivity.this, token -> {
                            new UserPrefs(LoginPhoneActivity.this).setDeviceToken(token);
                        }).getToken();
                        new GenericDelay(() -> startActivity(new Intent(LoginPhoneActivity.this, LoginOtpActivity.class)
                                .putExtra("phone",binding.phone.getText().toString())
                                .putExtra("country_code", binding.countryCode.getSelectedCountryCodeWithPlus())
                                .putExtra("otp",body.getOtp())
                        )).CustomDelay(1500);

                    }else{
                        helper.showError(body.getMessage());
                    }
                }
            }).loginByPhone(loadingSpinner,apiService,binding.phone.getText().toString(),binding.countryCode.getSelectedCountryCodeWithPlus().toString());
        });


    }

}