package com.apps.onlinegroceriesworld.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.onlinegroceriesworld.activity.ProductListActivity;
import com.apps.onlinegroceriesworld.databinding.ExclusiveOfferCardBinding;
import com.apps.onlinegroceriesworld.models.ExclusiveOfferCard;
import com.apps.onlinegroceriesworld.utils.Constent;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

public class ExclusiveOfferAdapter extends RecyclerView.Adapter<ExclusiveOfferAdapter.ViewHolder> {
    Context context;
    List<ExclusiveOfferCard> data;
    public ExclusiveOfferAdapter(Context context, List<ExclusiveOfferCard> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ExclusiveOfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExclusiveOfferCardBinding binding = ExclusiveOfferCardBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ExclusiveOfferAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExclusiveOfferAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(data.get(position).getImage()).into(holder.productImage);


        Gson gson = new Gson();
        Object src;
        String g_data = gson.toJson(data.get(position).getProducts());
        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ProductListActivity.class)
                        .putExtra("form","offerCard")
                        .putExtra(Constent.TITLE,data.get(position).getTitle())
                        .putExtra(Constent.PRODUCT_ID, data.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        public ViewHolder(@NonNull ExclusiveOfferCardBinding itemView) {
            super(itemView.getRoot());

            productImage = itemView.offerId;
        }
    }
}
