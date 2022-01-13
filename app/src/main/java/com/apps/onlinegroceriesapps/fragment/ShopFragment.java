package com.apps.onlinegroceriesapps.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.onlinegroceriesapps.activity.SearchActivity;
import com.apps.onlinegroceriesapps.adapter.BestSellingAdapter;
import com.apps.onlinegroceriesapps.adapter.ExclusiveOfferAdapter;
import com.apps.onlinegroceriesapps.api.ApiClient;
import com.apps.onlinegroceriesapps.api.CommanApiCall.CartCommonApiCall;
import com.apps.onlinegroceriesapps.api.interfaces.CartInterfaces;
import com.apps.onlinegroceriesapps.api.network.response.GetExclusiveOfferCardResponse;
import com.apps.onlinegroceriesapps.api.network.response.ImageSlider;
import com.apps.onlinegroceriesapps.api.network.response.ProductResponse;
import com.apps.onlinegroceriesapps.api.network.services.SettingInterfaces;
import com.apps.onlinegroceriesapps.api.network.services.ShopFragmentInterfaces;
import com.apps.onlinegroceriesapps.databinding.FragmentShopBinding;
import com.apps.onlinegroceriesapps.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesapps.models.Products;
import com.apps.onlinegroceriesapps.models.UserAddressList;
import com.apps.onlinegroceriesapps.models.UserModel;
import com.apps.onlinegroceriesapps.utils.BottomSheet.AddressList;
import com.apps.onlinegroceriesapps.utils.LoadingSpinner;
import com.apps.onlinegroceriesapps.utils.UserPrefs;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;
import com.apps.onlinegroceriesapps.utils.Util;
import com.apps.onlinegroceriesapps.utils.helper.SliderAdapter;
import com.apps.onlinegroceriesapps.utils.helper.SliderItem;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopFragment extends Fragment implements CartInterfaces.BestSelling {


    FragmentShopBinding binding;
    SliderAdapter adapter;
    ExclusiveOfferAdapter exclusiveOfferAdapter;
    BestSellingAdapter bestSellingAdapter;
    UserResponseHelper helper;
    private ShopFragmentInterfaces apiService;
    private SettingInterfaces settingInterfaces;
    CartCommonApiCall cartCommonApiCall;
    LoadingSpinner loadingSpinner;
    List<String> sliderImage;
    UserPrefs userPrefs ;
    UserModel userModel ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShopBinding.inflate(getLayoutInflater(),container,false);


        initialized();
        renewItems();
        setupSliderView();


//        getExclusiveOffer();
        if(userPrefs.getAppSettingsPreferenceObjectJson()!=null){
            if(userPrefs.getAppSettingsPreferenceObjectJson().getData().get(0).getExclusiveOfferType().equals("card")){
                getCardExclusiveOffer();
            }else{
                getExclusiveOffer();
            }
        }
        BestSelling();

        return binding.getRoot();
    }

    private void getCardExclusiveOffer() {
        Call<GetExclusiveOfferCardResponse> call = apiService.getExeclusiveOfferCard();

        call.enqueue(new Callback<GetExclusiveOfferCardResponse>() {
            @Override
            public void onResponse(Call<GetExclusiveOfferCardResponse> call, Response<GetExclusiveOfferCardResponse> response) {
                exclusiveOfferAdapter = new ExclusiveOfferAdapter(getContext(), response.body().getData());
                GridLayoutManager horizontalLayoutManager
                        = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
                binding.exclusiveOfferCard.setLayoutManager(horizontalLayoutManager);
                binding.exclusiveOfferCard.addItemDecoration( new LayoutMarginDecoration( 1,  Util.convertDpToPx(getContext(), 10)) );
                binding.exclusiveOfferCard.setAdapter(exclusiveOfferAdapter);
            }

            @Override
            public void onFailure(Call<GetExclusiveOfferCardResponse> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });
    }

    private void initialized() {
        apiService = ApiClient.getClient().create(ShopFragmentInterfaces.class);
        settingInterfaces = ApiClient.getClient().create(SettingInterfaces.class);
        adapter = new SliderAdapter(getContext());
        helper = new UserResponseHelper(requireContext());
        loadingSpinner = new LoadingSpinner(requireContext());
        userPrefs = new UserPrefs(requireContext());
        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
        cartCommonApiCall = new CartCommonApiCall(requireContext(), body -> {
            if(body.isError()){
                helper.showError(body.getMessage());
            }else{
                helper.showSuccess(body.getMessage());
            }
        });
        if(userPrefs.getDefaultAddress()!=null){
            binding.myLocation.setText(userPrefs.getDefaultAddress().getKnownName());
        }
        binding.myLocation.setOnClickListener(v -> {
            if(userModel==null){
                helper.showLogin();
                return;
            }
            if(userModel!=null){
                new AddressList(requireContext(), loadingSpinner, this::setAddressData).open();
            }else{
                binding.myLocation.setText("Login");
            }
        });

        binding.searchLayout.setOnClickListener(v -> startActivity(new Intent(requireContext(), SearchActivity.class)));
    }

    private void setAddressData(UserAddressList data) {
        binding.myLocation.setText(data.getKnownName());
    }

    private void setupSliderView() {
        binding.SliderView.imageSlider.setSliderAdapter(adapter);
        binding.SliderView.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.SliderView.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.SliderView.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.SliderView.imageSlider.setIndicatorSelectedColor(Color.WHITE);
        binding.SliderView.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        binding.SliderView.imageSlider.setScrollTimeInSec(3);
        binding.SliderView.imageSlider.setAutoCycle(true);
        binding.SliderView.imageSlider.startAutoCycle();


        binding.SliderView.imageSlider.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + binding.SliderView.imageSlider.getCurrentPagePosition());
            }
        });
    }

    public void renewItems() {
        List<SliderItem> sliderItemList = new ArrayList<>();
        Call<ImageSlider> call = settingInterfaces.getImageSlider();

        call.enqueue(new Callback<ImageSlider>() {
            @Override
            public void onResponse(Call<ImageSlider> call, Response<ImageSlider> response) {
//                SliderItem sliderItem = new SliderItem();
//                sliderItem.addAll(response.body().getData().get(0).getPhotos());
                sliderImage = response.body().getData().get(0).getPhotos();
                if(sliderImage.size() > 0) {
                    for (int i = 0; i < sliderImage.size(); i++) {
                        SliderItem sliderItem = new SliderItem();
                        sliderItem.setDescription("");
                        sliderItem.setImageUrl(sliderImage.get(i));
                        sliderItemList.add(sliderItem);
                    }
                    adapter.renewItems(sliderItemList);
                }else{
                    binding.SliderView.getRoot().setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ImageSlider> call, Throwable t) {
                System.out.println("error "+t.getMessage());
                binding.SliderView.getRoot().setVisibility(View.GONE);
            }
        });

    }
    private void getExclusiveOffer() {

        Call<ProductResponse> call = apiService.getExeclusiveOffer();

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                bestSellingAdapter = new BestSellingAdapter(getContext(),response.body().getData(),ShopFragment.this);
                GridLayoutManager horizontalLayoutManager
                        = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                binding.exclusiveOffer.setLayoutManager(horizontalLayoutManager);
                binding.exclusiveOffer.addItemDecoration( new LayoutMarginDecoration( 2,  Util.convertDpToPx(getContext(), 10)) );
                binding.exclusiveOffer.setAdapter(bestSellingAdapter);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });

    }
    private void BestSelling() {
        Call<ProductResponse> call = apiService.getBestSelling();

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                bestSellingAdapter = new BestSellingAdapter(getContext(),response.body().getData(),ShopFragment.this);
                GridLayoutManager horizontalLayoutManager
                        = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                binding.bestSelling.setLayoutManager(horizontalLayoutManager);
                binding.bestSelling.addItemDecoration( new LayoutMarginDecoration( 2,  Util.convertDpToPx(getContext(), 10)) );
                binding.bestSelling.setAdapter(bestSellingAdapter);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });
    }

    @Override
    public void SetCartQty(Products products) {
        if(userModel==null){
            helper.showLogin();
            return;
        }
        userPrefs.setTotalCart(String.valueOf(userPrefs.getTotalCart()+1));
        new Util(requireContext()).QBadgeView(String.valueOf(userPrefs.getTotalCart()));




        if(products.getPerUnit()!=null){
            cartCommonApiCall.updateCart(loadingSpinner, products.getId(),Util.nullString(products.getPerUnit().getId()),userModel);
        }else{
            cartCommonApiCall.updateCart(loadingSpinner, products.getId(),"0",userModel);
        }
    }
}