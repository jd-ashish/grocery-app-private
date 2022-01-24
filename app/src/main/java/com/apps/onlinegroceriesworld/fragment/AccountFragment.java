package com.apps.onlinegroceriesworld.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.onlinegroceriesworld.activity.LoginActivity;
import com.apps.onlinegroceriesworld.activity.MainActivity;
import com.apps.onlinegroceriesworld.activity.NotificationActivity;
import com.apps.onlinegroceriesworld.activity.OrdersActivity;
import com.apps.onlinegroceriesworld.activity.WebViewActivity;
import com.apps.onlinegroceriesworld.activity.profile.UpdateUserProfileActivity;
import com.apps.onlinegroceriesworld.api.ApiClient;
import com.apps.onlinegroceriesworld.api.CommanApiCall.LoginByPhoneApiCall;
import com.apps.onlinegroceriesworld.api.CommanApiCall.SettingCommonApiCall;
import com.apps.onlinegroceriesworld.api.network.services.LoginPhoneApiInterfaces;
import com.apps.onlinegroceriesworld.databinding.FragmentAccountBinding;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.apps.onlinegroceriesworld.utils.Constent;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.apps.onlinegroceriesworld.utils.UserPrefs;
import com.apps.onlinegroceriesworld.utils.UserResponseHelper;
import com.bumptech.glide.Glide;

public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    UserPrefs userPrefs;
    UserModel userModel;
    UserResponseHelper helper;
    LoadingSpinner loadingSpinner;
    private LoginPhoneApiInterfaces apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(getLayoutInflater(), container, false);
        initialized();
        ClickMethod();
        setUI();
        return binding.getRoot();
    }

    private void setUI() {

        if (userModel != null) {
            new LoginByPhoneApiCall(requireContext(), new LoginByPhoneApiCall.LoginInterfaces() {
                @Override
                public void onLogin(UserModel body) {
                    new UserPrefs(requireContext()).setAuthPreferenceObject(body, UserPrefs.users);
                    Glide.with(requireContext()).load(userModel.getUser().getAvatar()).into(binding.circleImageView);
                }
            }).loginByUserId(loadingSpinner, apiService, userModel);
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
        binding.aboutus.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), WebViewActivity.class).putExtra("form", "account")
                    .putExtra("title", "About Us")
                    .putExtra(Constent.ABOUT_US, userPrefs.getAppSettingsPreferenceObjectJson().getData().get(0).getAbout_us()));
        });
        binding.contactUs.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), WebViewActivity.class).putExtra("form", "account")
                    .putExtra("title", "Contact Us")
                    .putExtra(Constent.CONTACT_US, userPrefs.getAppSettingsPreferenceObjectJson().getData().get(0).getContact_us()));
        });
        binding.privacyPolicy.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), WebViewActivity.class).putExtra("form", "account")
                    .putExtra("title", "Privacy Policy")
                    .putExtra(Constent.PRIVACY_POLICY, userPrefs.getAppSettingsPreferenceObjectJson().getData().get(0).getPrivacy_policy()));
        });
        binding.termsConditions.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), WebViewActivity.class).putExtra("form", "account")
                    .putExtra("title", "Terms And Conditions")
                    .putExtra(Constent.TERMS_CONDITIONS, userPrefs.getAppSettingsPreferenceObjectJson().getData().get(0).getPrivacy_policy()));
        });
        binding.order.setOnClickListener(v -> {
            if (userModel == null) {
                helper.showLogin();
                return;
            }
            startActivity(new Intent(requireContext(), OrdersActivity.class));
        });
        binding.updateAccount.setOnClickListener(v -> {
            if (userModel == null) {
                helper.showLogin();
                return;
            }
            startActivity(new Intent(requireContext(), UpdateUserProfileActivity.class));
        });
        binding.notification.setOnClickListener(v -> {
            if (userModel == null) {
                helper.showLogin();
                return;
            }
            startActivity(new Intent(requireContext(), NotificationActivity.class));
        });

        binding.logout.setOnClickListener(v -> {
            if (userModel == null) {
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