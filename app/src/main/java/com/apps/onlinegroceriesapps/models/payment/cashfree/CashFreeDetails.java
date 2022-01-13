package com.apps.onlinegroceriesapps.models.payment.cashfree;

public class CashFreeDetails {
    String APP_ID;
    String SecretKey;

    public String getAPP_ID() {
        return APP_ID;
    }

    public void setAPP_ID(String APP_ID) {
        this.APP_ID = APP_ID;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public void setSecretKey(String secretKey) {
        SecretKey = secretKey;
    }
}
