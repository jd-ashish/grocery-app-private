package com.apps.onlinegroceriesworld.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.apps.onlinegroceriesworld.adapter.BestSellingAdapter;
import com.apps.onlinegroceriesworld.api.ApiClient;
import com.apps.onlinegroceriesworld.api.CommanApiCall.CartCommonApiCall;
import com.apps.onlinegroceriesworld.api.interfaces.CartInterfaces;
import com.apps.onlinegroceriesworld.api.network.response.ProductResponse;
import com.apps.onlinegroceriesworld.api.network.services.ShopFragmentInterfaces;
import com.apps.onlinegroceriesworld.databinding.ActivitySearchBinding;
import com.apps.onlinegroceriesworld.models.Products;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.apps.onlinegroceriesworld.utils.BottomSheet.ProductListFilter;
import com.apps.onlinegroceriesworld.utils.GenericRecyclerViewScroll;
import com.apps.onlinegroceriesworld.utils.GenericSwipeRefresh;
import com.apps.onlinegroceriesworld.utils.GenericTextWatcher;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.apps.onlinegroceriesworld.utils.UserPrefs;
import com.apps.onlinegroceriesworld.utils.UserResponseHelper;
import com.apps.onlinegroceriesworld.utils.Util;
import com.apps.onlinegroceriesworld.utils.listeners.EndlessRecyclerOnScrollListener;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements CartInterfaces.BestSelling{

    ActivitySearchBinding binding;
    ProductListFilter productListFilter;
    BestSellingAdapter bestSellingAdapter;
    private ShopFragmentInterfaces apiService;
    UserResponseHelper helper;
    LoadingSpinner loadingSpinner;
    GenericSwipeRefresh genericSwipeRefresh;
    GenericRecyclerViewScroll genericRecyclerViewScroll;
    CartCommonApiCall cartCommonApiCall;
    private List<Products> mProducts = new ArrayList<>();
    private ProductResponse productListingResponse = null;
    UserPrefs userPrefs ;
    UserModel userModel ;
    Call<ProductResponse> call = null;
    String search = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(binding.getRoot());
        initialized();
        getProducts("",false);
        setupRecyclerView();
        clickMethod();
    }

    private void clickMethod() {


        binding.search.addTextChangedListener(new GenericTextWatcher(() -> search(binding.search.getText().toString())));



    }

    private void search(String toString) {
        search = toString;
        mProducts.clear();
        call = apiService.search("popularity",toString);
        getProducts("filter",true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initialized() {
        helper = new UserResponseHelper(this);
        loadingSpinner = new LoadingSpinner(this);
        apiService = ApiClient.getClient().create(ShopFragmentInterfaces.class);
        genericSwipeRefresh = new GenericSwipeRefresh(() -> {
            mProducts.clear();
            getProducts("",false);

        }, this, binding.SwipeRefresh);
        binding.SwipeRefresh.setOnRefreshListener(genericSwipeRefresh);


        binding.products.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                addDataToList(productListingResponse);
            }
        });

        userPrefs = new UserPrefs(this);
        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
        cartCommonApiCall = new CartCommonApiCall(this, body -> {
            if(body.isError()){
                helper.showError(body.getMessage());
            }else{
                helper.showSuccess(body.getMessage());
            }
        });

    }
    public void addDataToList(ProductResponse productListingResponse){
        if (productListingResponse != null && productListingResponse.getMeta() != null && !productListingResponse.getMeta().getCurrentPage().equals(productListingResponse.getMeta().getLastPage())){
            getProducts(productListingResponse.getLinks().getNext(),true);

        }
    }
    private void getProducts(String type,boolean isLoadingMore){
        if(!isLoadingMore){
            loadingSpinner.showLoading();
        }else{
            binding.linearProgressIndicator.setVisibility(View.VISIBLE);
        }


        if(type.equals("filter")){

        }else{
            if(type.equals("url")){
                call = apiService.getProductByUrl(productListingResponse.getLinks().getNext());
            }else {
                call = apiService.search("popularity","");
            }
        }


        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if(!isLoadingMore){
                    loadingSpinner.cancelLoading();
                }else{
                    binding.linearProgressIndicator.setVisibility(View.GONE);
                }

                productListingResponse = response.body();
                if(!response.isSuccessful()){
                    helper.showError("Some thing going wrong try after some time");
                    return;
                }
                if(response.body().getData().size()==0){
                    binding.noDataFound.noDataFound.setVisibility(View.VISIBLE);
                    return;
                }
                binding.noDataFound.noDataFound.setVisibility(View.GONE);
                //set product data
                mProducts.addAll(response.body().getData());
                System.out.println("loading canceled");
                bestSellingAdapter.notifyDataSetChanged();
                if (mProducts.size() <= 0){
                    System.out.println("products_empty_text show");
                }
//                setupRecyclerView(response.body().getData());
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                loadingSpinner.cancelLoading();
                System.out.println("error "+t.getMessage());
            }
        });
    }

    //    private void setupRecyclerView(List<Products> data){
//        if(data.size()==0){
//            binding.noDataFound.noDataFound.setVisibility(View.VISIBLE);
//            return;
//        }
//        binding.noDataFound.noDataFound.setVisibility(View.GONE);
//        bestSellingAdapter = new BestSellingAdapter(ProductListActivity.this,data);
//        GridLayoutManager horizontalLayoutManager
//                = new GridLayoutManager(ProductListActivity.this, 2, GridLayoutManager.VERTICAL, false);
//        binding.products.setLayoutManager(horizontalLayoutManager);
////        binding.exclusiveOffer.setNestedScrollingEnabled(true);
//        binding.products.addItemDecoration( new LayoutMarginDecoration( 2,  Util.convertDpToPx(ProductListActivity.this, 10)) );
//        binding.products.setAdapter(bestSellingAdapter);
//
//    }
    private void setupRecyclerView(){
        bestSellingAdapter = new BestSellingAdapter(this, mProducts,this);

        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(SearchActivity.this, 2, GridLayoutManager.VERTICAL, false);
        binding.products.setLayoutManager(horizontalLayoutManager);
        //adapter.setClickListener(this);
        binding.products.addItemDecoration( new LayoutMarginDecoration( 2,  Util.convertDpToPx(this, 10)) );

        binding.products.setAdapter(bestSellingAdapter);

        binding.products.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                addDataToList(productListingResponse);
            }
        });

        System.out.println("show_loading_after_recycler_view");

    }

    @Override
    public void SetCartQty(Products products) {
        userPrefs.setTotalCart(String.valueOf(userPrefs.getTotalCart()+1));
        new Util(this).QBadgeView(String.valueOf(userPrefs.getTotalCart()));

        if(products.getPerUnit()!=null){
            cartCommonApiCall.updateCart(loadingSpinner, products.getId(),Util.nullString(products.getPerUnit().getId()),userModel);
        }else{
            cartCommonApiCall.updateCart(loadingSpinner, products.getId(),"0",userModel);
        }
    }
}