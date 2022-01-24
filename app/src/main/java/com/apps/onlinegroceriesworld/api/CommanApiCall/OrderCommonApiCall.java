package com.apps.onlinegroceriesworld.api.CommanApiCall;

import android.content.Context;

import com.apps.onlinegroceriesworld.api.ApiClient;
import com.apps.onlinegroceriesworld.api.network.response.OrderHistoryResponse;
import com.apps.onlinegroceriesworld.api.network.services.OrderInterfaces;
import com.apps.onlinegroceriesworld.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderCommonApiCall {
    Context context;
    PlaceOrderApiCall placeOrderApiCall;
    HistoryOrderApiCall historyOrderApiCall;
    OrderInterfaces apiService;
    public interface PlaceOrderApiCall{
        void CallBack(CommonGlobalMessageModel body);
    }
    public interface HistoryOrderApiCall{
        void CallBack(OrderHistoryResponse body);
    }
    public OrderCommonApiCall(Context context , PlaceOrderApiCall placeOrderApiCall) {
        this.context = context;
        this.placeOrderApiCall = placeOrderApiCall;
    }
    public OrderCommonApiCall(Context context , HistoryOrderApiCall historyOrderApiCall) {
        this.context = context;
        this.historyOrderApiCall = historyOrderApiCall;
    }

    public void placeOrder(LoadingSpinner loadingSpinner, JsonObject data , UserModel userModel) {
        loadingSpinner.showLoading();
        apiService = ApiClient.getClient().create(OrderInterfaces.class);
        Call<CommonGlobalMessageModel> call = apiService.placeOrder(userModel.getToken_type()+" "+userModel.getAccess_token(), data);

        call.enqueue(new Callback<CommonGlobalMessageModel>() {
            @Override
            public void onResponse(Call<CommonGlobalMessageModel> call, Response<CommonGlobalMessageModel> response) {
                loadingSpinner.cancelLoading();
                if(!response.isSuccessful()){
                    return;
                }
                placeOrderApiCall.CallBack(response.body());
            }

            @Override
            public void onFailure(Call<CommonGlobalMessageModel> call, Throwable t) {
                System.out.println("error "+t.getMessage());
                loadingSpinner.cancelLoading();
            }
        });
    }
    public void getOrderHistory(LoadingSpinner loadingSpinner , UserModel userModel,String url,String status) {
        loadingSpinner.showLoading();
        apiService = ApiClient.getClient().create(OrderInterfaces.class);
        Call<OrderHistoryResponse> call = null;
        if(url.equals("")){
            call = apiService.getOrderHistory(userModel.getToken_type()+" "+userModel.getAccess_token(),status);
        }else {
            call = apiService.getOrderHistoryByUrl(userModel.getToken_type()+" "+userModel.getAccess_token(),url);
        }

        call.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                loadingSpinner.cancelLoading();
                if(!response.isSuccessful()){
                    return;
                }
                historyOrderApiCall.CallBack(response.body());
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                System.out.println("error "+t.getMessage());
                loadingSpinner.cancelLoading();
            }
        });
    }
    public void orderOrderByCode(LoadingSpinner loadingSpinner , UserModel userModel , String code) {
        loadingSpinner.showLoading();
        apiService = ApiClient.getClient().create(OrderInterfaces.class);
        Call<OrderHistoryResponse> call = apiService.TrackOrderByCode(userModel.getToken_type()+" "+userModel.getAccess_token(),code);

        call.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                loadingSpinner.cancelLoading();
                if(!response.isSuccessful()){
                    return;
                }
                historyOrderApiCall.CallBack(response.body());
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                System.out.println("error "+t.getMessage());
                loadingSpinner.cancelLoading();
            }
        });
    }
    public void CancelOrders(LoadingSpinner loadingSpinner , UserModel userModel , String code) {
        loadingSpinner.showLoading();
        apiService = ApiClient.getClient().create(OrderInterfaces.class);
        Call<CommonGlobalMessageModel> call = apiService.CancelOrder(userModel.getToken_type()+" "+userModel.getAccess_token(),code);

        call.enqueue(new Callback<CommonGlobalMessageModel>() {
            @Override
            public void onResponse(Call<CommonGlobalMessageModel> call, Response<CommonGlobalMessageModel> response) {
                loadingSpinner.cancelLoading();
                if(!response.isSuccessful()){
                    return;
                }
                placeOrderApiCall.CallBack(response.body());
            }

            @Override
            public void onFailure(Call<CommonGlobalMessageModel> call, Throwable t) {
                System.out.println("error "+t.getMessage());
                loadingSpinner.cancelLoading();
            }
        });
    }
}
