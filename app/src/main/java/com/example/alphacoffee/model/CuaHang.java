package com.example.alphacoffee.model;

public class CuaHang {
    private String tenCuaHang;
    private String soDienThoai;
    private String diaChi;
    private String gioMoCua;
    private String hinhAnh;

    public CuaHang() {
    }

    public CuaHang(String tenCuaHang, String soDienThoai, String diaChi, String gioMoCua, String hinhAnh) {
        this.tenCuaHang = tenCuaHang;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.gioMoCua = gioMoCua;
        this.hinhAnh = hinhAnh;
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
