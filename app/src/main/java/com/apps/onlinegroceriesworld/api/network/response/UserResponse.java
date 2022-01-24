package com.apps.onlinegroceriesworld.api.network.response;

public class UserResponse {
    String id , type , name , email , avatar , avatar_original , phone , plateform, provider_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_original() {
        return avatar_original;
    }

    public void setAvatar_original(String avatar_original) {
        this.avatar_original = avatar_original;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlateform() {
        return plateform;
    }

    public void setPlateform(String plateform) {
        this.plateform = plateform;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }
}
