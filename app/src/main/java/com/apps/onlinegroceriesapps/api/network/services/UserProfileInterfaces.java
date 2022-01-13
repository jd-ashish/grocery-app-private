package com.apps.onlinegroceriesapps.api.network.services;

import com.apps.onlinegroceriesapps.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesapps.models.UserAddressList;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserProfileInterfaces {
    @Headers("Content-Type: application/json")
    @POST("user/get-address")
    Call<List<UserAddressList>> getUserAddress(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @POST("user/update-profile")
    Call<CommonGlobalMessageModel> updateAccountInfo(@Header("Authorization") String authHeader,@Body JsonObject jsonObject);

    @Multipart
    @POST("user/update-profile-pic")
    Call<CommonGlobalMessageModel> updateUserProfilePicture(@Header("Authorization") String authHeader, @Part MultipartBody.Part file);

}
