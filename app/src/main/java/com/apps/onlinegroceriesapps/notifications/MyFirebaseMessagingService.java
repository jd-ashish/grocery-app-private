package com.apps.onlinegroceriesapps.notifications;

import android.content.Intent;
import android.util.Log;

import com.apps.onlinegroceriesapps.activity.MainActivity;
import com.apps.onlinegroceriesapps.activity.ProductDetailsActivity;
import com.apps.onlinegroceriesapps.activity.ProductListActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                System.out.println("=====n_response " + json.toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void sendPushNotification(JSONObject json) {
        try {

            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            String type = data.getString("type");
            String id = data.getString("id");

            Intent intent = null;


            if (type.toLowerCase().equals("account")) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("message", "");
                intent.putExtra("form_to", "account");

            } else if (type.equals("products")) {
                intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
//                intent.putExtra("message", "");
                intent.putExtra("id",id);
            }
            //else  if (type.equals("order_cancel")) {
//                intent = new Intent(getApplicationContext(), PurchaseHistoryActivity.class);
//                intent.putExtra("id", id);
//
//            }else if(type.toLowerCase().equals("scratch")){
//                intent = new Intent(getApplicationContext(), ScratchCardActivity.class);
//                intent.putExtra("id", id);
//            }else if(type.toLowerCase().equals("withdrawal_account")){
//                intent = new Intent(getApplicationContext(), WithdrawalActivity.class);
//                intent.putExtra("id", id);
//            }else if(type.toLowerCase().equals("new_products")){
//                intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("id", id);
//            }else{
//                intent = new Intent(getApplicationContext(), MainActivity.class);
//            }
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            if (imageUrl.equals("null") || imageUrl.equals("")) {
                mNotificationManager.showSmallNotification(title, message, intent);
            } else {
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }

            System.out.println("success");

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

// Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//
//        if (refreshedToken!=null) {
//            SettingPreferences.setStringValueInPref(this, SettingPreferences.REG_ID, refreshedToken);
//        }
//        UserPrefs userPrefs = new UserPrefs(getApplicationContext());
//        App.getInstance().setDeviceToken(s);
//        userPrefs.setDeviceToken(s);
        System.out.println("dhsjgdjsghjtoken "+s);
        //MainActivity.UpdateToken(s);
    }

}
