package com.apps.onlinegroceriesapps.api.network.response;

import com.apps.onlinegroceriesapps.models.ProductDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetialsResponse {
    @SerializedName("data")
    @Expose
    private List<ProductDetails> data = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<ProductDetails> getData() {
        return data;
    }

    public void setData(List<ProductDetails> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
