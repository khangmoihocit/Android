package com.khangmoihocit.th_quanlynhansu;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nhanvien")
public class NhanVien {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String hoTen;
    private String chuVu;
    private Double luongCoBan;

    public NhanVien(){}

    public NhanVien(int id, Double luongCoBan, String chuVu, String hoTen) {
        this.id = id;
        this.luongCoBan = luongCoBan;
        this.chuVu = chuVu;
        this.hoTen = hoTen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(Double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public String getChuVu() {
        return chuVu;
    }

    public void setChuVu(String chuVu) {
        this.chuVu = chuVu;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
}
