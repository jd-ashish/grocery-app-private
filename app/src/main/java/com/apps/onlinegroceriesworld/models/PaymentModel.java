package com.apps.onlinegroceriesworld.models;

public class PaymentModel {
    int drawable;
    String payment_method, payment_text;

    public PaymentModel(int drawable, String payment_method, String payment_text) {
        this.drawable = drawable;
        this.payment_method = payment_method;
        this.payment_text = payment_text;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_text() {
        return payment_text;
    }

    public void setPayment_text(String payment_text) {
        this.payment_text = payment_text;
    }
}
