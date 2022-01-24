package com.apps.onlinegroceriesworld.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.apps.onlinegroceriesworld.adapter.NotificationAdapter;
import com.apps.onlinegroceriesworld.api.CommanApiCall.NotificationCommonApiCall;
import com.apps.onlinegroceriesworld.api.network.response.NotificationResponse;
import com.apps.onlinegroceriesworld.databinding.ActivityNotificationBinding;
import com.apps.onlinegroceriesworld.models.Notification;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.apps.onlinegroceriesworld.utils.BottomSheet.NotificationDetails;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.apps.onlinegroceriesworld.utils.UserPrefs;
import com.apps.onlinegroceriesworld.utils.UserResponseHelper;
import com.google.gson.JsonObject;

public class NotificationActivity extends AppCompatActivity {

    ActivityNotificationBinding binding;
    UserPrefs userPrefs ;
    UserModel userModel ;
    UserResponseHelper helper;
    LoadingSpinner loadingSpinner;
    NotificationAdapter notificationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialized();
        getNotifications();
    }

    private void initialized() {
        helper = new UserResponseHelper(this);
        loadingSpinner = new LoadingSpinner(this);
        userPrefs = new UserPrefs(this);
        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
        binding.include.title.setText("Notifications");
    }

    private void getNotifications() {
        new NotificationCommonApiCall(this, new NotificationCommonApiCall.NotificationApiCallInterfaces() {
            @Override
            public void CallBack(NotificationResponse body) {
                notificationAdapter = new NotificationAdapter(NotificationActivity.this, body.getData(), notification -> onViewNotification(notification));
                GridLayoutManager horizontalLayoutManager
                        = new GridLayoutManager(NotificationActivity.this, 1, GridLayoutManager.VERTICAL, false);
                binding.rv.setLayoutManager(horizontalLayoutManager);
                binding.rv.setAdapter(notificationAdapter);
            }
        }).getNotifications(loadingSpinner,userModel);
    }

    private void onViewNotification(Notification notification) {
        NotificationDetails notificationDetails = new NotificationDetails(this,notification);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",notification.getId());
        new NotificationCommonApiCall(this).NotificationViewed(userModel,jsonObject);
        notificationDetails.open();
    }
}