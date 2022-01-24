package com.apps.onlinegroceriesworld.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.onlinegroceriesworld.api.network.response.ProductResponse;
import com.apps.onlinegroceriesworld.databinding.CommonOrderListBinding;
import com.apps.onlinegroceriesworld.models.OrderDetail;
import com.apps.onlinegroceriesworld.utils.Util;
import com.bumptech.glide.Glide;

import java.util.List;

public class CommonOrderListAdapter extends RecyclerView.Adapter<CommonOrderListAdapter.ViewHolder> {
    Context context;
    List<OrderDetail> orderDetails;
    List<ProductResponse> products;
    Double grandTotal;


    public CommonOrderListAdapter(Context context, List<OrderDetail> orderDetails, List<ProductResponse> products, Double grandTotal) {
        this.context = context;
        this.orderDetails = orderDetails;
        this.products = products;
        this.grandTotal = grandTotal;
    }

    @NonNull
    @Override
    public CommonOrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommonOrderListBinding binding = CommonOrderListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CommonOrderListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonOrderListAdapter.ViewHolder holder, int position) {
        holder.bind(products.get(position),orderDetails.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView product_name , amount , status;
        public ViewHolder(@NonNull CommonOrderListBinding itemView) {
            super(itemView.getRoot());

            productImage = itemView.imageView11;
            product_name = itemView.productName;
            amount = itemView.amount;
            status = itemView.status;
        }

        public void bind(ProductResponse data, OrderDetail orderDetail) {

            if(data.getData()!=null){
                System.out.println("productsproductsproductsproducts "+data.getData().size());
            }
            product_name.setText(data.getData().get(0).getName());
            amount.setText(Util.convertPrice(context,grandTotal));
            status.setText(Util.StringModify(orderDetail.getDeliveryStatus()));
            Glide.with(context).load(data.getData().get(0).getThumbnail_image()).into(productImage);
        }
    }
}
