package com.apps.onlinegroceriesapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.onlinegroceriesapps.databinding.AddressListLayoutBinding;
import com.apps.onlinegroceriesapps.models.UserAddressList;
import com.apps.onlinegroceriesapps.models.UserModel;
import com.apps.onlinegroceriesapps.utils.UserPrefs;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {
    Context context;
    List<UserAddressList> data;
    static UserResponseHelper helper;
    static AddressListInterfaces addressListInterfaces;
    static int row_index = -1;
    UserPrefs userPrefs;
    UserModel userModel ;
    public interface AddressListInterfaces{
        void getAddressList(UserAddressList data);
    }
    public AddressListAdapter(Context context, List<UserAddressList> data, UserResponseHelper helper,AddressListInterfaces addressListInterfaces) {
        this.context = context;
        this.data = data;
        AddressListAdapter.helper = helper;
        AddressListAdapter.addressListInterfaces = addressListInterfaces;
        this.userPrefs = new UserPrefs(context);
        this.userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
    }

    @NonNull
    @Override
    public AddressListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddressListLayoutBinding binding = AddressListLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressListAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position),position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView address_name , full_address;
        ConstraintLayout addressCard;
        ImageView check;
        public ViewHolder(@NonNull AddressListLayoutBinding itemView) {
            super(itemView.getRoot());

            address_name = itemView.addressName;
            full_address = itemView.fullAddress;
            addressCard = itemView.addressCard;
            check = itemView.checkbox;
        }

        public void bind(UserAddressList data, int position) {
            address_name.setText(data.getKnownName());
            full_address.setText(data.getAddress());

            if(row_index == position){
                check.setVisibility(View.VISIBLE);
            }else{
                check.setVisibility(View.GONE);
            }

            if(userPrefs.getDefaultAddress()!=null){
                if(userPrefs.getDefaultAddress().getId()==data.getId()){
                    check.setVisibility(View.VISIBLE);
                }
            }

            addressCard.setOnClickListener(v -> {
                if(userPrefs.getDefaultAddress()!=null){
                    if(userPrefs.getDefaultAddress().getId()==data.getId()){
                        check.setVisibility(View.VISIBLE);
                    }
                }
                row_index = position;
                notifyDataSetChanged();
                helper.showConfirm("Are you sure select this address ?");
                helper.getCancel().setOnClickListener(v1 -> helper.cancelConfirm());
                helper.getConfirmOk().setOnClickListener(v12 -> {
                    addressListInterfaces.getAddressList(data);
                    helper.cancelConfirm();
                });
                helper.getCancel().setOnClickListener(v1 -> {
                    if(row_index == position){
                        check.setVisibility(View.GONE);
                    }
                    helper.cancelConfirm();
                });
            });
        }
    }
}
