package com.apps.onlinegroceriesapps.models;

public class CommonGlobalMessageModel {
    String message , otp;
    boolean error;

    public CommonGlobalMessageModel(String message, String otp, boolean error) {
        this.message = message;
        this.otp = otp;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
