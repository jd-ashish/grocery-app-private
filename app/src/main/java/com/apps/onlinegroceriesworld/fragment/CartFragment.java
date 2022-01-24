package com.apps.onlinegroceriesworld.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.onlinegroceriesworld.activity.MainActivity;
import com.apps.onlinegroceriesworld.adapter.CartAdapter;
import com.apps.onlinegroceriesworld.api.ApiClient;
import com.apps.onlinegroceriesworld.api.CommanApiCall.CartCommonApiCall;
import com.apps.onlinegroceriesworld.api.interfaces.CartInterfaces;
import com.apps.onlinegroceriesworld.api.network.response.CartsResponse;
import com.apps.onlinegroceriesworld.api.network.services.CartFragmentInterfaces;
import com.apps.onlinegroceriesworld.databinding.FragmentCartBinding;
import com.apps.onlinegroceriesworld.models.Carts;
import com.apps.onlinegroceriesworld.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.apps.onlinegroceriesworld.utils.BottomSheet.Checkout;
import com.apps.onlinegroceriesworld.utils.GenericSwipeRefresh;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.apps.onlinegroceriesworld.utils.SwipeToDeleteCallback;
import com.apps.onlinegroceriesworld.utils.UserPrefs;
import com.apps.onlinegroceriesworld.utils.UserResponseHelper;
import com.apps.onlinegroceriesworld.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment implements CartInterfaces , CartInterfaces.CallBack  {


    CartAdapter cartAdapter;
    FragmentCartBinding binding;
    UserResponseHelper helper;
    LoadingSpinner loadingSpinner;
    double grand_total;
    GenericSwipeRefresh genericSwipeRefresh;
    private CartFragmentInterfaces apiService;
    UserPrefs userPrefs ;
    UserModel userModel ;
    CartCommonApiCall cartCommonApiCall;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(getLayoutInflater(),container,false);
        initialized();
        SwipeRefresh();

        GetCartList();

        ClickMethod();
        return binding.getRoot();
    }

    private void ClickMethod() {


        binding.gotoExplore.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), MainActivity.class)
            .putExtra("form","cart_fragment")
            .putExtra("form_to","explore"));
        });
        binding.gotoCheckout.setOnClickListener(v -> {
            if(userModel==null){
                helper.showLogin();
                return;
            }
            Checkout checkout = new Checkout(requireContext(), loadingSpinner, grand_total, getActivity());
            checkout.open();
        });
    }

    private void SwipeRefresh() {
        if(userModel==null){
            helper.showLogin();
            return;
        }
        genericSwipeRefresh = new GenericSwipeRefresh(this::GetCartList, requireContext(), binding.SwipeRefresh);
        binding.SwipeRefresh.setOnRefreshListener(genericSwipeRefresh);
    }

    private void initialized() {
        helper = new UserResponseHelper(requireContext());
        loadingSpinner = new LoadingSpinner(requireContext());
        apiService = ApiClient.getClient().create(CartFragmentInterfaces.class);
        userPrefs = new UserPrefs(requireContext());
        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
        cartCommonApiCall = new CartCommonApiCall(requireContext(),this);
    }

    private void GetCartList() {
        if(userModel==null){
//            helper.showLogin();
            return;
        }
        loadingSpinner.showLoading();
        Call<CartsResponse> call = apiService.getCarts(userModel.getToken_type()+" "+userModel.getAccess_token());

        call.enqueue(new Callback<CartsResponse>() {
            @Override
            public void onResponse(Call<CartsResponse> call, Response<CartsResponse> response) {
                loadingSpinner.cancelLoading();
                if(!response.isSuccessful()){
                    helper.showError("Some thing going wrong try after some time");
                    return;
                }
                userPrefs.setTotalCart(String.valueOf(response.body().getData().size()));
                new Util(requireContext()).QBadgeView(String.valueOf(response.body().getData().size()));
                setupRecyclerView(response.body().getData());
                calculatePrice(response.body().getData());
            }

            @Override
            public void onFailure(Call<CartsResponse> call, Throwable t) {
                loadingSpinner.cancelLoading();
                System.out.println("error "+t.getMessage());
            }
        });


    }

    private void calculatePrice(List<Carts> data) {
        double price = 0;
        for(int i=0; i< data.size(); i++){
            price += (data.get(i).getPrice()*data.get(i).getQty());
        }
        grand_total = price;
        binding.finalAmount.setText(Util.convertPrice(requireContext(),price));
    }

    private void setupRecyclerView(List<Carts> data) {
        if(data.size()==0){
            binding.emptyCartLayout.emptyCartLayout.setVisibility(View.VISIBLE);
            binding.gotoExplore.setVisibility(View.VISIBLE);
            binding.gotoCheckout.setVisibility(View.GONE);
            binding.finalAmount.setVisibility(View.GONE);
        }else{
            binding.emptyCartLayout.emptyCartLayout.setVisibility(View.GONE);
            binding.gotoExplore.setVisibility(View.GONE);
            binding.gotoCheckout.setVisibility(View.VISIBLE);
            binding.finalAmount.setVisibility(View.VISIBLE);
        }
        cartAdapter = new CartAdapter(getContext(),data,this);
        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        binding.myCart.setLayoutManager(horizontalLayoutManager);
        binding.myCart.setAdapter(cartAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(cartAdapter));
        itemTouchHelper.attachToRecyclerView(binding.myCart);
    }


    @Override
    public void onSetUpdateCartQty(Carts carts) {
        if(userModel==null){
            helper.showLogin();
            return;
        }
        if(carts.getProductStock().getId().equals("") || carts.getProductStock()==null){
            cartCommonApiCall.updateCart(loadingSpinner, carts.getProduct().getId(),"0",userModel);
        }else{
            cartCommonApiCall.updateCart(loadingSpinner, carts.getProduct().getId(),carts.getProductStock().getId(),userModel);
        }
    }

    @Override
    public void onSetNegativeUpdateCartQty(Carts carts) {
        if((int) carts.getQty() > 1){

            int qty = (int) carts.getQty() - 1;
            if(qty != 0){
                cartCommonApiCall.descCartQty(loadingSpinner,apiService,carts.getId(), String.valueOf(qty),userModel);
            }
        }
    }

    @Override
    public void onCartRemove(Carts carts) {
        userPrefs.setTotalCart(String.valueOf(userPrefs.getTotalCart()-1));
        new Util(requireContext()).QBadgeView(String.valueOf(userPrefs.getTotalCart()));

        cartCommonApiCall.removeCart(loadingSpinner,apiService,carts.getId(),userModel);
    }

    @Override
    public void onUpdateCartResult(CommonGlobalMessageModel body) {
        GetCartList();
        userPrefs.setTotalCart(String.valueOf(userPrefs.getTotalCart()+1));
        new Util(requireContext()).QBadgeView(String.valueOf(userPrefs.getTotalCart()));
        if(body.isError()){
            helper.showError(body.getMessage());
        }else{
            helper.showSuccess(body.getMessage());
        }
    }



}