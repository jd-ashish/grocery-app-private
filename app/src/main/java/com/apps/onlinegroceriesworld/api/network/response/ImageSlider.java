package com.apps.onlinegroceriesworld.api.network.response;

import java.util.List;

public class ImageSlider {
    private List<Data> data;
    private boolean error;
    private int status;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
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

    public static class Data {
        String id;
        List<String> photos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }
    }
}
