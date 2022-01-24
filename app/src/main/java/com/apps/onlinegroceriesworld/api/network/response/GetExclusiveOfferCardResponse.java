package com.apps.onlinegroceriesworld.api.network.response;

import com.apps.onlinegroceriesworld.models.ExclusiveOfferCard;

import java.util.List;

public class GetExclusiveOfferCardResponse {
    private List<ExclusiveOfferCard> data;
    private boolean error;
    private int status;

    public List<ExclusiveOfferCard> getData() {
        return data;
    }

    public void setData(List<ExclusiveOfferCard> data) {
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
