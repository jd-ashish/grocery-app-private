package com.apps.onlinegroceriesapps.api.network.services.payment;

import com.apps.onlinegroceriesapps.models.payment.cashfree.CFTokenModel;
import com.apps.onlinegroceriesapps.utils.Constent;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface CashFreeApiInterfaces {
    @Headers({"Content-Type: application/json","x-client-id: "+ Constent.client_id , "x-client-secret: "+Constent.Secret_key})
    @POST
    Call<CFTokenModel> gerCFToken(@Url String url, @Body JsonObject jsonObject);
}
