package com.apps.onlinegroceriesapps.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.activity.order.SuccessActivity;
import com.apps.onlinegroceriesapps.api.CommanApiCall.OrderCommonApiCall;
import com.apps.onlinegroceriesapps.databinding.ActivityMainBinding;
import com.apps.onlinegroceriesapps.fragment.AccountFragment;
import com.apps.onlinegroceriesapps.fragment.CartFragment;
import com.apps.onlinegroceriesapps.fragment.ExploreFragment;
import com.apps.onlinegroceriesapps.fragment.ShopFragment;
import com.apps.onlinegroceriesapps.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesapps.models.UserModel;
import com.apps.onlinegroceriesapps.utils.LoadingSpinner;
import com.apps.onlinegroceriesapps.utils.UserPrefs;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;
import com.apps.onlinegroceriesapps.utils.Util;
import com.cashfree.pg.CFPaymentService;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.razorpay.PaymentResultListener;
import com.tbuonomo.morphbottomnavigation.MorphBottomNavigationView;

import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {
    final Fragment shopFragment = new ShopFragment();
    Fragment cartFragment = new CartFragment();
    Fragment exploreFragment = new ExploreFragment();
    Fragment accountFragment = new AccountFragment();
    final FragmentManager fm = getSupportFragmentManager();
    //for active fragments
    Fragment active = shopFragment;
    public static ActivityMainBinding binding;
    private long pressedTime;
    UserPrefs userPrefs ;
    UserModel userModel ;
    LoadingSpinner loadingSpinner;
    UserResponseHelper helper;
    public static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialization();

        bottomNavigationViewSetup();







        FragmentSetup();

//        new Util(this).QBadgeView("5");
    }

    private void initialization() {
        loadingSpinner = new LoadingSpinner(this);
        helper = new UserResponseHelper(this);
        userPrefs = new UserPrefs(this);
        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
    }


    private void FragmentSetup() {
//        fm.beginTransaction().add(R.id.container, exploreFragment, "explore").hide(exploreFragment).commit();
        fm.beginTransaction().add(R.id.container, cartFragment, "cart").hide(cartFragment).commit();
//        fm.beginTransaction().add(R.id.container, accountFragment, "account").hide(accountFragment).commit();
        fm.beginTransaction().add(R.id.container, shopFragment, "shop").commit();

        loadFragment(shopFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getExtras() != null){
            String position = getIntent().getStringExtra("form_to");

//            CustomToast.showToast(this, message, R.color.colorSuccess);
//            String pos = getIntent().get("account");

            if(position.equals("explore")){
                loadFragment(exploreFragment);
            }else if(position.equals("account")){
                loadFragment(accountFragment);
            }
        }
    }

    private void bottomNavigationViewSetup() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shop:
                        loadFragment(shopFragment);
                        break;
                    case R.id.explore:
                        loadFragment(exploreFragment);
                        break;
                    case R.id.cart:
                        loadFragment(cartFragment);
                        break;
                    case R.id.account:
                        loadFragment(accountFragment);
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment){
        if(fragment != null){
            if(fragment==exploreFragment){
                exploreFragment = new ExploreFragment();
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
                fm.beginTransaction().add(R.id.container,exploreFragment,"explore").hide(exploreFragment).commitAllowingStateLoss();
                fm.beginTransaction().hide(active).show(exploreFragment).commitAllowingStateLoss();
                active = exploreFragment;
            }else
            if(fragment==cartFragment){
                cartFragment = new CartFragment();
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
                fm.beginTransaction().add(R.id.container,cartFragment,"cart").hide(cartFragment).commitAllowingStateLoss();
                fm.beginTransaction().hide(active).show(cartFragment).commitAllowingStateLoss();
                active = cartFragment;
            }else
            if(fragment==accountFragment){
                accountFragment = new AccountFragment();
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
                fm.beginTransaction().add(R.id.container,accountFragment,"account").hide(accountFragment).commitAllowingStateLoss();
                fm.beginTransaction().hide(active).show(accountFragment).commitAllowingStateLoss();
                active = accountFragment;
            }else{
                fm.beginTransaction().hide(active).show(fragment).commit();
                active = fragment;
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    public void onPaymentSuccess(String s) {
        System.out.println("activity___main_datya"+new Gson().toJson(userPrefs.getCheckoutList()));

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address_id",userPrefs.getCheckoutList().get("address_id").getAsString());
        jsonObject.addProperty("payment_type","razorpay");
        jsonObject.addProperty("payment_status","paid");;
        jsonObject.addProperty("grand_total",userPrefs.getCheckoutList().get("grand_total").getAsString());
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("name","razorpay");
        jsonObject1.addProperty("tid",s);

        jsonObject.addProperty("payment_details",new Gson().toJson(jsonObject1));
        System.out.println("jsonObjectjsonObjectjsonObject "+new Gson().toJson(jsonObject));
        new OrderCommonApiCall(this, new OrderCommonApiCall.PlaceOrderApiCall() {
            @Override
            public void CallBack(CommonGlobalMessageModel body) {
                if(body.isError()){
                    helper.showError(body.getMessage());
                }else{
                    startActivity(new Intent(MainActivity.this, SuccessActivity.class));
                }
            }
        }).placeOrder(loadingSpinner,jsonObject,userModel);
    }

    @Override
    public void onPaymentError(int i, String s) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address_id",userPrefs.getCheckoutList().get("address_id").getAsString());
        jsonObject.addProperty("payment_type","cashfree");
        jsonObject.addProperty("payment_status","paid");
        jsonObject.addProperty("grand_total",userPrefs.getCheckoutList().get("grand_total").getAsString());
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("name","cashfree");




        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.d(TAG, "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle  bundle = data.getExtras();
            if (bundle != null)
                for (String  key  :  bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        jsonObject1.addProperty(key,bundle.getString(key));
                        Log.d(TAG, key + " : " + bundle.getString(key));
                        /*paymentMode : CREDIT_CARD
                        orderId : 54679855
                        txTime : 2021-12-22 16:29:40
                        referenceId : 1221177
                        type : CashFreeResponse
                        txMsg : Transaction Successful
                        signature : hI+jfvtZK2f0DeiccCpdmasVG7olFE9zc/m/GxT/fik=
                        orderAmount : 104.00
                        txStatus : SUCCESS*/
                    }
                }
        }
        jsonObject.addProperty("payment_details",new Gson().toJson(jsonObject1));
        new OrderCommonApiCall(this, new OrderCommonApiCall.PlaceOrderApiCall() {
            @Override
            public void CallBack(CommonGlobalMessageModel body) {
                if(body.isError()){
                    helper.showError(body.getMessage());
                }else{
                    startActivity(new Intent(MainActivity.this, SuccessActivity.class));
                }
            }
        }).placeOrder(loadingSpinner,jsonObject,userModel);

    }
}