package com.apps.onlinegroceriesapps.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.apps.onlinegroceriesapps.R;

public class CustomViewCheckOutList extends ConstraintLayout {
    TextView title , right_text_icon;
    public CustomViewCheckOutList(@NonNull Context context) {
        super(context);
    }

    public CustomViewCheckOutList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrs1 = context.obtainStyledAttributes(attrs, R.styleable.CustomViewCheckOutList);
        inflate(context, R.layout.custom_view_checkout_list, this);
        title = findViewById(R.id.textView6);
        right_text_icon = findViewById(R.id.right_text_icon);

        title.setText(attrs1.getText(R.styleable.CustomViewCheckOutList_right_text_col));
        right_text_icon.setText(attrs1.getText(R.styleable.CustomViewCheckOutList_left_text_icon_col));
    }

    public void setRight_text_icon(String right_text_icon) {
        this.right_text_icon.setText(right_text_icon);
    }
}
