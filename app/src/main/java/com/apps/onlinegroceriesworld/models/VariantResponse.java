package com.apps.onlinegroceriesworld.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VariantResponse {

    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("variant_id")
    @Expose
    private String variant_id;
    @SerializedName("variant")
    @Expose
    private String variant;
    @SerializedName("price")
    @Expose
    private Double price;

    public String getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(String variant_id) {
        this.variant_id = variant_id;
    }

    @SerializedName("in_stock")
    @Expose


    private Boolean inStock;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

}