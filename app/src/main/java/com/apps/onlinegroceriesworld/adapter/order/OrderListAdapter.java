package com.apps.onlinegroceriesworld.adapter.order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.onlinegroceriesworld.activity.order.TrackOrderActivity;
import com.apps.onlinegroceriesworld.databinding.OrderListLayoutBinding;
import com.apps.onlinegroceriesworld.models.OrderHistory;
import com.apps.onlinegroceriesworld.utils.Util;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    Context context;
    List<OrderHistory> orderHistories;
    CommonOrderListAdapter commonOrderListAdapter;
    String hide_track = "none";

    public OrderListAdapter(Context context , List<OrderHistory> orderHistories) {
        this.context = context;
        this.orderHistories = orderHistories;
        this.hide_track = "none";
    }

    public OrderListAdapter(TrackOrderActivity context, List<OrderHistory> orderHistories, String hide_track) {
        this.context = context;
        this.orderHistories = orderHistories;
        this.hide_track = hide_track;
    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderListLayoutBinding binding = OrderListLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrderListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        holder.bind(orderHistories.get(position));
    }

    @Override
    public int getItemCount() {
        return orderHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId , date;
        RecyclerView recyclerView;
        Button track_order;
        public ViewHolder(@NonNull OrderListLayoutBinding itemView) {
            super(itemView.getRoot());

            orderId = itemView.orderId;
            date = itemView.orderDate;
            recyclerView = itemView.orderList;
            track_order = itemView.trackOrder;
        }

        public void bind(OrderHistory data) {

            if(data.getProducts()!=null){
                System.out.println("productsproductsproductsproducts "+data.getProducts().size());
            }
            commonOrderListAdapter = new CommonOrderListAdapter(context, data.getOrderDetails(),data.getProducts(),data.getGrandTotal());
            setupRecyclerView();
            orderId.setText("Order Id: "+data.getCode());
            date.setText("Date: "+data.getDate());
            if(hide_track.equals("hide")){
                track_order.setVisibility(View.GONE);
            }
            track_order.setOnClickListener(v -> {
                context.startActivity(new Intent(context, TrackOrderActivity.class)
                .putExtra("code", data.getCode()));
            });
        }
        private void setupRecyclerView(){


            GridLayoutManager horizontalLayoutManager
                    = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            //adapter.setClickListener(this);
            recyclerView.addItemDecoration( new LayoutMarginDecoration( 1,  Util.convertDpToPx(context, 10)) );

            recyclerView.setAdapter(commonOrderListAdapter);

        }
    }
}
