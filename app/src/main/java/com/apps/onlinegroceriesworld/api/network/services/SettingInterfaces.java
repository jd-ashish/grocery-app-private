package com.apps.onlinegroceriesworld.api.network.services;

import com.apps.onlinegroceriesworld.api.network.response.AppSettingsResponse;
import com.apps.onlinegroceriesworld.api.network.response.ImageSlider;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface SettingInterfaces {
    @Headers("Content-Type: application/json")
    @GET("setting/image/slider")
    Call<ImageSlider> getImageSlider();

    @Headers("Content-Type: application/json")
    @GET("setting/get")
    Call<AppSettingsResponse> getSettings();
}
