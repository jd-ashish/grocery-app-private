package com.apps.onlinegroceriesworld.api.network.services;

import com.apps.onlinegroceriesworld.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginPhoneApiInterfaces {

    @POST("auth/login")
    Call<CommonGlobalMessageModel> ProcessToPhoneOtpBasedLoginRequest(@Body JsonObject jsonObject);

    @POST("user/get")
    Call<UserModel> userDetailsById(@Header("Authorization") String authHeader);

    @POST("auth/login/verify")
    Call<UserModel> VerifyProcessToPhoneOtpBasedLoginRequest(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("login/save_address")
    Call<CommonGlobalMessageModel> saveAddress(@Header("Authorization") String authHeader, @Body JsonObject jsonObject);
}
