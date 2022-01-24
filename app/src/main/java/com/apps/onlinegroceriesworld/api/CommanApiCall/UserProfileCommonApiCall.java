package com.apps.onlinegroceriesworld.api.CommanApiCall;

import android.content.Context;

import com.apps.onlinegroceriesworld.api.ApiClient;
import com.apps.onlinegroceriesworld.api.network.services.UserProfileInterfaces;
import com.apps.onlinegroceriesworld.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesworld.models.UserAddressList;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileCommonApiCall {
    Context context;
    CallBack callBack;
    CallBackUpdateProfileInfo callBackUpdateProfileInfo;
    public interface CallBack{
        void onGetAddresses(List<UserAddressList> body);
    }
    public interface CallBackUpdateProfileInfo{
        void onResult(CommonGlobalMessageModel body);
    }
    public UserProfileCommonApiCall(Context context,CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }
    public UserProfileCommonApiCall(Context context,CallBackUpdateProfileInfo callBackUpdateProfileInfo) {
        this.context = context;
        this.callBackUpdateProfileInfo = callBackUpdateProfileInfo;
    }


    public void getUserAddressesList(LoadingSpinner loadingSpinner, UserModel userModel){
        loadingSpinner.showLoading();
        UserProfileInterfaces apiService = ApiClient.getClient().create(UserProfileInterfaces.class);
        Call<List<UserAddressList>> call = apiService.getUserAddress(userModel.getToken_type()+" "+userModel.getAccess_token());

        call.enqueue(new Callback<List<UserAddressList>>() {
            @Override
            public void onResponse(Call<List<UserAddressList>> call, Response<List<UserAddressList>> response) {
                loadingSpinner.cancelLoading();
                callBack.onGetAddresses(response.body());
            }

            @Override
            public void onFailure(Call<List<UserAddressList>> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });
    }

    public void updateProfile(LoadingSpinner loadingSpinner, UserModel userModel, JsonObject jsonObject){
        loadingSpinner.showLoading();
        UserProfileInterfaces apiService = ApiClient.getClient().create(UserProfileInterfaces.class);
        Call<CommonGlobalMessageModel> call = apiService.updateAccountInfo(userModel.getToken_type()+" "+userModel.getAccess_token(),jsonObject);

        call.enqueue(new Callback<CommonGlobalMessageModel>() {
            @Override
            public void onResponse(Call<CommonGlobalMessageModel> call, Response<CommonGlobalMessageModel> response) {
                loadingSpinner.cancelLoading();
                callBackUpdateProfileInfo.onResult(response.body());
            }

            @Override
            public void onFailure(Call<CommonGlobalMessageModel> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });
    }
}
