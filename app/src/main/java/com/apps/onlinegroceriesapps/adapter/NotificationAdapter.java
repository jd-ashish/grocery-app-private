package com.apps.onlinegroceriesapps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.databinding.NotificationLayoutBinding;
import com.apps.onlinegroceriesapps.models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    List<Notification> data;
    NotificationAdaptorCallBack notificationAdaptorCallBack;

    public interface NotificationAdaptorCallBack{
        void CallBack(Notification id);
    }
    public NotificationAdapter(Context context, List<Notification> data , NotificationAdaptorCallBack notificationAdaptorCallBack) {
        this.context = context;
        this.data = data;
        this.notificationAdaptorCallBack = notificationAdaptorCallBack;
    }




    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationLayoutBinding binding = NotificationLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new NotificationAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,desc;
        ImageView icon_or_image;
        ConstraintLayout cardView;
        public ViewHolder(@NonNull NotificationLayoutBinding itemView) {
            super(itemView.getRoot());
            title = itemView.textView4;
            desc = itemView.desc;
//            icon_or_image = itemView.imageView6;
            cardView = itemView.card;
        }

        public void bind(Notification notification) {
            title.setText(notification.getTitle());
            desc.setText(notification.getDescription());
            if(notification.getViewed()==1){
                cardView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardView.setBackgroundColor(Color.WHITE);
                    notificationAdaptorCallBack.CallBack(notification);
                }
            });
//            Glide.with(context).load(category.getIcon()).into(icon_or_image);
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    context.startActivity(new Intent(context, ProductListActivity.class).putExtra("form","ExploreCategory")
//                            .putExtra(Constent.CATEGORY_ID,category.getId())
//                            .putExtra(Constent.CATEGORY_NAME,category.getName()));
//                }
//            });
        }
    }
}
