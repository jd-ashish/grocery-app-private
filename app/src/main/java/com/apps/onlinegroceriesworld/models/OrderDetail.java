package com.apps.onlinegroceriesworld.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("variation")
    @Expose
    private String variation;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("tax")
    @Expose
    private Double tax;
    @SerializedName("shipping_cost")
    @Expose
    private Double shippingCost;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("delivery_status")
    @Expose
    private String deliveryStatus;
    @SerializedName("unitPrice")
    @Expose
    private Double unitPrice;
    @SerializedName("purchasePrice")
    @Expose
    private String purchasePrice;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("discountType")
    @Expose
    private String discountType;


    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
    public String getProductId() {
        return id;
    }

    public void setProductId(String id) {
        this.id = id;
    }
    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

}
