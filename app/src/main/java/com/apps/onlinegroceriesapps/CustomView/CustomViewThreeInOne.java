package com.apps.onlinegroceriesapps.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.apps.onlinegroceriesapps.R;

public class CustomViewThreeInOne extends ConstraintLayout {
    ImageView left_icon, arrow_down;
    TextView title;
    public CustomViewThreeInOne(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("ResourceType")
    public CustomViewThreeInOne(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrs1 = context.obtainStyledAttributes(attrs, R.styleable.CustomViewThreeInOne);
        inflate(context, R.layout.three_in_one_layout, this);
        left_icon = findViewById(R.id.icon_left);
        arrow_down = findViewById(R.id.arrow_down);
        title = findViewById(R.id.title);

        title.setText(attrs1.getText(R.styleable.CustomViewThreeInOne_title_tio));
        left_icon.setImageResource(attrs1.getResourceId(R.styleable.CustomViewThreeInOne_left_icon_tio,R.drawable.ic_orders_icon));
        arrow_down.setImageResource(attrs1.getResourceId(R.styleable.CustomViewThreeInOne_right_icon_tio,R.drawable.ic_back_arrow));

    }
}
