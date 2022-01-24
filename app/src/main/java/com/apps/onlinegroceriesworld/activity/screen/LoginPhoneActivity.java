package com.apps.onlinegroceriesworld.activity.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.apps.onlinegroceriesworld.api.ApiClient;
import com.apps.onlinegroceriesworld.api.CommanApiCall.LoginByPhoneApiCall;
import com.apps.onlinegroceriesworld.api.CommanApiCall.interfaces.LoginByPhoneApiCallInterfaces;
import com.apps.onlinegroceriesworld.api.network.services.LoginPhoneApiInterfaces;
import com.apps.onlinegroceriesworld.databinding.ActivityLoginPhoneBinding;
import com.apps.onlinegroceriesworld.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesworld.utils.FirebaseToken;
import com.apps.onlinegroceriesworld.utils.GenericDelay;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.apps.onlinegroceriesworld.utils.UserPrefs;
import com.apps.onlinegroceriesworld.utils.UserResponseHelper;

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