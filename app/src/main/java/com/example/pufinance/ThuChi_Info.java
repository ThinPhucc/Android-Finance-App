package com.example.pufinance;

import java.io.Serializable;

public class ThuChi_Info implements Serializable {

    String loaiTien, hangMuc, thoiGian, ghiChu;
    int tien, ID;

    public String getLoaiTien() {
        return loaiTien;
    }

    public void setLoaiTien(String loaiTien) {
        this.loaiTien = loaiTien;
    }

    public String getHangMuc() {
        return hangMuc;
    }

    public void setHangMuc(String hangMuc) {
        this.hangMuc = hangMuc;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getTien() {
        return tien;
    }

    public void setTien(int tien) {
        this.tien = tien;
    }

    public ThuChi_Info(int ID, String loaiTien, String hangMuc, String thoiGian, String ghiChu, int tien) {
        this.ID = ID;
        this.loaiTien = loaiTien;
        this.hangMuc = hangMuc;
        this.thoiGian = thoiGian;
        this.ghiChu = ghiChu;
        this.tien = tien;
    }
    public ThuChi_Info(String loaiTien, String hangMuc, String thoiGian, String ghiChu, int tien) {
        this.ID = ID;
        this.loaiTien = loaiTien;
        this.hangMuc = hangMuc;
        this.thoiGian = thoiGian;
        this.ghiChu = ghiChu;
        this.tien = tien;
    }
}
