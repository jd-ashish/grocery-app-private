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
import com.apps.onlinegroceriesworld.databinding.ActivityProductListBinding;
import com.apps.onlinegroceriesworld.models.Products;
import com.apps.onlinegroceriesworld.models.UserModel;
import com.apps.onlinegroceriesworld.utils.BottomSheet.ProductListFilter;
import com.apps.onlinegroceriesworld.utils.Constent;
import com.apps.onlinegroceriesworld.utils.FocusInput;
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

public class ProductListActivity extends AppCompatActivity implements CartInterfaces.BestSelling {

    ActivityProductListBinding binding;
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
        binding = ActivityProductListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialized();
        getProducts("",false);
        setupRecyclerView();
        clickMethod();
    }

    private void clickMethod() {
        binding.filterProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productListFilter = new ProductListFilter(ProductListActivity.this, new ProductListFilter.ProductListFilterInterfaces() {
                    @Override
                    public void onProductFilter(List<String> dateWise, List<String> priceWise) {
                        String date = "null";
                        String price = "null";

                        if(dateWise.size()>0){
                            if(dateWise.get(0).equalsIgnoreCase("Odl To New")){
                                date = "desc";
                            }
                            if(dateWise.get(0).equalsIgnoreCase("New To Old")){
                                date = "asc";
                            }
                        }
                        if(priceWise.size()>0){
                            if(priceWise.get(0).equalsIgnoreCase("High To Low")){
                                price = "desc";
                            }
                            if(priceWise.get(0).equalsIgnoreCase("Low To High")){
                                price = "asc";
                            }
                        }
                        if(price.equals("null") && date.equals("null")){
                            helper.showError("Please select filter");
                            return;
                        }
                        productListFilter.hide();
                        mProducts.clear();
                        if(getIntent().hasExtra("form")){
                            if(getIntent().hasExtra(Constent.CATEGORY_ID)){
                                call = apiService.getProductByFilter(getIntent().getStringExtra(Constent.CATEGORY_ID),price,date);
                                getProducts("filter",true);
                            }
                        }


                    }
                });
                productListFilter.open();
            }
        });
        binding.searchActions.setOnClickListener(v -> {
            binding.search.setVisibility(View.VISIBLE);
            new FocusInput(ProductListActivity.this,binding.search).focused();
        });


        binding.search.addTextChangedListener(new GenericTextWatcher(() -> search(binding.search.getText().toString())));



    }

    private void search(String toString) {
        search = toString;
        mProducts.clear();
        if(getIntent().hasExtra("form")){
            if(getIntent().hasExtra(Constent.CATEGORY_ID)){
                call = apiService.getProductByFilter(getIntent().getStringExtra(Constent.CATEGORY_ID),"popularity",toString);
                getProducts("filter",true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(binding.search.getVisibility()==View.VISIBLE){
            binding.search.setVisibility(View.GONE);
            return;
        }
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
                if(getIntent().hasExtra("form")){
                    if(getIntent().hasExtra(Constent.CATEGORY_ID)){
                        call = apiService.getProductByCategoryId(getIntent().getStringExtra(Constent.CATEGORY_ID));
                        binding.titles.setText(getIntent().getStringExtra(Constent.CATEGORY_NAME));
                    }
                    if(getIntent().hasExtra(Constent.PRODUCT_ID)){
                        call = apiService.getProductByExclusiveOfferId(getIntent().getStringExtra(Constent.PRODUCT_ID));
                        binding.titles.setText(getIntent().getStringExtra(Constent.TITLE));
                        binding.filterProducts.setVisibility(View.GONE);
                    }
                }
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
    private void setupRecyclerView(){
        bestSellingAdapter = new BestSellingAdapter(this, mProducts,this);

        GridLayoutManager horizontalLayoutManager
                = new GridLayoutManager(ProductListActivity.this, 2, GridLayoutManager.VERTICAL, false);
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
        if(userModel==null){
            helper.showLogin();
            return;
        }
        userPrefs.setTotalCart(String.valueOf(userPrefs.getTotalCart()+1));
        new Util(this).QBadgeView(String.valueOf(userPrefs.getTotalCart()));
        if(products.getPerUnit()!=null){
            cartCommonApiCall.updateCart(loadingSpinner, products.getId(),Util.nullString(products.getPerUnit().getId()),userModel);
        }else{
            cartCommonApiCall.updateCart(loadingSpinner, products.getId(),"0",userModel);
        }
    }
}