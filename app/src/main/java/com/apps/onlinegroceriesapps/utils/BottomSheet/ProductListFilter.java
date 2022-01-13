package com.apps.onlinegroceriesapps.utils.BottomSheet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.apps.onlinegroceriesapps.databinding.BottomProductListFilterBinding;
import com.apps.onlinegroceriesapps.utils.GenericChip;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class ProductListFilter {
    Context context;
    ProductListFilterInterfaces productListFilterInterfaces;
    BottomSheetDialog bottomSheetDialog;

    public interface ProductListFilterInterfaces{
        void onProductFilter(List<String> dateWise, List<String> priceWise);
    }
    public ProductListFilter(Context context , ProductListFilterInterfaces productListFilterInterfaces) {
        this.context = context;
        this.productListFilterInterfaces = productListFilterInterfaces;
    }

    public void open(){
        bottomSheetDialog = new BottomSheetDialog(context);
        BottomProductListFilterBinding binding = BottomProductListFilterBinding.inflate(LayoutInflater.from(context));
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.filterProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> dateWise = new GenericChip().getSelectedChipList(binding.dateWiseGrp);
                List<String> priceWise = new GenericChip().getSelectedChipList(binding.priceWise);
                productListFilterInterfaces.onProductFilter(dateWise,priceWise);
            }
        });
        bottomSheetDialog.show();
    }
    public void hide(){
        bottomSheetDialog.hide();
    }
}
