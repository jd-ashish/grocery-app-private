package com.apps.onlinegroceriesworld.utils.BottomSheet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.onlinegroceriesworld.R;
import com.apps.onlinegroceriesworld.adapter.PaymentSelectAdapter;
import com.apps.onlinegroceriesworld.databinding.SelectPaymentOptionsBinding;
import com.apps.onlinegroceriesworld.models.PaymentModel;
import com.apps.onlinegroceriesworld.utils.Constent;
import com.apps.onlinegroceriesworld.utils.LoadingSpinner;
import com.apps.onlinegroceriesworld.utils.UserPrefs;
import com.apps.onlinegroceriesworld.utils.UserResponseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class SelectPaymentOptions {
    Context context;
    PaymentModel paymentModelOption;
    SelectPaymentOptionsBinding binding;
    LoadingSpinner loadingSpinner;
    UserResponseHelper helper;
    SelectPaymentOptionsInterfaces selectPaymentOptionsInterfaces;
    UserPrefs userPrefs;
    public interface SelectPaymentOptionsInterfaces{
        void CallBack(PaymentModel paymentModelOption);
    }
    public SelectPaymentOptions(Context context, LoadingSpinner loadingSpinner, UserResponseHelper helper, SelectPaymentOptionsInterfaces selectPaymentOptionsInterfaces, UserPrefs userPrefs) {
        this.context = context;
        this.loadingSpinner = loadingSpinner;
        this.helper = helper;
        this.selectPaymentOptionsInterfaces = selectPaymentOptionsInterfaces;
        this.userPrefs = userPrefs;
    }

    public void Open() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        binding = SelectPaymentOptionsBinding.inflate(LayoutInflater.from(context));
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setContentView(binding.getRoot());
        setPaymentOptions();
        binding.gotoCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paymentModelOption!=null){
                    selectPaymentOptionsInterfaces.CallBack(paymentModelOption);
                    bottomSheetDialog.hide();
                }else{
                    helper.showError("Please select payment options");
                }
            }
        });
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.show();
    }

    private void setPaymentOptions() {
        List<PaymentModel> paymentModels = new ArrayList<>();

        if(userPrefs.getAppSettingsPreferenceObjectJson().getData().get(0).getCashfree().equals("1")){
            paymentModels.add(new PaymentModel(R.drawable.cardpayment, Constent.CARD, "Pay with Card"));
        }

//        paymentModels.add(new PaymentModel(R.drawable.cardpayment, "card", "Checkout with Card"));
        if(userPrefs.getAppSettingsPreferenceObjectJson().getData().get(0).getRazorpay().equals("1")){
            paymentModels.add(new PaymentModel(R.drawable.rzp_img, Constent.RAZORPAY, "Card or wallet online"));
        }

        if(userPrefs.getAppSettingsPreferenceObjectJson().getData().get(0).getCod().equals("1")){
            paymentModels.add(new PaymentModel(R.drawable.cod, "cod", "Cash on Delivery"));
        }


//        paymentModels.add(new PaymentModel(R.drawable.wallet_select, "wallet", "Wallet Balance"));
//        paymentModels.add(new PaymentModel(R.drawable.upi, "UPI", "UPI fast and secure"));
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.paymentSelectList.setLayoutManager(linearLayoutManager);
        PaymentSelectAdapter adapter = new PaymentSelectAdapter(context, paymentModels, new PaymentSelectAdapter.PaymentSelectListener() {
            @Override
            public void CallBack(PaymentModel paymentModel) {
                paymentModelOption = paymentModel;
            }
        });
        binding.paymentSelectList.setAdapter(adapter);
    }
}
