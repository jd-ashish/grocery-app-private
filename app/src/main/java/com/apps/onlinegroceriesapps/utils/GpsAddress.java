package com.apps.onlinegroceriesapps.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.apps.onlinegroceriesapps.models.UserModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GpsAddress {
    double latitude,longitude;
    Context context;

    UserPrefs userPrefs;
    public GpsAddress(double latitude, double longitude, Context context) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
    }

    public Address access(){
        userPrefs = new UserPrefs(context);
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); //
        String longitude = String.valueOf(addresses.get(0).getLongitude()); //
        String lattitude = String.valueOf(addresses.get(0).getLatitude()); //
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address",addresses.get(0).getAddressLine(0));
        jsonObject.addProperty("city",city);
        jsonObject.addProperty("state",state);
        jsonObject.addProperty("country",country);
        jsonObject.addProperty("postalCode",postalCode);
        jsonObject.addProperty("knownName",knownName);
        jsonObject.addProperty("longitude",longitude);
        jsonObject.addProperty("lattitude",lattitude);
        UserModel userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
        jsonObject.addProperty("user_id", userModel.getUser().getId());
        new UserPrefs(context).setGpsAddress(jsonObject);
        System.out.println("sgdcvsgfvdgshfgdhsgfhjdghs "+new Gson().toJson(jsonObject));
//        userPrefs.setGpsAddress(gps_address.toString());

        return addresses.get(0);
//        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName(); //
//
//        return new Gson().toJson(addresses.get(0).toString());
//        return address+" === "+city+" === "+state+" === "+country+" === "+postalCode+" === "+knownName+" === "+addresses.get(0).ge();
    }
}
