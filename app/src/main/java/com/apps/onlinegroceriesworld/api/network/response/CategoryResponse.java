package com.apps.onlinegroceriesworld.api.network.response;

import com.apps.onlinegroceriesworld.models.Category;

import java.util.List;

public class CategoryResponse {
    private List<Category> data;
    private boolean error;
    private int status;

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
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
