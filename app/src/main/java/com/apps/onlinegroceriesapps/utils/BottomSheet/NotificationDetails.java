package com.apps.onlinegroceriesapps.utils.BottomSheet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.apps.onlinegroceriesapps.databinding.BottomNotificationDetailsBinding;
import com.apps.onlinegroceriesapps.models.Notification;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class NotificationDetails {
    Context context;
    BottomNotificationDetailsBinding binding;
    Notification notification;
    public NotificationDetails(Context context , Notification notification) {
        this.context = context;
        this.notification = notification;
    }

    public void open(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        binding = BottomNotificationDetailsBinding.inflate(LayoutInflater.from(context));
        bottomSheetDialog.setContentView(binding.getRoot());

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        binding.date.setText(notification.getCreated_at());
        binding.title.setText(notification.getTitle());
        binding.desc.setText(notification.getDescription());
        if(notification.getImg()==null || notification.equals("")){
            binding.img.setVisibility(View.GONE);
        }else{
            binding.img.setVisibility(View.VISIBLE);
            Glide.with(context).load(notification.getImg()).into(binding.img);
        }
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.show();
    }
}
