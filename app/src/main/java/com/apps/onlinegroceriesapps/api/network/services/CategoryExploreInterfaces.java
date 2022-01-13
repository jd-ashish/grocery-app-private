package com.apps.onlinegroceriesapps.api.network.services;

import com.apps.onlinegroceriesapps.api.network.response.CategoryResponse;
import com.apps.onlinegroceriesapps.models.CommonGlobalMessageModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CategoryExploreInterfaces {
    @Headers("Content-Type: application/json")
    @GET("categories/{search}")
    Call<CategoryResponse> getCategoryExplore(@Path("search") String search);
}
