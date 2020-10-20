package com.example.alphacoffee.model;

import java.io.Serializable;

public class SanPham implements Serializable {

    private String name;
    private String price;
    private String note;
    private String size;
    private String topping;
    private String like;
    private String type;
    private String imageURL;

    public SanPham() {
    }

    public SanPham(String name, String price, String note, String size, String topping, String like, String type, String imageURL) {
        this.name = name;
        this.price = price;
        this.note = note;
        this.size = size;
        this.topping = topping;
        this.like = like;
        this.type = type;
        this.imageURL = imageURL;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
