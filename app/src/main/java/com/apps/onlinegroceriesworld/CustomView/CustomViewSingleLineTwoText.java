package com.apps.onlinegroceriesworld.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.apps.onlinegroceriesworld.R;

public class CustomViewSingleLineTwoText extends ConstraintLayout {

    TextView text_left,see_all;
    ImageView arrow_right_down;

    public CustomViewSingleLineTwoText(@NonNull Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public CustomViewSingleLineTwoText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrs1 = context.obtainStyledAttributes(attrs, R.styleable.CustomViewSingleLineTwoText);
        inflate(context, R.layout.common_layout_two_text, this);
        text_left = findViewById(R.id.text_left);
        see_all = findViewById(R.id.see_all);
        arrow_right_down = findViewById(R.id.arrow_down);

        text_left.setText(attrs1.getText(R.styleable.CustomViewSingleLineTwoText_text_left));
        see_all.setText(attrs1.getText(R.styleable.CustomViewSingleLineTwoText_text_right));

        if(attrs1.getString(R.styleable.CustomViewSingleLineTwoText_right_icon)!=null){
            see_all.setVisibility(View.GONE);
            arrow_right_down.setVisibility(View.VISIBLE);
        }
        if(attrs1.getString(R.styleable.CustomViewSingleLineTwoText_text_color)!=null){
            see_all.setTextColor(attrs1.getColor(R.styleable.CustomViewSingleLineTwoText_text_color,getResources().getColor(R.color.black)));
            text_left.setTextColor(attrs1.getColor(R.styleable.CustomViewSingleLineTwoText_text_color,getResources().getColor(R.color.black)));
        }
        if(attrs1.getString(R.styleable.CustomViewSingleLineTwoText_text_left_size)!=null){
//            see_all.setTextColor(attrs1.getColor(R.styleable.CustomViewSingleLineTwoText_text_color,getResources().getColor(R.color.black)));
            text_left.setTextSize(attrs1.getFloat(R.styleable.CustomViewSingleLineTwoText_text_left_size,24));
        }
    }
    public void setIconRotate(float rotate){
        arrow_right_down.setRotation(rotate);
    }
}
