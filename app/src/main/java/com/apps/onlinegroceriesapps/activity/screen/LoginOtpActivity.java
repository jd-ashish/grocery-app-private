package com.apps.onlinegroceriesapps.activity.screen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.api.ApiClient;
import com.apps.onlinegroceriesapps.api.CommanApiCall.LoginByPhoneApiCall;
import com.apps.onlinegroceriesapps.api.CommanApiCall.interfaces.LoginByPhoneApiCallInterfaces;
import com.apps.onlinegroceriesapps.api.network.services.LoginPhoneApiInterfaces;
import com.apps.onlinegroceriesapps.databinding.ActivityLoginOtpBinding;
import com.apps.onlinegroceriesapps.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesapps.models.UserModel;
import com.apps.onlinegroceriesapps.utils.Constent;
import com.apps.onlinegroceriesapps.utils.FirebaseToken;
import com.apps.onlinegroceriesapps.utils.GenericDelay;
import com.apps.onlinegroceriesapps.utils.LoadingSpinner;
import com.apps.onlinegroceriesapps.utils.SmsBroadcastReceiver;
import com.apps.onlinegroceriesapps.utils.UserPrefs;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aabhasjindal.otptextview.OTPListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginOtpActivity extends AppCompatActivity {

    ActivityLoginOtpBinding binding;
    boolean isResendOtp;
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    String otpFromMessage , server_otp , country_code ,auth_phone , device_token_data;
    String otpVirewerOtp ;
    UserResponseHelper helper;
    LoadingSpinner loadingSpinner;
    private LoginPhoneApiInterfaces apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Initialization();
        OtpSendCountDownTimer();

        clickMethod();
        
        readOTP();
        registerBroadcastReceiver();
        OtpVerify();
    }

    private void Initialization() {
        helper = new UserResponseHelper(this);
        loadingSpinner = new LoadingSpinner(this);
        apiService = ApiClient.getClient().create(LoginPhoneApiInterfaces.class);
        if(getIntent().hasExtra("otp")){
            server_otp = getIntent().getStringExtra("otp");
            country_code = getIntent().getStringExtra("country_code");
            auth_phone = getIntent().getStringExtra("phone");
        }
    }

    private void OtpVerify() {
        binding.otpView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.

                otpVirewerOtp = otp;
                verifyOtp(otp);
            }
        });

        binding.txtLogin.setOnClickListener(view -> {
            if(otpVirewerOtp!=null){
                verifyOtp(otpVirewerOtp);
            }

//            this is true one
//            startActivity(new Intent(LoginOtpActivity.this,AddressSelectActivity.class));

        });
    }

    private void verifyOtp(String otp) {

        if(otp.equals(otpFromMessage)){
            verifyPhoneUser();
        }else if(otp.equals(server_otp)){
            verifyPhoneUser();
        }else{
            binding.otpView.showError();
            helper.showError("OTP not match");
        }
    }

    private void verifyPhoneUser() {
        loadingSpinner.showLoading();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone", auth_phone);
        jsonObject.addProperty("country_code", country_code);
        jsonObject.addProperty("otp", server_otp);
        jsonObject.addProperty("plateform", "android");
        jsonObject.addProperty("device_token", new UserPrefs(this).getDeviceToken());
        System.out.println("jsonObjectjsonObject "+ new Gson().toJson(jsonObject));

        Call<UserModel> call = apiService.VerifyProcessToPhoneOtpBasedLoginRequest(jsonObject);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                loadingSpinner.cancelLoading();
                if(response.isSuccessful()) {
                    if (!response.body().isError()) {
                        helper.showSuccess(response.body().getMessage());
                        new UserPrefs(LoginOtpActivity.this).setAuthPreferenceObject(response.body(),UserPrefs.users);
                        new UserPrefs(LoginOtpActivity.this).setAddress("no");
                        new GenericDelay(() -> {
                            startActivity(new Intent(LoginOtpActivity.this, AddressSelectActivity.class));
                            finishAffinity();
                        }).CustomDelay(1500);
                    }else{
                        helper.showError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });
    }

    private void readOTP() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clickMethod() {
        binding.resendCode.setOnClickListener(vie -> {
            if(isResendOtp){
                resendOtp();
            }else{
                Toast.makeText(this, "Wait for gettting confirmations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void OtpSendCountDownTimer() {
        new CountDownTimer(Constent.OTP_RESEND, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.resendCode.setTextColor(ContextCompat.getColor(LoginOtpActivity.this,R.color.ndark_red_main));
                binding.progress.setVisibility(View.VISIBLE);
                binding.resendCode.setText(getString(R.string.resend_code)+" "+millisUntilFinished / 1000);
                isResendOtp = false;

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                binding.resendCode.setText("Resend OTP");
                binding.resendCode.setTextColor(ContextCompat.getColor(LoginOtpActivity.this,R.color.nsuccess));
                binding.progress.setVisibility(View.GONE);
                isResendOtp = true;
            }

        }.start();
    }

    private void resendOtp() {
        new LoginByPhoneApiCall(this, new LoginByPhoneApiCallInterfaces() {
            @Override
            public void onResult(CommonGlobalMessageModel body) {
                if(!body.isError()){
                    helper.showSuccess(body.getMessage());
                }else{
                    helper.showError(body.getMessage());
                }
            }
        }).loginByPhone(loadingSpinner, apiService ,auth_phone,country_code);
        OtpSendCountDownTimer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                binding.resendCode.setText(
//                        String.format("%s - %s", getString(R.string.received_message), message));
                getOtpFromMessage(message);
            }
        }
    }
    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            otpFromMessage = matcher.group(0);
            binding.otpView.setOTP(matcher.group(0));
            Toast.makeText(this, matcher.group(0), Toast.LENGTH_SHORT).show();
        }
    }
    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }
}