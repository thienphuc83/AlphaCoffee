package com.example.alphacoffee.model;

import java.io.Serializable;

public class TinTuc implements Serializable {

    private String tinTucId;
    private String tenTinTuc;
    private String noiDung;
    private String hinhAnh;

    public TinTuc() {
    }

    public TinTuc(String tinTucId, String tenTinTuc, String noiDung, String hinhAnh) {
        this.tinTucId = tinTucId;
        this.tenTinTuc = tenTinTuc;
        this.noiDung = noiDung;
        this.hinhAnh = hinhAnh;
    }

    public String getTinTucId() {
        return tinTucId;
    }

    public void setTinTucId(String tinTucId) {
        this.tinTucId = tinTucId;
    }

    public String getTenTinTuc() {
        return tenTinTuc;
    }

    public void setTenTinTuc(String tenTinTuc) {
        this.tenTinTuc = tenTinTuc;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
