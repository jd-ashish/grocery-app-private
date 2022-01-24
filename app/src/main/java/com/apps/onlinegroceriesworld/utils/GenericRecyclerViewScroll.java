package com.apps.onlinegroceriesworld.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class GenericRecyclerViewScroll extends RecyclerView.OnScrollListener {

    int currentPage, totalPage;
    Context context;
    RecyclerView recyclerView;
    OnGenericScrollListener mCallback;
    LinearLayoutManager layoutManager;

    /*
    This interface works as a callback for this class and send the data to activity if need to load more data from backend
    or not
     */
    public interface OnGenericScrollListener {
        void onScrolledListener(boolean toReload);
    }

    /*
    This is the main constructor of this class where the necessary arguments are send from the activity to track the scrolling
    action of the recycler view and load the data according to the need
     */
    public GenericRecyclerViewScroll(@NotNull Context context, RecyclerView recyclerView,LinearLayoutManager manager, int currentPage, int totalPage, OnGenericScrollListener mCallback) {
        super();
        this.context = context;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.mCallback = mCallback;
        this.recyclerView = recyclerView;
        this.layoutManager=manager;
    }

    /*
    this method is from override the abstract class RecyclerView.OnScrollListener
     */
    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    /*
   this method is also from override the abstract class RecyclerView.OnScrollListener
   and here the callback is send to the requesting activity
    */
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int totalItemCount = layoutManager.getItemCount();
        int lastVisible = layoutManager.findLastVisibleItemPosition();
        boolean endHasBeenReached = lastVisible + 1 >= totalItemCount;
        if (totalItemCount > 0 && endHasBeenReached && currentPage < totalPage) {
            mCallback.onScrolledListener(true);
        } else {
            mCallback.onScrolledListener(false);
        }
    }

    /*
    After every api call this methode set the current loaded page number and control the api call
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
