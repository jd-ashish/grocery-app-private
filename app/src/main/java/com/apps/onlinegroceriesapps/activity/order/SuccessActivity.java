package com.apps.onlinegroceriesapps.activity.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.activity.MainActivity;
import com.apps.onlinegroceriesapps.databinding.ActivitySuccessBinding;

public class SuccessActivity extends AppCompatActivity {

    ActivitySuccessBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        clickMethod();
    }

    private void clickMethod() {
        binding.backToHome.setOnClickListener(v -> {
            startActivity(new Intent(SuccessActivity.this, MainActivity.class));
            finishAffinity();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SuccessActivity.this, MainActivity.class));
        finishAffinity();
    }
}