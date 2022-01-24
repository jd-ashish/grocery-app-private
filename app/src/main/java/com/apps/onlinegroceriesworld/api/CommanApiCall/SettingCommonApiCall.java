package com.apps.onlinegroceriesworld.api.CommanApiCall;

import android.content.Context;

import com.apps.onlinegroceriesworld.api.ApiClient;
import com.apps.onlinegroceriesworld.api.network.response.AppSettingsResponse;
import com.apps.onlinegroceriesworld.api.network.services.SettingInterfaces;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingCommonApiCall {
    Context context;
    SettingCommonApiCallBack settingCommonApiCallBack;
    SettingInterfaces apiService;

    public interface SettingCommonApiCallBack{
        void CallBack(AppSettingsResponse body);
    }
    public SettingCommonApiCall(Context context , SettingCommonApiCallBack settingCommonApiCallBack) {
        this.context = context;
        this.settingCommonApiCallBack = settingCommonApiCallBack;
    }

    public void getSettings(){
        apiService = ApiClient.getClient().create(SettingInterfaces.class);
        Call<AppSettingsResponse> call = apiService.getSettings();

        call.enqueue(new Callback<AppSettingsResponse>() {
            @Override
            public void onResponse(Call<AppSettingsResponse> call, Response<AppSettingsResponse> response) {

                settingCommonApiCallBack.CallBack(response.body());
            }

            @Override
            public void onFailure(Call<AppSettingsResponse> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });
    }
}
