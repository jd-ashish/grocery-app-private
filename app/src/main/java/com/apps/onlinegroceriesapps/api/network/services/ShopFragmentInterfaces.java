package com.apps.onlinegroceriesapps.api.network.services;

import com.apps.onlinegroceriesapps.api.network.response.CategoryResponse;
import com.apps.onlinegroceriesapps.api.network.response.GetExclusiveOfferCardResponse;
import com.apps.onlinegroceriesapps.api.network.response.ProductDetialsResponse;
import com.apps.onlinegroceriesapps.api.network.response.ProductResponse;
import com.apps.onlinegroceriesapps.models.VariantResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ShopFragmentInterfaces {
    @Headers("Content-Type: application/json")
    @GET("products/bestSelling")
    Call<ProductResponse> getBestSelling();

    @Headers("Content-Type: application/json")
    @GET("products/execlusive/offer")
    Call<ProductResponse> getExeclusiveOffer();

    @Headers("Content-Type: application/json")
    @GET("offer/exclusive")
    Call<GetExclusiveOfferCardResponse> getExeclusiveOfferCard();

    @Headers("Content-Type: application/json")
    @GET("products/by/category/{id}")
    Call<ProductResponse> getProductByCategoryId(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("offer/exclusive-by-id/{id}")
    Call<ProductResponse> getProductByExclusiveOfferId(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("products/show/{id}")
    Call<ProductDetialsResponse> getProductById(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET
    Call<ProductResponse> getProductByUrl(@Url String url);

    @FormUrlEncoded
    @POST("products/variant/price")
    Call<VariantResponse> getVariantPrice(@Field("id") int id, @Field("color") String color, @Field("choice") String choiceArray);


    @Headers("Content-Type: application/json")
    @GET("products/filter/{id}/{price}/{date}")
    Call<ProductResponse> getProductByFilter(@Path("id") String id,@Path("price") String price,@Path("date") String date);

    @Headers("Content-Type: application/json")
    @GET("products/search")
    Call<ProductResponse> search(@Query("scope") String scope , @Query("key") String key);
    /*Here id means category id*/

}
