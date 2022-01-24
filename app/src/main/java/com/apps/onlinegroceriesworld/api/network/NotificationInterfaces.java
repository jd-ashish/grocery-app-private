package com.apps.onlinegroceriesworld.api.network;

import com.apps.onlinegroceriesworld.api.network.response.NotificationResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationInterfaces {
    @Headers("Content-Type: application/json")
    @POST("notification/get")
    Call<NotificationResponse> getNotifications(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @POST("notification/viewed")
    Call<NotificationResponse> viewed(@Header("Authorization") String authHeader, @Body JsonObject jsonObject);
}
