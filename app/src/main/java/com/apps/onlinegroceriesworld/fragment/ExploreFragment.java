package com.apps.onlinegroceriesworld.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.onlinegroceriesworld.adapter.ExploreCategoryAdapter;
import com.apps.onlinegroceriesworld.api.ApiClient;
import com.apps.onlinegroceriesworld.api.network.response.CategoryResponse;
import com.apps.onlinegroceriesworld.api.network.services.CategoryExploreInterfaces;
import com.apps.onlinegroceriesworld.databinding.FragmentExploreBinding;
import com.apps.onlinegroceriesworld.utils.GenericTextWatcher;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.apps.onlinegroceriesworld.utils.Util;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExploreFragment extends Fragment {

    FragmentExploreBinding binding;
    ExploreCategoryAdapter exploreCategoryAdapter;
    private CategoryExploreInterfaces apiService;
    String search = "null";
    LoadingSpinner loadingSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(getLayoutInflater(),container,false);
        initialization();
        GetExploreList();

        binding.search.addTextChangedListener(new GenericTextWatcher(() -> search(binding.search.getText().toString())));
        return binding.getRoot();
    }

    private void search(String toString) {
        search = toString;
        GetExploreList();
    }

    private void initialization() {
        apiService = ApiClient.getClient().create(CategoryExploreInterfaces.class);
        loadingSpinner = new LoadingSpinner(getContext());
    }

    private void GetExploreList() {
        loadingSpinner.showLoading();
        Call<CategoryResponse> call = apiService.getCategoryExplore(search);

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                loadingSpinner.cancelLoading();
                exploreCategoryAdapter = new ExploreCategoryAdapter(getContext(),response.body().getData());
                GridLayoutManager horizontalLayoutManager
                        = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                binding.exclusiveOffer.setLayoutManager(horizontalLayoutManager);
//        binding.exclusiveOffer.setNestedScrollingEnabled(true);
                binding.exclusiveOffer.addItemDecoration( new LayoutMarginDecoration( 2,  Util.convertDpToPx(getContext(), 10)) );
                binding.exclusiveOffer.setAdapter(exploreCategoryAdapter);
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });

    }
}