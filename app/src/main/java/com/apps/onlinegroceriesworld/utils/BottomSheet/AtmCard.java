package com.apps.onlinegroceriesworld.utils.BottomSheet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.apps.onlinegroceriesworld.databinding.AtmCardLayoutBinding;
import com.apps.onlinegroceriesworld.utils.UserResponseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AtmCard {
    Context context;
    AtmCardLayoutBinding binding;
    AtmCardInterfaces atmCardInterfaces;
    UserResponseHelper helper;
    public interface AtmCardInterfaces{
        void CallBack(String accountHolderName, String cardNumber, String month, String year, String cvv);
    }
    public AtmCard(Context context, AtmCardInterfaces atmCardInterfaces, UserResponseHelper helper) {
        this.context = context;
        this.atmCardInterfaces = atmCardInterfaces;
        this.helper = helper;
    }
    public void Open(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        binding = AtmCardLayoutBinding.inflate(LayoutInflater.from(context));
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.contunue.setOnClickListener(v -> {
            if(binding.accountHolderName.getText().length()<5){
                helper.showError("Enter valid account holder name ");
                return;
            }
            if(binding.cardNumber.getText().length()!=16){
                helper.showError("Enter valid card number ");
                return;
            }
            if(binding.month.getText().toString().length()!=2){
                helper.showError("Enter valid month");
                return;
            }
            if(binding.year.getText().toString().length()!=4){
                helper.showError("Enter valid year");
                return;
            }
            if(binding.cvv.getText().toString().length()!=3){
                helper.showError("Enter valid cvv");
                return;
            }
            atmCardInterfaces.CallBack(binding.accountHolderName.getText().toString(),
                    binding.cardNumber.getText().toString(),
                    binding.month.getText().toString(),
                    binding.year.getText().toString(),
                    binding.cvv.getText().toString());
        });

        bottomSheetDialog.show();
    }
}
