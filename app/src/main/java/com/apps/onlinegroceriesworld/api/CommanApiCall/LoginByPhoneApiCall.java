package com.apps.onlinegroceriesworld.api.CommanApiCall;

import android.content.Context;

import com.apps.onlinegroceriesworld.api.CommanApiCall.interfaces.LoginByPhoneApiCallInterfaces;
import com.apps.onlinegroceriesworld.api.network.services.LoginPhoneApiInterfaces;
import com.apps.onlinegroceriesworld.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesworld.models.UserAddressList;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.apps.onlinegroceriesworld.utils.UserPrefs;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginByPhoneApiCall {
    Context context;
    LoginByPhoneApiCallInterfaces loginByPhoneApiCallInterfaces;
    LoginInterfaces loginInterfaces;


    public interface LoginInterfaces{
        void onLogin(UserModel body);
    }
    public LoginByPhoneApiCall(Context context, LoginByPhoneApiCallInterfaces loginByPhoneApiCallInterfaces) {
        this.context = context;
        this.loginByPhoneApiCallInterfaces = loginByPhoneApiCallInterfaces;
    }
    public LoginByPhoneApiCall(Context context, LoginInterfaces loginInterfaces) {
        this.context = context;
        this.loginInterfaces = loginInterfaces;
    }

    public void loginByPhone(LoadingSpinner loadingSpinner, LoginPhoneApiInterfaces apiService, String phoneNumber, String countryCode) {
        loadingSpinner.showLoading();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone", phoneNumber);
        jsonObject.addProperty("country_code", countryCode);

        Call<CommonGlobalMessageModel> call = apiService.ProcessToPhoneOtpBasedLoginRequest(jsonObject);

        call.enqueue(new Callback<CommonGlobalMessageModel>() {
            @Override
            public void onResponse(Call<CommonGlobalMessageModel> call, Response<CommonGlobalMessageModel> response) {
                loadingSpinner.cancelLoading();
                loginByPhoneApiCallInterfaces.onResult(response.body());
            }

            @Override
            public void onFailure(Call<CommonGlobalMessageModel> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });
    }
    public void loginByUserId(LoadingSpinner loadingSpinner, LoginPhoneApiInterfaces apiService , UserModel userModel) {
        loadingSpinner.showLoading();

        Call<UserModel> call = apiService.userDetailsById(userModel.getToken_type()+" "+userModel.getAccess_token());

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                loadingSpinner.cancelLoading();
                loginInterfaces.onLogin(response.body());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });
    }

    public void updateUserAddress(LoadingSpinner loadingSpinner, LoginPhoneApiInterfaces apiService, UserAddressList userAddressList) {
        loadingSpinner.showLoading();

        if(userAddressList!=null){
            loginByPhoneApiCallInterfaces.onResult(new CommonGlobalMessageModel("UserAddressList update","123",false));
            return;
        }
        UserPrefs userPrefs = new UserPrefs(context);
        JsonObject address = userPrefs.getGpsAddress();
        UserModel userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
//        String city = address.getLocality();
//        String state = address.getAdminArea();
//        String country = address.getCountryName();
//        String postalCode = address.getPostalCode();
//        String knownName = address.getFeatureName(); //
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("user_id", userModel.getUser().getId());
//        jsonObject.addProperty("longitude", String.valueOf(address.getLongitude()));
//        jsonObject.addProperty("lattitude", String.valueOf(address.getLatitude()));
//        jsonObject.addProperty("address", address.getAddressLine(0));
//        jsonObject.addProperty("city", city);
//        jsonObject.addProperty("state", state);
//        jsonObject.addProperty("country", country);
//        jsonObject.addProperty("postalCode", postalCode);
//        jsonObject.addProperty("knownName", knownName);

        System.out.println("jxvhncxnvfhjjdghdfjgdfjgfj "+new Gson().toJson(address));
        Call<CommonGlobalMessageModel> call = apiService.saveAddress(userModel.getToken_type()+" "+userModel.getAccess_token(),address);

        call.enqueue(new Callback<CommonGlobalMessageModel>() {
            @Override
            public void onResponse(Call<CommonGlobalMessageModel> call, Response<CommonGlobalMessageModel> response) {
                loadingSpinner.cancelLoading();
                loginByPhoneApiCallInterfaces.onResult(response.body());
            }

            @Override
            public void onFailure(Call<CommonGlobalMessageModel> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });
    }
}
