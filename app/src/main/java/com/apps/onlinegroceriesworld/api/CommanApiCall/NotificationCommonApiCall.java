package com.apps.onlinegroceriesworld.api.CommanApiCall;

import android.content.Context;

import com.apps.onlinegroceriesworld.api.ApiClient;
import com.apps.onlinegroceriesworld.api.network.NotificationInterfaces;
import com.apps.onlinegroceriesworld.api.network.response.NotificationResponse;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationCommonApiCall {
    Context context;
    NotificationApiCallInterfaces notificationInterfaces;

    public interface NotificationApiCallInterfaces{
        void CallBack(NotificationResponse body);
    }
    public NotificationCommonApiCall(Context context , NotificationApiCallInterfaces notificationInterfaces) {
        this.context = context;
        this.notificationInterfaces = notificationInterfaces;
    }
    public NotificationCommonApiCall(Context context) {
        this.context = context;
    }
    public void getNotifications(LoadingSpinner loadingSpinner,UserModel userModel) {
        NotificationInterfaces apiService = ApiClient.getClient().create(NotificationInterfaces.class);
        loadingSpinner.showLoading();

        Call<NotificationResponse> call = apiService.getNotifications(userModel.getToken_type()+" "+userModel.getAccess_token());

        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                loadingSpinner.cancelLoading();
                if(!response.isSuccessful()){
                    return;
                }
                notificationInterfaces.CallBack(response.body());
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                System.out.println("error "+t.getMessage());
                loadingSpinner.cancelLoading();
            }
        });
    }
    public void NotificationViewed(UserModel userModel, JsonObject jsonObject) {
        NotificationInterfaces apiService = ApiClient.getClient().create(NotificationInterfaces.class);

        Call<NotificationResponse> call = apiService.viewed(userModel.getToken_type()+" "+userModel.getAccess_token(),jsonObject);

        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if(!response.isSuccessful()){
                    return;
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });
    }
}
