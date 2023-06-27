package com.example.pufinance;

public class Wallet_Info {
    int ID, tongTien;
    String tenVi, ghiChu;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public String getTenVi() {
        return tenVi;
    }

    public void setTenVi(String tenVi) {
        this.tenVi = tenVi;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Wallet_Info(int ID, int tongTien, String tenVi, String ghiChu) {
        this.ID = ID;
        this.tongTien = tongTien;
        this.tenVi = tenVi;
        this.ghiChu = ghiChu;
    }

    public Wallet_Info(int tongTien, String tenVi, String ghiChu) {
        this.tongTien = tongTien;
        this.tenVi = tenVi;
        this.ghiChu = ghiChu;
    }
}
