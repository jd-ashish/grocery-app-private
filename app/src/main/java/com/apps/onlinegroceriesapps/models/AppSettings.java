package com.apps.onlinegroceriesapps.models;

import com.apps.onlinegroceriesapps.models.payment.cashfree.CashFreeDetails;
import com.apps.onlinegroceriesapps.models.payment.rzp.RzpDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppSettings implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("instagram")
    @Expose
    private String instagram;
    @SerializedName("youtube")
    @Expose
    private String youtube;
    @SerializedName("google_plus")
    @Expose
    private String googlePlus;
    @SerializedName("currency")
    @Expose
    private Currency currency;
    @SerializedName("currency_format")
    @Expose
    private String currencyFormat;

    @SerializedName("login_by_phone")
    @Expose
    private String loginByPhone;

    @SerializedName("razorpay")
    @Expose
    private String razorpay;

    @SerializedName("rzp_details")
    @Expose
    private RzpDetails rzpDetails;

    @SerializedName("cashfree")
    @Expose
    private String cashfree;

    @SerializedName("cod")
    @Expose
    private String cod;

    @SerializedName("cashfree_details")
    @Expose
    private CashFreeDetails cashfreeDetails;

    @SerializedName("exclusive_offer_type")
    @Expose
    private String exclusiveOfferType;


    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getGooglePlus() {
        return googlePlus;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getCurrencyFormat() {
        return currencyFormat;
    }

    public void setCurrencyFormat(String currencyFormat) {
        this.currencyFormat = currencyFormat;
    }

    public String getLoginByPhone() {
        return loginByPhone;
    }

    public void setLoginByPhone(String loginByPhone) {
        this.loginByPhone = loginByPhone;
    }

    public String getRazorpay() {
        return razorpay;
    }

    public void setRazorpay(String razorpay) {
        this.razorpay = razorpay;
    }

    public RzpDetails getRzpDetails() {
        return rzpDetails;
    }

    public void setRzpDetails(RzpDetails rzpDetails) {
        this.rzpDetails = rzpDetails;
    }

    public String getCashfree() {
        return cashfree;
    }

    public void setCashfree(String cashfree) {
        this.cashfree = cashfree;
    }

    public CashFreeDetails getCashfreeDetails() {
        return cashfreeDetails;
    }

    public void setCashfreeDetails(CashFreeDetails cashfreeDetails) {
        this.cashfreeDetails = cashfreeDetails;
    }

    public String getExclusiveOfferType() {
        return exclusiveOfferType;
    }

    public void setExclusiveOfferType(String exclusiveOfferType) {
        this.exclusiveOfferType = exclusiveOfferType;
    }
}
