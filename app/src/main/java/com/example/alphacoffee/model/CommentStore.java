package com.example.alphacoffee.model;

import java.io.Serializable;

public class CommentStore implements Serializable {
    private String idCmt;
    private String noiDung;
    private String ngayTao;
    private String tenKH;
    private String idKH;
    private String hinhKH;
    private String idCH;

    public CommentStore() {
    }

    public CommentStore(String idCmt, String noiDung, String ngayTao, String tenKH, String idKH, String hinhKH, String idCH) {
        this.idCmt = idCmt;
        this.noiDung = noiDung;
        this.ngayTao = ngayTao;
        this.tenKH = tenKH;
        this.idKH = idKH;
        this.hinhKH = hinhKH;
        this.idCH = idCH;
    }

    public String getIdCmt() {
        return idCmt;
    }

    public void setIdCmt(String idCmt) {
        this.idCmt = idCmt;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getIdKH() {
        return idKH;
    }

    public void setIdKH(String idKH) {
        this.idKH = idKH;
    }

    public String getHinhKH() {
        return hinhKH;
    }

    public void setHinhKH(String hinhKH) {
        this.hinhKH = hinhKH;
    }

    public String getIdCH() {
        return idCH;
    }

    public void setIdCH(String idCH) {
        this.idCH = idCH;
    }
}
