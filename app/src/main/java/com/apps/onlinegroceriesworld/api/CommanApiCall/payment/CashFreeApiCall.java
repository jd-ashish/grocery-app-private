package com.apps.onlinegroceriesworld.api.CommanApiCall.payment;

import android.content.Context;

import com.apps.onlinegroceriesworld.api.ApiClient;
import com.apps.onlinegroceriesworld.api.network.services.payment.CashFreeApiInterfaces;
import com.apps.onlinegroceriesworld.models.payment.cashfree.CFTokenModel;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CashFreeApiCall {
    Context context;
    CashFreeApiCallInterfaces.CFToken mCFToken;
    public interface CashFreeApiCallInterfaces{
        interface CFToken{
            void onResult(CFTokenModel body);
        }
    }
    public CashFreeApiCall(Context context , CashFreeApiCallInterfaces.CFToken mCFToken) {
        this.context = context;
        this.mCFToken = mCFToken;
    }

    public void GenerateCFToken(LoadingSpinner loadingSpinner, JsonObject jsonObject) {
        loadingSpinner.showLoading();
        CashFreeApiInterfaces apiService = ApiClient.getClient().create(CashFreeApiInterfaces.class);


        Call<CFTokenModel> call = apiService.gerCFToken("https://test.cashfree.com/api/v2/cftoken/order",jsonObject);

        call.enqueue(new Callback<CFTokenModel>() {
            @Override
            public void onResponse(Call<CFTokenModel> call, Response<CFTokenModel> response) {
                loadingSpinner.cancelLoading();
                System.out.println("jhvfjdhvgjhvkjfk "+new Gson().toJson(response.body()));
                mCFToken.onResult(response.body());
            }

            @Override
            public void onFailure(Call<CFTokenModel> call, Throwable t) {
                loadingSpinner.cancelLoading();
                System.out.println("error "+t.getMessage());
            }
        });
    }
}
