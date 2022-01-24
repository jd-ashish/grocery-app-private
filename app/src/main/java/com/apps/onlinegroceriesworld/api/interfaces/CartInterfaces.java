package com.apps.onlinegroceriesworld.api.interfaces;

import com.apps.onlinegroceriesworld.models.Carts;
import com.apps.onlinegroceriesworld.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesworld.models.Products;

public interface CartInterfaces {
    interface CallBack{
        void onUpdateCartResult(CommonGlobalMessageModel body);
    }
    interface BestSelling{
        void SetCartQty(Products products);
    }
    void onSetUpdateCartQty(Carts carts);
    void onSetNegativeUpdateCartQty(Carts carts);
    void onCartRemove(Carts carts);
}
