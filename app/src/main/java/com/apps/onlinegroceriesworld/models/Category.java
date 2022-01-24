package com.apps.onlinegroceriesworld.models;

public class Category {
    String id;
    String name;
    String icon;
    Links links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public static class Links{
        String products;

        public String getProducts() {
            return products;
        }

        public void setProducts(String products) {
            this.products = products;
        }
    }
}
