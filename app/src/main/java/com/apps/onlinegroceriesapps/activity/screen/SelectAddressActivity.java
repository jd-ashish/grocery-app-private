package com.apps.onlinegroceriesapps.activity.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.apps.onlinegroceriesapps.databinding.ActivitySelectAddressBinding;

public class SelectAddressActivity extends AppCompatActivity {

    ActivitySelectAddressBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}