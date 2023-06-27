package com.example.pufinance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class Database {

    Context context;
    String dbname = "PuAndroid.db";

    public Database(Context context) {
        this.context = context;
        createTable();
    }

    public SQLiteDatabase openDB(){
        return context.openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
    }

    public void CloseDB(SQLiteDatabase db){
        db.close();
    }

    public void createTable() {
        SQLiteDatabase db = openDB();
        //Tao bang cho Thu Chi
        String sqlThuChi = "CREATE TABLE IF NOT EXISTS ThuChi (" +
                " ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " loaiTien TEXT," +
                " hangMuc TEXT," +
                " thoiGian TEXT," +
                " ghiChu TEXT," +
                " Tien INTEGER," +
                " dateTime TEXT);";
        String sqlWallet = "CREATE TABLE IF NOT EXISTS Wallet (" +
                " ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " tenVi TEXT," +
                " tongTien INTEGER," +
                " ghiChu TEXT)";
        String sqlTaiKhoan = "CREATE TABLE IF NOT EXISTS TaiKhoan (" +
                " sdt TEXT NOT NULL PRIMARY KEY," +
                " matKhau TEXT)";
        db.execSQL(sqlThuChi);
        db.execSQL(sqlWallet);
        db.execSQL(sqlTaiKhoan);
        CloseDB(db);
    }

    public ArrayList<ThuChi_Info> getAllThuChi(String key) {
        SQLiteDatabase db = openDB();
        ArrayList<ThuChi_Info> arr = new ArrayList<>();
        String sql = "select * from ThuChi where loaiTien = '" + key + "'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int ID = csr.getInt(0);
                    String loaiTien = csr.getString(1);
                    String hangMuc = csr.getString(2);
                    String thoiGian = csr.getString(3);
                    String ghiChu = csr.getString(4);
                    int tien = csr.getInt(5);
                    arr.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                } while (csr.moveToNext());
            }
        }
        CloseDB(db);
        return arr;
    }

    public ArrayList<ThuChi_Info> get10ThuChi() {
        SQLiteDatabase db = openDB();
        ArrayList<ThuChi_Info> arr = new ArrayList<>();
        String sql = "SELECT * FROM ( SELECT * FROM ThuChi ORDER BY ID DESC LIMIT 10) " +
                "ORDER BY ID ASC";
        Cursor csr = db.rawQuery(sql, null);
        int count = 0;
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int ID = csr.getInt(0);
                    String loaiTien = csr.getString(1);
                    String hangMuc = csr.getString(2);
                    String thoiGian = csr.getString(3);
                    String ghiChu = csr.getString(4);
                    int tien = csr.getInt(5);
                    arr.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                    count ++;
                } while (csr.moveToNext() && count < 10);
            }
        }
        CloseDB(db);
        return arr;
    }

    public void insertThuChi(ThuChi_Info thuChi_info, String dateTime) {
        SQLiteDatabase db = openDB();
        ContentValues cv = new ContentValues();
        //Push thong tin
        cv.put("loaiTien", thuChi_info.loaiTien);
        cv.put("hangMuc", thuChi_info.hangMuc);
        cv.put("thoiGian", thuChi_info.thoiGian);
        cv.put("ghiChu", thuChi_info.ghiChu);
        cv.put("Tien", thuChi_info.tien);
        cv.put("dateTime", dateTime);
        db.insert("ThuChi", null, cv);
        CloseDB(db);
    }

    public void insertWallet(Wallet_Info wallet_info) {
        SQLiteDatabase db = openDB();
        ContentValues cv = new ContentValues();
        //Push thong tin
        cv.put("tenVi", wallet_info.tenVi);
        cv.put("tongTien", wallet_info.tongTien);
        cv.put("ghiChu", wallet_info.ghiChu);
        db.insert("Wallet", null, cv);
        CloseDB(db);
    }

    public ArrayList<Wallet_Info> getAllWallet() {
        SQLiteDatabase db = openDB();
        ArrayList<Wallet_Info> arr = new ArrayList<>();
        String sql = "select * from Wallet";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int ID = csr.getInt(0);
                    String tenVi = csr.getString(1);
                    int tongTien = csr.getInt(2);
                    String ghiChu = csr.getString(3);
                    arr.add(0, new Wallet_Info (ID, tongTien, tenVi, ghiChu));
                } while (csr.moveToNext());
            }
        }
        CloseDB(db);
        return arr;
    }

    public int TongTienWallet() {
        SQLiteDatabase db = openDB();
        int total = 0;
        String sql = "select * from Wallet";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int tongTien = csr.getInt(2);
                    total += tongTien;
                } while (csr.moveToNext());
            }
        }
        CloseDB(db);
        return total;
    }

    public ArrayList<Account_Info> getALlAccount() {
        SQLiteDatabase db = openDB();
        ArrayList<Account_Info> arr = new ArrayList<>();
        String sql = "select * from TaiKhoan";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    String sdt = csr.getString(0);
                    String pwd = csr.getString(1);
                    arr.add(0, new Account_Info (sdt, pwd));
                } while (csr.moveToNext());
            }
        }
        CloseDB(db);
        return arr;
    }

    public void insertAccount(Account_Info account_info) {
        SQLiteDatabase db = openDB();
        ContentValues values = new ContentValues();
        values.put("sdt", account_info.getSdt());
        values.put("matKhau", account_info.getMatKhau());
        db.insert("TaiKhoan", null, values);
        CloseDB(db);
    }

    public void EditWallet(Wallet_Info wallet_info) {
        SQLiteDatabase db = openDB();
        String sql = "update Wallet " +
                "set tenVi = '" + wallet_info.tenVi + "', tongTien = " + wallet_info.tongTien + ", ghiChu = '" + wallet_info.ghiChu + "'" +
                "where ID = " + wallet_info.ID;
        db.execSQL(sql);
        CloseDB(db);
    }

}
