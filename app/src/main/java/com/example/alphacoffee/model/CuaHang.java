package com.example.alphacoffee.model;

import java.io.Serializable;

public class CuaHang implements Serializable {
    private String cuaHangId;
    private String tenCuaHang;
    private String soDienThoai;
    private String diaChi;
    private String gioMoCua;
    private String hinhAnh;

    public CuaHang() {
    }

    public CuaHang(String cuaHangId, String tenCuaHang, String soDienThoai, String diaChi, String gioMoCua, String hinhAnh) {
        this.cuaHangId = cuaHangId;
        this.tenCuaHang = tenCuaHang;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.gioMoCua = gioMoCua;
        this.hinhAnh = hinhAnh;
    }

    public String getCuaHangId() {
        return cuaHangId;
    }

    public void setCuaHangId(String cuaHangId) {
        this.cuaHangId = cuaHangId;
    }

    public String getTenCuaHang() {
        return tenCuaHang;
    }

    public void setTenCuaHang(String tenCuaHang) {
        this.tenCuaHang = tenCuaHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getGioMoCua() {
        return gioMoCua;
    }

    public void setGioMoCua(String gioMoCua) {
        this.gioMoCua = gioMoCua;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
