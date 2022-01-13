package com.apps.onlinegroceriesapps.fragment.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.onlinegroceriesapps.adapter.order.OrderListAdapter;
import com.apps.onlinegroceriesapps.api.CommanApiCall.OrderCommonApiCall;
import com.apps.onlinegroceriesapps.api.network.response.OrderHistoryResponse;
import com.apps.onlinegroceriesapps.databinding.CommonOrderTrackLayoutBinding;
import com.apps.onlinegroceriesapps.models.OrderHistory;
import com.apps.onlinegroceriesapps.models.UserModel;
import com.apps.onlinegroceriesapps.utils.GenericSwipeRefresh;
import com.apps.onlinegroceriesapps.utils.LoadingSpinner;
import com.apps.onlinegroceriesapps.utils.UserPrefs;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;
import com.apps.onlinegroceriesapps.utils.Util;
import com.apps.onlinegroceriesapps.utils.listeners.EndlessRecyclerOnScrollListener;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;

public class CancelOrderFragment extends Fragment {
    CommonOrderTrackLayoutBinding binding;
    GenericSwipeRefresh genericSwipeRefresh;
    OrderListAdapter orderListAdapter;
    LoadingSpinner loadingSpinner;
    UserResponseHelper helper;
    UserPrefs userPrefs ;
    UserModel userModel ;
    private final List<OrderHistory> orderHistories = new ArrayList<>();
    private OrderHistoryResponse orderHistoryResponse = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = CommonOrderTrackLayoutBinding.inflate(getLayoutInflater(),container,false);
        Initialization();
        ClickMethod();
        OrderTrackDetails("",true);
        setupRecyclerView();
        return binding.getRoot();
    }

    private void Initialization() {
        loadingSpinner = new LoadingSpinner(requireContext());
        helper = new UserResponseHelper(requireContext());
        userPrefs = new UserPrefs(requireContext());
        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);

        genericSwipeRefresh = new GenericSwipeRefresh(() -> {
            orderHistories.clear();
            OrderTrackDetails("",true);
            setupRecyclerView();
        }, requireContext(), binding.SwipeRefresh);
        binding.SwipeRefresh.setOnRefreshListener(genericSwipeRefresh);
    }

    private void ClickMethod() {
    }
    public void addDataToList(OrderHistoryResponse productListingResponse){
        if (productListingResponse != null && productListingResponse.getMeta() != null && !productListingResponse.getMeta().getCurrentPage().equals(productListingResponse.getMeta().getLastPage())){
            OrderTrackDetails(productListingResponse.getLinks().getNext(),true);

        }
    }
    private void OrderTrackDetails(String url, boolean isLoadingMore) {
        new OrderCommonApiCall(requireContext(), new OrderCommonApiCall.HistoryOrderApiCall() {
            @Override
            public void CallBack(OrderHistoryResponse body) {
                if(!isLoadingMore) {
                    loadingSpinner.cancelLoading();
                }

                orderHistoryResponse = body;
//                if(!response.isSuccessful()){
//                    helper.showError("Some thing going wrong try after some time");
//                    return;
//                }
                if(body.getData().size()==0){
                    binding.noDataFound.noDataFound.setVisibility(View.VISIBLE);
                    return;
                }
                binding.noDataFound.noDataFound.setVisibility(View.GONE);
                //set product data
                orderHistories.addAll(body.getData());
                System.out.println("loading canceled");
                orderListAdapter.notifyDataSetChanged();
            }
        }).getOrderHistory(loadingSpinner,userModel,url,"cancel");
    }
    private void setupRecyclerView(){
        orderListAdapter = new OrderListAdapter(requireContext(), orderHistories);

        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
        binding.orderList.setLayoutManager(horizontalLayoutManager);
        binding.orderList.addItemDecoration( new LayoutMarginDecoration( 1,  Util.convertDpToPx(requireContext(), 10)) );

        binding.orderList.setAdapter(orderListAdapter);

        binding.orderList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                addDataToList(orderHistoryResponse);
            }
        });

    }
}