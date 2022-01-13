package com.apps.onlinegroceriesapps.utils;

import android.content.Context;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import com.apps.onlinegroceriesapps.R;

import org.jetbrains.annotations.NotNull;


public class GenericSwipeRefresh extends SwipeRefreshLayout implements OnRefreshListener {
    SwipeRefreshLayout swipeRefresh;
    OnRefreshListener mListener;
    onRefreshCallBack mCallback;

    public GenericSwipeRefresh(@NonNull @NotNull Context context) {
        super(context, null);
    }
/*
* overloads means constructors if 0 then constructors 1
* if 1 then constructors 2
* */
    public interface onRefreshCallBack{
        void onRun();
    }
    public GenericSwipeRefresh(onRefreshCallBack mCallback,@NonNull @NotNull Context context, SwipeRefreshLayout swipeRefreshes) {
        super(context, null);
        this.mCallback = mCallback;
        this.swipeRefresh = swipeRefreshes;
    }

    @Override
    public void onRefresh() {
        mCallback.onRun();
        swipeRefresh.setRefreshing(false);
    }

    public interface OnRefreshListener {
        /**
         * Called when a swipe gesture triggers a refresh.
         */
        void onRefresh();
    }
    public void setOnRefreshListener(@Nullable OnRefreshListener listener) {
        mListener = listener;
    }

    public void setColorScheme(){
        swipeRefresh.setColorSchemeResources(R.color.ndark_red_main, R.color.nsuccess, R.color.light_blue_900);
    }
}
