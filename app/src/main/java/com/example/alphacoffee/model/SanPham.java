package com.example.alphacoffee.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private String productId;
    private String name;
    private String priceL;
    private String priceM;
    private String priceS;
    private String note;
    private String topping;
    private String priceTopping;
    private String like;
    private String type;
    private String imageURL;

    public SanPham() {
    }

    public SanPham(String productId, String name, String priceL, String priceM, String priceS, String note, String topping, String priceTopping, String like, String type, String imageURL) {
        this.productId = productId;
        this.name = name;
        this.priceL = priceL;
        this.priceM = priceM;
        this.priceS = priceS;
        this.note = note;
        this.topping = topping;
        this.priceTopping = priceTopping;
        this.like = like;
        this.type = type;
        this.imageURL = imageURL;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceL() {
        return priceL;
    }

    public void setPriceL(String priceL) {
        this.priceL = priceL;
    }

    public String getPriceM() {
        return priceM;
    }

    public void setPriceM(String priceM) {
        this.priceM = priceM;
    }

    public String getPriceS() {
        return priceS;
    }

    public void setPriceS(String priceS) {
        this.priceS = priceS;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTopping() {
        return topping;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }

    public String getPriceTopping() {
        return priceTopping;
    }

    public void setPriceTopping(String priceTopping) {
        this.priceTopping = priceTopping;
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
