package com.apps.onlinegroceriesapps.utils;

import android.os.Handler;
import android.os.Looper;

public class GenericDelay {
    GenericDelayed genericDelayed;
    final Handler handler = new Handler(Looper.getMainLooper());
    public GenericDelay(GenericDelayed genericDelayed) {
        this.genericDelayed = genericDelayed;
    }

    public interface GenericDelayed{
        void run();
    }

    public GenericDelay Use(){
        handler.postDelayed(() -> genericDelayed.run(), 1000);
        return null;
    }
    public GenericDelay CustomDelay(int time_in_milliseconds){
        handler.postDelayed(() -> genericDelayed.run(), time_in_milliseconds);
        return null;
    }

}
