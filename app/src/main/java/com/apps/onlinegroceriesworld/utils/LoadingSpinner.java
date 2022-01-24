package com.apps.onlinegroceriesworld.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.apps.onlinegroceriesworld.R;


public class LoadingSpinner {
    Dialog progress;
    Context context;

    public LoadingSpinner(Context context) {
        this.context=context;
    }

    public void showLoading(){
        progress=new Dialog(context);
        progress.setContentView(R.layout.common_loading_spinner );
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(!progress.isShowing()){
            progress.show();
        }

    }
    public void showLoading(String titleData){
        TextView title = progress.findViewById(R.id.title);
        title.setText(titleData);
        progress.show();
    }

    public void cancelLoading(){
        if(progress!=null)
        progress.cancel();
    }
}
