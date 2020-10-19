package com.example.alphacoffee.model;

public class User {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String type;
    private String birthday;
    private String point;
    private String note;
    private String imageURL;

    public User() {
    }

    public User(String userId, String name, String email, String phone, String gender, String type, String birthday, String point, String note, String imageURL) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.type = type;
        this.birthday = birthday;
        this.point = point;
        this.note = note;
        this.imageURL = imageURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
