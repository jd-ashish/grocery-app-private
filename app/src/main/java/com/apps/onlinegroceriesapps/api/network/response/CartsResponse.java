package com.apps.onlinegroceriesapps.api.network.response;

import com.apps.onlinegroceriesapps.models.Carts;
import com.apps.onlinegroceriesapps.models.Category;

import java.util.List;

public class CartsResponse {
    private List<Carts> data;
    private boolean error;
    private int status;

    public List<Carts> getData() {
        return data;
    }

    public void setData(List<Carts> data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
