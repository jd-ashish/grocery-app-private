package com.apps.onlinegroceriesapps.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.databinding.ActivityWebViewBinding;
import com.apps.onlinegroceriesapps.utils.Constent;
import com.apps.onlinegroceriesapps.utils.LoadingSpinner;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;

public class WebViewActivity extends AppCompatActivity {

    ActivityWebViewBinding binding;
    UserResponseHelper helper;
    LoadingSpinner loadingSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialization();
        getIntentData();
    }

    private void initialization() {
        loadingSpinner = new LoadingSpinner(this);
        helper = new UserResponseHelper(this);
    }

    private void WebUrlSection() {
        //getIntent().getStringExtra("id")
        binding.web.setVerticalScrollBarEnabled(true);
        binding.web.loadDataWithBaseURL("", getIntent().getStringExtra("html"), "text/html", "UTF-8", "");
        binding.web.setBackgroundColor(getResources().getColor(R.color.white));
//        prgLoading.setVisibility(View.GONE);
    }
    private void getIntentData() {
        if(getIntent().hasExtra("form")){
            if(getIntent().hasExtra("title")){
                binding.textView3.setText(getIntent().getStringExtra("title"));
            }
            if(getIntent().hasExtra(Constent.ABOUT_US)){
                loadData(Constent.URL+"about-us");
            }
            if(getIntent().hasExtra(Constent.CONTACT_US)){
                loadData(Constent.URL+"contact-us");
            }
            if(getIntent().hasExtra(Constent.PRIVACY_POLICY)){
                loadData(Constent.URL+"privacy-policy");
            }
            if(getIntent().hasExtra(Constent.TERMS_CONDITIONS)){
                loadData(Constent.URL+"terms-and-conditions");
            }
        }
    }

    private void loadData(String url) {
        binding.web.setClickable(true);
        binding.web.setFocusableInTouchMode(true);
        binding.web.getSettings().setJavaScriptEnabled(true);
        binding.web.getSettings().setUseWideViewPort(true);
        binding.web.getSettings().setLoadWithOverviewMode(true);

        binding.web.setVerticalScrollBarEnabled(true);
        binding.web.loadUrl(url);
        binding.web.setBackgroundColor(getResources().getColor(R.color.white));
        binding.web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingSpinner.showLoading();
            }

            public void onPageFinished(WebView view, String url) {
                loadingSpinner.cancelLoading();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                loadingSpinner.cancelLoading();
            }
        });
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        super.onBackPressed();

    }
}