package com.apps.onlinegroceriesworld.activity.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.apps.onlinegroceriesworld.activity.LoginActivity;
import com.apps.onlinegroceriesworld.databinding.ActivityGetStartBinding;

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