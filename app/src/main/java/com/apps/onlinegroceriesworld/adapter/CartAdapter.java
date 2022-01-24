package com.apps.onlinegroceriesworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.onlinegroceriesworld.api.interfaces.CartInterfaces;
import com.apps.onlinegroceriesworld.databinding.CartCardLayout1Binding;
import com.apps.onlinegroceriesworld.models.Carts;
import com.apps.onlinegroceriesworld.utils.Util;
import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    CartInterfaces cartInterfaces;

    public Context getContext(){
        return this.context;
    }
    List<Carts> data;

    public CartAdapter(Context context, List<Carts> data,CartInterfaces cartInterfaces) {
        this.context = context;
        this.cartInterfaces = cartInterfaces;
        this.data = data;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartCardLayout1Binding binding = CartCardLayout1Binding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CartAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        if(position==(data.size()-1)){
            holder.hr_line.setVisibility(View.GONE);
        }
        holder.bind(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void deleteItem(int position) {
        cartInterfaces.onCartRemove(data.get(position));
//        cartItemListener.onCartRemove(cartItems.get(position));
//        cartItems.remove(position);
        data.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View hr_line;
        ImageView productImage , increases_cart, desc_cart , removeCartItem;
        TextView productTitle , productVeriant , qty , price;
        public ViewHolder(@NonNull CartCardLayout1Binding itemView) {
            super(itemView.getRoot());

            hr_line = itemView.hrLine;
            productTitle = itemView.productTitle;
            productImage = itemView.productImage;
            increases_cart = itemView.incCart;
            price = itemView.price;
            desc_cart = itemView.descCart;
            qty = itemView.qty;
            productVeriant = itemView.h2;
            removeCartItem = itemView.removeCart;
        }

        public void bind(Carts carts) {
            Glide.with(context).load(carts.getProduct().getThumbnail_image()).into(productImage);
            productTitle.setText(Util.nullString(carts.getProduct().getName()));
            productVeriant.setText(Util.nullString(carts.getProductStock().getVariation()));
            qty.setText(Util.nullString(String.valueOf(Integer.valueOf((int) carts.getQty()))));
            price.setText(Util.convertPrice(context,carts.getPrice()));
            increases_cart.setOnClickListener(v -> cartInterfaces.onSetUpdateCartQty(carts));
            desc_cart.setOnClickListener(v -> cartInterfaces.onSetNegativeUpdateCartQty(carts));
            removeCartItem.setOnClickListener(v -> cartInterfaces.onCartRemove(carts));
        }
    }
}
