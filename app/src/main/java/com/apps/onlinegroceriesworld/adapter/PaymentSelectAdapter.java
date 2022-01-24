package com.apps.onlinegroceriesworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.apps.onlinegroceriesworld.R;
import com.apps.onlinegroceriesworld.models.PaymentModel;

import java.util.List;

public class PaymentSelectAdapter extends RecyclerView.Adapter<PaymentSelectAdapter.ViewHolder> {

    private Context context;
    private List<PaymentModel> paymentModels;
    private LayoutInflater mInflater;
    private PaymentSelectListener paymentSelectListener;
    public static interface PaymentSelectListener{
        void CallBack(PaymentModel paymentModel);
    }
    private int row_index = -1;
    double walletBalance;
    // data is passed into the constructor
    public PaymentSelectAdapter(Context context, List<PaymentModel> paymentModels, PaymentSelectListener paymentSelectListener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.paymentModels = paymentModels;
        this.paymentSelectListener = paymentSelectListener;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.payment_select_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(paymentModels.get(position), position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return paymentModels.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView payment_icon , checkbox;
        TextView payment_text;
//        TextView amount_type;
        ConstraintLayout payment_layout;

        ViewHolder(View itemView) {
            super(itemView);
            payment_icon = itemView.findViewById(R.id.payment_icon);
            checkbox = itemView.findViewById(R.id.checkbox);
            payment_text = itemView.findViewById(R.id.payment_text);
            payment_layout = itemView.findViewById(R.id.payment_layout);
//            amount_type = itemView.findViewById(R.id.amount_type);
        }

        public void bind(final PaymentModel paymentModel, int position) {
//            amount_type.setText(AppConfig.convertPrice(context,walletBalance));
            payment_icon.setImageResource(paymentModel.getDrawable());
            payment_text.setText(paymentModel.getPayment_text());

            if(row_index == position){
                checkbox.setVisibility(View.VISIBLE);
            }
            else
            {
                checkbox.setVisibility(View.GONE);
            }
//            if(paymentModel.getPayment_method().equals("wallet")){
//                amount_type.setVisibility(View.VISIBLE);
//            }else{
//                amount_type.setVisibility(View.GONE);
//            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    row_index = position;
                    notifyDataSetChanged();
                    if (paymentSelectListener != null){
                        paymentSelectListener.CallBack(paymentModel);
                    }
                }
            });
        }
    }
}

