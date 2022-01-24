package com.apps.onlinegroceriesworld.utils;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

/*
This class is used to search functionality while user is typing.
This is used in all magic search functions , account functionality, etc
 */
public class GenericTextWatcher implements TextWatcher {



    /*
        This interface which provides that the user typing is done and ready to search.
        You must implement this interface where or where you want to use.
         */
    public interface GenericTextWatcherInterface {
        interface CallBack {
            void onRun();
        }
        interface CallBackReturn {
            void onRun(String s);
        }
    }

    private GenericTextWatcherInterface.CallBack mCallback;
    private GenericTextWatcherInterface.CallBackReturn mCallbackReturn;
    private int postDelayed = Constent.postDelayed;

    public GenericTextWatcher(GenericTextWatcherInterface.CallBack mCallback) {
        this.mCallback = mCallback;
    }

    public GenericTextWatcher(GenericTextWatcherInterface.CallBackReturn mCallbackReturn, int postDelayed) {
        this.mCallbackReturn = mCallbackReturn;
        this.postDelayed = postDelayed;
    }

    Handler handler = new Handler();
    Runnable run_search = new Runnable() {
        @Override
        public void run() {
            mCallback.onRun();
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        handler.removeCallbacks(run_search);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(postDelayed==0){
            mCallbackReturn.onRun(s.toString());
        }else{
            handler.postDelayed(run_search, postDelayed);
        }
    }
}
