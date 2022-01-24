package com.apps.onlinegroceriesworld.api.network.response;

import com.apps.onlinegroceriesworld.models.Meta;
import com.apps.onlinegroceriesworld.models.OrderHistory;
import com.apps.onlinegroceriesworld.models.ProductLinks;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderHistoryResponse implements Serializable {
    @SerializedName("data")
    @Expose private List<OrderHistory> data = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("status")
    @Expose
    private Integer status;

    private ProductLinks links;

    private Meta meta;

    public ProductLinks getLinks() {
        return links;
    }

    public void setLinks(ProductLinks links) {
        this.links = links;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<OrderHistory> getData() {
        return data;
    }

    public void setData(List<OrderHistory> data) {
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
