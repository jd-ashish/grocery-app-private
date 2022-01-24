package com.apps.onlinegroceriesworld.models;

import com.apps.onlinegroceriesworld.api.network.response.ProductResponse;
import com.apps.onlinegroceriesworld.api.network.response.UserResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderHistory implements Serializable {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("user")
    @Expose
    private UserResponse user;

    @SerializedName("shipping_address")
    @Expose
    private UserAddressList shippingAddress;
    @SerializedName("orderDetails")
    @Expose
    private List<OrderDetail> orderDetails;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("grand_total")
    @Expose
    private Double grandTotal;
    @SerializedName("shipping_cost")
    @Expose
    private Double shippingCost;
    @SerializedName("coupon_discount")
    @Expose
    private Double coupon_discount;
    @SerializedName("subtotal")
    @Expose
    private Double subtotal;
    @SerializedName("tax")
    @Expose
    private Double tax;
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("products")
    @Expose
    private List<ProductResponse> products;

    @SerializedName("delivery_status")
    @Expose
    private String delivery_status;


    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public UserAddressList getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(UserAddressList shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Double getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(Double coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

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
}
