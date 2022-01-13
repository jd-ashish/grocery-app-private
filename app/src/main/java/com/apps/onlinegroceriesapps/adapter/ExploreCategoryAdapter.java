package com.apps.onlinegroceriesapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.onlinegroceriesapps.activity.ProductListActivity;
import com.apps.onlinegroceriesapps.databinding.ExploreCardLayout1Binding;
import com.apps.onlinegroceriesapps.models.Category;
import com.apps.onlinegroceriesapps.utils.Constent;
import com.bumptech.glide.Glide;

import java.util.List;

public class ExploreCategoryAdapter extends RecyclerView.Adapter<ExploreCategoryAdapter.ViewHolder> {

    Context context;
    List<Category> data;
    public ExploreCategoryAdapter(Context context, List<Category> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExploreCardLayout1Binding binding = ExploreCardLayout1Binding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ExploreCategoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon_or_image;
        ConstraintLayout cardView;
        public ViewHolder(@NonNull ExploreCardLayout1Binding itemView) {
            super(itemView.getRoot());
            name = itemView.productTitle;
            icon_or_image = itemView.imageView6;
            cardView = itemView.cardView;
        }

        public void bind(Category category) {
            name.setText(category.getName());
            Glide.with(context).load(category.getIcon()).into(icon_or_image);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ProductListActivity.class).putExtra("form","ExploreCategory")
                    .putExtra(Constent.CATEGORY_ID,category.getId())
                    .putExtra(Constent.CATEGORY_NAME,category.getName()));
                }
            });
        }
    }
}
