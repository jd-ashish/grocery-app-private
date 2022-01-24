package com.apps.onlinegroceriesworld.api.network.services;

import com.apps.onlinegroceriesworld.api.network.response.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface CategoryExploreInterfaces {
    @Headers("Content-Type: application/json")
    @GET("categories/{search}")
    Call<CategoryResponse> getCategoryExplore(@Path("search") String search);
}
