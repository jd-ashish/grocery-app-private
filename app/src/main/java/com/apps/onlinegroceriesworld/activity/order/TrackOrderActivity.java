package com.apps.onlinegroceriesworld.activity.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.apps.onlinegroceriesworld.activity.OrdersActivity;
import com.apps.onlinegroceriesworld.adapter.order.OrderListAdapter;
import com.apps.onlinegroceriesworld.api.CommanApiCall.OrderCommonApiCall;
import com.apps.onlinegroceriesworld.api.network.response.OrderHistoryResponse;
import com.apps.onlinegroceriesworld.databinding.ActivityTrackOrderBinding;
import com.apps.onlinegroceriesworld.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesworld.models.OrderHistory;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.apps.onlinegroceriesworld.utils.UserPrefs;
import com.apps.onlinegroceriesworld.utils.UserResponseHelper;
import com.apps.onlinegroceriesworld.utils.Util;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;

public class TrackOrderActivity extends AppCompatActivity {

    ActivityTrackOrderBinding binding;
    String code;
    UserResponseHelper helper;
    LoadingSpinner loadingSpinner;
    UserPrefs userPrefs ;
    OrderListAdapter orderListAdapter;
    UserModel userModel ;
    private final List<OrderHistory> orderHistories = new ArrayList<>();
    private OrderHistoryResponse orderHistoryResponse = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        code = getIntent().getStringExtra("code");
        Initialization();

        UserDefinedMethod();
        setupRecyclerView();
        ClickMethod();
//        Parcelable[] od = getIntent().getParcelableArrayExtra("getOrderDetails");
//        System.out.println("gshjhfljoidsgsdbklnjxbhjz "+new Gson().toJson(getIntent().getSerializableExtra("getOrderDetails")));
    }

    private void ClickMethod() {
        binding.orderCanceled.setOnClickListener(v -> {
            helper.showConfirm("Are sure ?");
            helper.getConfirmOk().setOnClickListener(view -> {
                new OrderCommonApiCall(this, new OrderCommonApiCall.PlaceOrderApiCall() {
                    @Override
                    public void CallBack(CommonGlobalMessageModel body) {
                        helper.cancelConfirm();
                        if(body.isError()){
                            helper.showError(body.getMessage());
                        }else{
                            startActivity(new Intent(TrackOrderActivity.this, OrdersActivity.class));
                            helper.showSuccess(body.getMessage());
                        }
                    }
                }).CancelOrders(loadingSpinner,userModel,code);
            });
        });
    }

    private void Initialization() {
        helper = new UserResponseHelper(this);
        loadingSpinner = new LoadingSpinner(this);
        userPrefs = new UserPrefs(this);
        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
        binding.toolbar.title.setText("Track Order Details");
    }
    private void setupRecyclerView(){
        orderListAdapter = new OrderListAdapter(this, orderHistories,"hide");



        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        binding.orderList.setLayoutManager(horizontalLayoutManager);
        binding.orderList.addItemDecoration( new LayoutMarginDecoration( 2,  Util.convertDpToPx(this, 10)) );

        binding.orderList.setAdapter(orderListAdapter);


    }
    private void UserDefinedMethod() {
        new OrderCommonApiCall(this, new OrderCommonApiCall.HistoryOrderApiCall() {
            @Override
            public void CallBack(OrderHistoryResponse body) {
                loadingSpinner.cancelLoading();

                orderHistoryResponse = body;
//                if(!response.isSuccessful()){
//                    helper.showError("Some thing going wrong try after some time");
//                    return;
//                }
//                if(body.getData().size()==0){
//                    binding.noDataFound.noDataFound.setVisibility(View.VISIBLE);
//                    return;
//                }
//                binding.noDataFound.noDataFound.setVisibility(View.GONE);
                //set product data
                binding.name.setText("Name: "+Util.nullString(body.getData().get(0).getUser().getName()));
                binding.phone.setText("Phone number: "+Util.nullString(body.getData().get(0).getUser().getPhone()));
                binding.address.setText("Address: "+Util.nullString(body.getData().get(0).getShippingAddress().getAddress()));
                binding.orderCurrentStatus.setText(Util.StringModify(body.getData().get(0).getOrderDetails().get(0).getDeliveryStatus()));
                binding.orderlastUpdate.setText(Util.nullString(body.getData().get(0).getOrderDetails().get(0).getUpdated_at()));
                orderHistories.addAll(body.getData());
                System.out.println("loading canceled");
                orderListAdapter.notifyDataSetChanged();
                if (orderHistories.size() <= 0){
                    System.out.println("products_empty_text show");
                }
            }
        }).orderOrderByCode(loadingSpinner,userModel,code);
    }
}