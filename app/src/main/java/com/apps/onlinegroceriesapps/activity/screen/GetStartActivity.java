package com.apps.onlinegroceriesapps.activity.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.apps.onlinegroceriesapps.activity.LoginActivity;
import com.apps.onlinegroceriesapps.databinding.ActivityGetStartBinding;

public class GetStartActivity extends AppCompatActivity {

    ActivityGetStartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetStartBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());
        binding.welcomeToGetStart.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}