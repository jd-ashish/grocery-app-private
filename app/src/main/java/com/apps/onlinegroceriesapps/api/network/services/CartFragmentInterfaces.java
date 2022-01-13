package com.apps.onlinegroceriesapps.api.network.services;

import com.apps.onlinegroceriesapps.api.network.response.CartsResponse;
import com.apps.onlinegroceriesapps.api.network.response.ProductResponse;
import com.apps.onlinegroceriesapps.models.CommonGlobalMessageModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartFragmentInterfaces {
    @Headers("Content-Type: application/json")
    @POST("carts/get")
    Call<CartsResponse> getCarts(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @POST("carts/add")
    Call<CommonGlobalMessageModel> updateCart(@Header("Authorization") String authHeader, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("carts/update")
    Call<CommonGlobalMessageModel> descCartQty(@Header("Authorization") String authHeader, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("carts/destroy/{id}")
    Call<CommonGlobalMessageModel> removeCart(@Header("Authorization") String authHeader, @Path("id") String id);
}
