package com.apps.onlinegroceriesapps.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.activity.LoginActivity;
import com.apps.onlinegroceriesapps.activity.MainActivity;
import com.apps.onlinegroceriesapps.activity.NotificationActivity;
import com.apps.onlinegroceriesapps.activity.OrdersActivity;
import com.apps.onlinegroceriesapps.activity.SearchActivity;
import com.apps.onlinegroceriesapps.activity.SplashscreenActivity;
import com.apps.onlinegroceriesapps.activity.profile.UpdateUserProfileActivity;
import com.apps.onlinegroceriesapps.activity.screen.AddressSelectActivity;
import com.apps.onlinegroceriesapps.activity.screen.LoginOtpActivity;
import com.apps.onlinegroceriesapps.api.ApiClient;
import com.apps.onlinegroceriesapps.api.CommanApiCall.CartCommonApiCall;
import com.apps.onlinegroceriesapps.api.CommanApiCall.LoginByPhoneApiCall;
import com.apps.onlinegroceriesapps.api.CommanApiCall.SettingCommonApiCall;
import com.apps.onlinegroceriesapps.api.network.response.AppSettingsResponse;
import com.apps.onlinegroceriesapps.api.network.services.LoginPhoneApiInterfaces;
import com.apps.onlinegroceriesapps.api.network.services.SettingInterfaces;
import com.apps.onlinegroceriesapps.api.network.services.ShopFragmentInterfaces;
import com.apps.onlinegroceriesapps.databinding.FragmentAccountBinding;
import com.apps.onlinegroceriesapps.databinding.FragmentCartBinding;
import com.apps.onlinegroceriesapps.models.UserModel;
import com.apps.onlinegroceriesapps.utils.BottomSheet.AddressList;
import com.apps.onlinegroceriesapps.utils.GenericDelay;
import com.apps.onlinegroceriesapps.utils.LoadingSpinner;
import com.apps.onlinegroceriesapps.utils.UserPrefs;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;
import com.apps.onlinegroceriesapps.utils.helper.SliderAdapter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.Objects;

public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    UserPrefs userPrefs ;
    UserModel userModel ;
    UserResponseHelper helper;
    LoadingSpinner loadingSpinner;
    private LoginPhoneApiInterfaces apiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(getLayoutInflater(),container,false);
        initialized();
        ClickMethod();
        setUI();
        return binding.getRoot();
    }

    private void setUI() {

        if(userModel!=null){
            new LoginByPhoneApiCall(requireContext(), new LoginByPhoneApiCall.LoginInterfaces() {
                @Override
                public void onLogin(UserModel body) {
                    new UserPrefs(requireContext()).setAuthPreferenceObject(body,UserPrefs.users);
                    Glide.with(requireContext()).load(userModel.getUser().getAvatar()).into(binding.circleImageView);
                }
            }).loginByUserId(loadingSpinner,apiService,userModel);
        }

    }

    private void initialized() {
        userPrefs = new UserPrefs(requireContext());
        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
        apiService = ApiClient.getClient().create(LoginPhoneApiInterfaces.class);
        helper = new UserResponseHelper(requireContext());
        loadingSpinner = new LoadingSpinner(requireContext());
    }
    private void ClickMethod() {
        binding.order.setOnClickListener(v -> {
            if(userModel==null){
                helper.showLogin();
                return;
            }
            startActivity(new Intent(requireContext(), OrdersActivity.class));
        });
        binding.updateAccount.setOnClickListener(v -> {
            if(userModel==null){
                helper.showLogin();
                return;
            }
            startActivity(new Intent(requireContext(), UpdateUserProfileActivity.class));
        });
        binding.notification.setOnClickListener(v -> {
            if(userModel==null){
                helper.showLogin();
                return;
            }
            startActivity(new Intent(requireContext(), NotificationActivity.class));
        });

        binding.logout.setOnClickListener(v -> {
            if(userModel==null){
                startActivity(new Intent(requireContext(), LoginActivity.class));
                return;
            }
            helper.showConfirm("Are you sure ?");
            helper.getConfirmOk().setOnClickListener(view -> {
                userPrefs.clearPreference();
                loadingSpinner.showLoading();
                new SettingCommonApiCall(requireContext(), body -> {
                    userPrefs.setAppSettingsPreferenceObject(body);
                    loadingSpinner.cancelLoading();
                    startActivity(new Intent(requireContext(), MainActivity.class));
                    requireActivity().finishAffinity();
                }).getSettings();

            });
        });
    }
}