package com.apps.onlinegroceriesapps.api.network.response;
import com.apps.onlinegroceriesapps.models.Meta;
import com.apps.onlinegroceriesapps.models.ProductLinks;
import com.apps.onlinegroceriesapps.models.Products;

import java.io.Serializable;
import java.util.List;

public class ProductResponse implements Serializable {
    private List<Products> data;
    private boolean error;
    private int status;

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

    public List<Products> getData() {
        return data;
    }

    public void setData(List<Products> data) {
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
