package com.apps.onlinegroceriesapps.models;

public class Carts {
    String id;
    Products product;
    ProductsStock productStock;
    double price;
    double qty;
    String date;

    public double getPrice() {
        return price;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public ProductsStock getProductStock() {
        return productStock;
    }

    public void setProductStock(ProductsStock productStock) {
        this.productStock = productStock;
    }


    public void setPrice(double price) {
        this.price = price;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static class ProductsStock{
        String id;
        String variation;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVariation() {
            return variation;
        }

        public void setVariation(String variation) {
            this.variation = variation;
        }
    }
}
