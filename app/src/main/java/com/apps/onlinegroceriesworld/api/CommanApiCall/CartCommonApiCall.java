package com.apps.onlinegroceriesworld.api.CommanApiCall;

import android.content.Context;

import com.apps.onlinegroceriesworld.api.ApiClient;
import com.apps.onlinegroceriesworld.api.interfaces.CartInterfaces;
import com.apps.onlinegroceriesworld.api.network.services.CartFragmentInterfaces;
import com.apps.onlinegroceriesworld.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.apps.onlinegroceriesworld.utils.Constent;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartCommonApiCall {
    Context context;
    CartInterfaces.CallBack mCallback;

    public CartCommonApiCall(Context context , CartInterfaces.CallBack mCallback) {
        this.context = context;
        this.mCallback = mCallback;
    }

    public void updateCart(LoadingSpinner loadingSpinner, String product_id, String product_stock_id, UserModel userModel) {
        CartFragmentInterfaces apiService = ApiClient.getClient().create(CartFragmentInterfaces.class);
        loadingSpinner.showLoading();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("product_id", product_id);
        jsonObject.addProperty("product_stock_id", (product_stock_id.equals("0"))? "":product_stock_id);
        jsonObject.addProperty(Constent.PLATFORM_NAME, Constent.PLATFORM);

        Call<CommonGlobalMessageModel> call = apiService.updateCart(userModel.getToken_type()+" "+userModel.getAccess_token(),jsonObject);

        call.enqueue(new Callback<CommonGlobalMessageModel>() {
            @Override
            public void onResponse(Call<CommonGlobalMessageModel> call, Response<CommonGlobalMessageModel> response) {
                loadingSpinner.cancelLoading();
                if(!response.isSuccessful()){
                    return;
                }
                mCallback.onUpdateCartResult(response.body());
            }

            @Override
            public void onFailure(Call<CommonGlobalMessageModel> call, Throwable t) {
                System.out.println("error "+t.getMessage());
                loadingSpinner.cancelLoading();
            }
        });
    }
    public void updateCartWithQty(LoadingSpinner loadingSpinner, String product_id, String product_stock_id, UserModel userModel,String qty) {
        CartFragmentInterfaces apiService = ApiClient.getClient().create(CartFragmentInterfaces.class);
        loadingSpinner.showLoading();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("product_id", product_id);
        jsonObject.addProperty("product_stock_id", (product_stock_id.equals("0"))? "":product_stock_id);
        jsonObject.addProperty("qty", qty);
        jsonObject.addProperty(Constent.PLATFORM_NAME, Constent.PLATFORM);

        Call<CommonGlobalMessageModel> call = apiService.updateCart(userModel.getToken_type()+" "+userModel.getAccess_token(),jsonObject);

        call.enqueue(new Callback<CommonGlobalMessageModel>() {
            @Override
            public void onResponse(Call<CommonGlobalMessageModel> call, Response<CommonGlobalMessageModel> response) {
                loadingSpinner.cancelLoading();
                if(!response.isSuccessful()){
                    return;
                }
                mCallback.onUpdateCartResult(response.body());
            }

            @Override
            public void onFailure(Call<CommonGlobalMessageModel> call, Throwable t) {
                System.out.println("error "+t.getMessage());
                loadingSpinner.cancelLoading();
            }
        });
    }
    public void descCartQty(LoadingSpinner loadingSpinner, CartFragmentInterfaces apiService, String qty_id , String qty, UserModel userModel) {
        loadingSpinner.showLoading();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", qty_id);
        jsonObject.addProperty("qty", qty);
        jsonObject.addProperty(Constent.PLATFORM_NAME, Constent.PLATFORM);

        Call<CommonGlobalMessageModel> call = apiService.descCartQty(userModel.getToken_type()+" "+userModel.getAccess_token(),jsonObject);

        call.enqueue(new Callback<CommonGlobalMessageModel>() {
            @Override
            public void onResponse(Call<CommonGlobalMessageModel> call, Response<CommonGlobalMessageModel> response) {
                loadingSpinner.cancelLoading();
                if(!response.isSuccessful()){
                    return;
                }
                mCallback.onUpdateCartResult(response.body());
            }

            @Override
            public void onFailure(Call<CommonGlobalMessageModel> call, Throwable t) {
                System.out.println("error "+t.getMessage());
                loadingSpinner.cancelLoading();
            }
        });
    }
    public void removeCart(LoadingSpinner loadingSpinner, CartFragmentInterfaces apiService, String id , UserModel userModel) {
        loadingSpinner.showLoading();

        Call<CommonGlobalMessageModel> call = apiService.removeCart(userModel.getToken_type()+" "+userModel.getAccess_token(),id);

        call.enqueue(new Callback<CommonGlobalMessageModel>() {
            @Override
            public void onResponse(Call<CommonGlobalMessageModel> call, Response<CommonGlobalMessageModel> response) {
                loadingSpinner.cancelLoading();
                if(!response.isSuccessful()){
                    return;
                }
                mCallback.onUpdateCartResult(response.body());
            }

            @Override
            public void onFailure(Call<CommonGlobalMessageModel> call, Throwable t) {
                System.out.println("error "+t.getMessage());
                loadingSpinner.cancelLoading();
            }
        });
    }
}
