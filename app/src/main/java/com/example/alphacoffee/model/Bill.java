package com.example.alphacoffee.model;

import java.io.Serializable;

public class Bill implements Serializable {

    private String idBill;
    private String soThuTu;
    private String ngayTao;
    private long tongtien;
    private String trangThai;
    private String diaDiem;
    private String ghiChu;
    private String loaiThanhToan;
    private String tichDiem;
    private String idKH;
    private String tenKH;
    private String tenCH;
    private String tenNV;
    private String idNV;

    public Bill() {
    }

    public Bill(String idBill, String soThuTu, String ngayTao, long tongtien, String trangThai, String diaDiem, String ghiChu, String loaiThanhToan, String tichDiem, String idKH, String tenKH, String tenCH, String tenNV, String idNV) {
        this.idBill = idBill;
        this.soThuTu = soThuTu;
        this.ngayTao = ngayTao;
        this.tongtien = tongtien;
        this.trangThai = trangThai;
        this.diaDiem = diaDiem;
        this.ghiChu = ghiChu;
        this.loaiThanhToan = loaiThanhToan;
        this.tichDiem = tichDiem;
        this.idKH = idKH;
        this.tenKH = tenKH;
        this.tenCH = tenCH;
        this.tenNV = tenNV;
        this.idNV = idNV;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public String getSoThuTu() {
        return soThuTu;
    }

    public void setSoThuTu(String soThuTu) {
        this.soThuTu = soThuTu;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public long getTongtien() {
        return tongtien;
    }

    public void setTongtien(long tongtien) {
        this.tongtien = tongtien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getLoaiThanhToan() {
        return loaiThanhToan;
    }

    public void setLoaiThanhToan(String loaiThanhToan) {
        this.loaiThanhToan = loaiThanhToan;
    }

    public String getTichDiem() {
        return tichDiem;
    }

    public void setTichDiem(String tichDiem) {
        this.tichDiem = tichDiem;
    }

    public String getIdKH() {
        return idKH;
    }

    public void setIdKH(String idKH) {
        this.idKH = idKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getTenCH() {
        return tenCH;
    }

    public void setTenCH(String tenCH) {
        this.tenCH = tenCH;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getIdNV() {
        return idNV;
    }

    public void setIdNV(String idNV) {
        this.idNV = idNV;
    }
}
