package com.apps.onlinegroceriesworld.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.apps.onlinegroceriesworld.api.network.response.AppSettingsResponse;
import com.apps.onlinegroceriesworld.models.UserAddressList;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UserPrefs{

    private static final String PREFS_NAME = "com.apps.onlinegroceriesapps.utils.UserPrefs";

    private static SharedPreferences settings;

    private static SharedPreferences.Editor editor;

    public static String users = "users";
    public static String address = "address";
    public static String gps_address = "gps_address";
    public static String checkoutList = "checkoutList";
    public static String AppSettings = "AppSettings";

    public UserPrefs(Context ctx){
        if(settings == null){
            settings = ctx.getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE );
        }
        editor = settings.edit();
    }
    public String getDeviceToken() {
        return settings.getString("DEVICETOKEN", "");
    }
    public void setDeviceToken(String token) {
        editor.putString("DEVICETOKEN", token);
        editor.commit();
    }
    public void setAuthPreferenceObject(UserModel userModel, String key) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(userModel);
        editor.putString(key, jsonObject);
        editor.commit();
    }

    public UserModel getAuthPreferenceObjectJson(String key) {
        String json = settings.getString(key, "");
        Gson gson = new Gson();
        UserModel authResponse = gson.fromJson(json, UserModel.class);
        return authResponse;
    }
    public void setAddress(String isAddress){
        editor.putString(UserPrefs.address,isAddress);
        editor.commit();
    }

    public String getIsAddress(){
        return settings.getString(UserPrefs.address,"");
    }

    public void setGpsAddress(JsonObject address) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(address);
        editor.putString(UserPrefs.gps_address, jsonObject);
        editor.commit();
    }

    public JsonObject getGpsAddress() {
        String json = settings.getString(UserPrefs.gps_address, "");
        Gson gson = new Gson();
        JsonObject addres = gson.fromJson(json, JsonObject.class);
        return addres;
    }
    public void setCheckoutList(JsonObject checkoutList) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(checkoutList);
        editor.putString(UserPrefs.checkoutList, jsonObject);
        editor.commit();
    }

    public JsonObject getCheckoutList() {
        String json = settings.getString(UserPrefs.checkoutList, "");
        Gson gson = new Gson();
        JsonObject checkoutList = gson.fromJson(json, JsonObject.class);
        return checkoutList;
    }
    public void setDefaultAddress(UserAddressList data) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(data);
        editor.putString("setDefaultAddress", jsonObject);
        editor.commit();
    }
    public UserAddressList getDefaultAddress() {
        String json = settings.getString("setDefaultAddress", "");
        Gson gson = new Gson();
        UserAddressList userAddressList = gson.fromJson(json, UserAddressList.class);
        return userAddressList;
    }
    public void setAppSettingsPreferenceObject(AppSettingsResponse appSettingsResponse) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(appSettingsResponse);
        editor.putString(this.AppSettings, jsonObject);
        editor.commit();
    }

    public AppSettingsResponse getAppSettingsPreferenceObjectJson() {
        String json = settings.getString(this.AppSettings, "");
        Gson gson = new Gson();
        AppSettingsResponse appSettingsResponse = gson.fromJson(json, AppSettingsResponse.class);
        return appSettingsResponse;
    }
    public void setTotalCart(String numberOfCarts) {
        editor.putString("numberOfCarts", numberOfCarts);
        editor.commit();
    }
    public Integer getTotalCart() {
        String numberOfCarts = settings.getString("numberOfCarts", "");
        Integer carts = Integer.parseInt(numberOfCarts);
        return carts;
    }
//
//    public void setUpdateData(UpdateAppModel body, String key){
//        System.out.println("dfhghjdfhgjchfbjhcjhb "+body);
//        Gson gson = new Gson();
//        String jsonObject = gson.toJson(body);
//        editor.putString(key, jsonObject);
//        editor.commit();
//    }
//    public UpdateAppModel getUpdateData(String key) {
//        String json = settings.getString(key, "");
//        Gson gson = new Gson();
//        UpdateAppModel updateAppModel = gson.fromJson(json, UpdateAppModel.class);
//        return updateAppModel;
//    }

    public void clearPreference(){
        editor.clear().commit();
    }



}
