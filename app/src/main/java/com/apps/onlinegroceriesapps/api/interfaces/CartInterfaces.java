package com.apps.onlinegroceriesapps.api.interfaces;

import com.apps.onlinegroceriesapps.models.Carts;
import com.apps.onlinegroceriesapps.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesapps.models.Products;

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
