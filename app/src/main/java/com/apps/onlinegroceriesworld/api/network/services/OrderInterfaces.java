package com.apps.onlinegroceriesworld.api.network.services;

import com.apps.onlinegroceriesworld.api.network.response.OrderHistoryResponse;
import com.apps.onlinegroceriesworld.models.CommonGlobalMessageModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface OrderInterfaces {
    @Headers("Content-Type: application/json")
    @POST("order/store")
    Call<CommonGlobalMessageModel> placeOrder(@Header("Authorization") String authHeader, @Body JsonObject data);

    @Headers("Content-Type: application/json")
    @GET("order/history/{status}")
    Call<OrderHistoryResponse> getOrderHistory(@Header("Authorization") String authHeader,@Path("status") String status);

    @Headers("Content-Type: application/json")
    @GET
    Call<OrderHistoryResponse> getOrderHistoryByUrl(@Header("Authorization") String authHeader,@Url String url);

    @Headers("Content-Type: application/json")
    @GET("order/track/{id}")
    Call<OrderHistoryResponse> TrackOrderByCode(@Header("Authorization") String authHeader,@Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("order/cancel/{id}")
    Call<CommonGlobalMessageModel> CancelOrder(@Header("Authorization") String authHeader, @Path("id") String id);
}
