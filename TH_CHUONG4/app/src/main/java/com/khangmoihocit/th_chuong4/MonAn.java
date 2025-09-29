package com.khangmoihocit.th_chuong4;

public class MonAn {
    private int id;
    private String tenMon;
    private String gia;
    private String moTa;
    private int hinhAnh;

    public MonAn(int id, String tenMon, String gia, String moTa, int hinhAnh) {
        this.id = id;
        this.tenMon = tenMon;
        this.gia = gia;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }

    public int getId() {
        return id;
    }

    public String getTenMon() {
        return tenMon;
    }

    public String getGia() {
        return gia;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getHinhAnh() {
        return hinhAnh;
    }
}
