package com.apps.onlinegroceriesapps.api.network.response;

import com.apps.onlinegroceriesapps.models.Carts;
import com.apps.onlinegroceriesapps.models.Notification;

import java.util.List;

public class NotificationResponse {
    private List<Notification> data;
    private boolean error;
    private int status;

    public List<Notification> getData() {
        return data;
    }

    public void setData(List<Notification> data) {
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
