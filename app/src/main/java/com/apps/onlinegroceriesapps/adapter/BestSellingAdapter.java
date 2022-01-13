package com.apps.onlinegroceriesapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.onlinegroceriesapps.activity.ProductDetailsActivity;
import com.apps.onlinegroceriesapps.api.interfaces.CartInterfaces;
import com.apps.onlinegroceriesapps.databinding.ProductCardLayout1Binding;
import com.apps.onlinegroceriesapps.models.Products;
import com.apps.onlinegroceriesapps.utils.Util;
import com.bumptech.glide.Glide;

import java.util.List;

public class BestSellingAdapter extends RecyclerView.Adapter<BestSellingAdapter.ViewHolder> {
    Context context;
    List<Products> data;
    CartInterfaces.BestSelling cartInterfaces;

    public BestSellingAdapter(Context context, List<Products> data, CartInterfaces.BestSelling cartInterfaces) {
        this.context = context;
        this.data = data;
        this.cartInterfaces = cartInterfaces;
    }

    @NonNull
    @Override
    public BestSellingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductCardLayout1Binding binding = ProductCardLayout1Binding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new BestSellingAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellingAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage , addToCart;
        TextView productName ,per_unit,price;
        public ViewHolder(@NonNull ProductCardLayout1Binding itemView) {
            super(itemView.getRoot());

            productImage = itemView.imageView6;
            productName = itemView.productTitle;
            per_unit = itemView.h2;
            price = itemView.price;
            addToCart = itemView.addToCart;
        }

        public void bind(Products products) {
            productName.setText(products.getName());
            if(products.getPerUnit()!=null){
                per_unit.setText(products.getPerUnit().getVariant() +" "+products.getUnit());
                price.setText(Util.convertPrice(context,Double.valueOf(products.getPerUnit().getPrice())));
            }else{
                per_unit.setText(products.getBase_price() +" / Qty");
                price.setText(Util.convertPrice(context,Double.valueOf(products.getBase_price())));
            }

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartInterfaces.SetCartQty(products);
                }
            });
            Glide.with(context).load(products.getThumbnail_image()).into(productImage);
            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ProductDetailsActivity.class)
                    .putExtra("id",products.getId()));
                }
            });
        }
    }
}
