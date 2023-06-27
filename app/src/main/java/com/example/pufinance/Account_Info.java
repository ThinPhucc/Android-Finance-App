package com.example.pufinance;

public class Account_Info {
    String matKhau, sdt;

    public Account_Info(String sdt, String matKhau) {
        this.matKhau = matKhau;
        this.sdt = sdt;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
