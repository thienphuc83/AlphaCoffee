package com.example.alphacoffee.model;

import java.io.Serializable;

public class SanPhamOrder implements Serializable {
    private String id;
    private String name;
    private String price;
    private String size;
    private String topping;
    private String imageURL;
    private int soluong;

    public SanPhamOrder() {
    }

    public SanPhamOrder(String id, String name, String price, String size, String topping, String imageURL, int soluong) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.size = size;
        this.topping = topping;
        this.imageURL = imageURL;
        this.soluong = soluong;
    }

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTopping() {
        return topping;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
