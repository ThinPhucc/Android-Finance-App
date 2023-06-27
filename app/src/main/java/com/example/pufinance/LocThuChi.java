package com.example.pufinance;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LocThuChi extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static ArrayList<ThuChi_Info> ThuChi_HomNay (Database database, String key) {
        ArrayList<ThuChi_Info> thuChi_info = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        // Lấy ngày tháng hôm nay
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = day + "-" + (month + 1) + "-" + year;
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where dateTime = '" + date + "' and loaiTien = '" + key + "'";
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
                    thuChi_info.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return thuChi_info;
    }

    public static ArrayList<ThuChi_Info> ThuChi_HomQua (Database database, String key) {
        ArrayList<ThuChi_Info> thuChi_info = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        // Chỉnh lịch lùi 1 hôm
        calendar.add(calendar.DAY_OF_MONTH, -1);
        // Lấy ngày tháng hôm qua
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = day + "-" + (month + 1) + "-" + year;
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where dateTime = '" + date + "' and loaiTien = '" + key + "'";
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
                    thuChi_info.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return thuChi_info;
    }

    public static ArrayList<ThuChi_Info> ThuChi_TuanNay (Database database, String key) {
        ArrayList<ThuChi_Info> thuChi_info = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        // Chỉnh ngày bắt đầu trong tuần là thứ 2 -> chỉnh lịch "đưa ngày hiện tại về thứ 2"
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int first_day = calendar.get(Calendar.DAY_OF_MONTH);
        int first_month = calendar.get(Calendar.MONTH) + 1;
        int first_year = calendar.get(Calendar.YEAR);
        // Chỉnh lịch tăng thêm 6 ngày nữa để lấy chủ nhật.
        calendar.add(calendar.DAY_OF_MONTH, 6);
        int last_day = calendar.get(Calendar.DAY_OF_MONTH);
        int last_month = calendar.get(Calendar.MONTH) + 1;
        int last_year = calendar.get(Calendar.YEAR);
        // Check
        SQLiteDatabase db = database.openDB();
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
                    String dateTime = csr.getString(6);
                    String[] tmp = dateTime.split("-");
                    int ngay = Integer.parseInt(tmp[0]);
                    int thang = Integer.parseInt(tmp[1]);
                    int nam = Integer.parseInt(tmp[2]);
                    if (first_year == last_year) {
                        if (nam == first_year || nam == last_year) {
                            if (first_month == last_month) {
                                if (last_month == thang && ngay >= first_day && ngay <= last_day)
                                    thuChi_info.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                            }
                            else {
                                if (first_month != last_month) {
                                    if (last_month == thang || first_month == thang) {
                                        if (ngay >= first_day || ngay <= last_day)
                                            thuChi_info.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (nam == first_year || nam == last_year) {
                            if (thang == 12 || thang == 1) {
                                if (ngay >= first_day || ngay <= last_day)
                                    thuChi_info.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                            }
                        }
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return thuChi_info;
    }

    public static ArrayList<ThuChi_Info> ThuChi_TuanTruoc (Database database, String key) {
        ArrayList<ThuChi_Info> thuChi_info = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        // Chỉnh ngày bắt đầu trong tuần là thứ 2 -> chỉnh lịch "đưa ngày hiện tại về thứ 2"
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // Lấy ngày CN tuần trước làm mốc cuối của tuần
        calendar.add(calendar.DAY_OF_MONTH, -1);
        int last_day = calendar.get(Calendar.DAY_OF_MONTH);
        int last_month = calendar.get(Calendar.MONTH) + 1;
        int last_year = calendar.get(Calendar.YEAR);
        // Chỉnh lịch giảm 7 ngày để lấy thứ 2 tuần trước.
        calendar.add(calendar.DAY_OF_MONTH, -6);
        int first_day = calendar.get(Calendar.DAY_OF_MONTH);
        int first_month = calendar.get(Calendar.MONTH) + 1;
        int first_year = calendar.get(Calendar.YEAR);
        // Check
        SQLiteDatabase db = database.openDB();
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
                    String dateTime = csr.getString(6);
                    String[] tmp = dateTime.split("-");
                    int ngay = Integer.parseInt(tmp[0]);
                    int thang = Integer.parseInt(tmp[1]);
                    int nam = Integer.parseInt(tmp[2]);
                    if (first_year == last_year) {
                        if (nam == first_year || nam == last_year) {
                            if (first_month == last_month) {
                                if (last_month == thang && ngay >= first_day && ngay <= last_day)
                                    thuChi_info.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                            }
                            else {
                                if (first_month != last_month) {
                                    if (last_month == thang || first_month == thang) {
                                        if (ngay >= first_day || ngay <= last_day)
                                            thuChi_info.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (nam == first_year || nam == last_year) {
                            if (thang == 12 || thang == 1) {
                                if (ngay >= first_day || ngay <= last_day)
                                    thuChi_info.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                            }
                        }
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return thuChi_info;
    }

    public static ArrayList<ThuChi_Info> ThuChi_ThangNay (Database database, String key) {
        ArrayList<ThuChi_Info> thuChi_info = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        // Lấy tháng năm
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        SQLiteDatabase db = database.openDB();
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
                    String dateTime = csr.getString(6);
                    String[] tmp = dateTime.split("-");
                    int thang = Integer.parseInt(tmp[1]);
                    int nam = Integer.parseInt(tmp[2]);
                    if (nam == year && thang == month) {
                        thuChi_info.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return thuChi_info;
    }

    public static ArrayList<ThuChi_Info> ThuChi_ThangTruoc (Database database, String key) {
        ArrayList<ThuChi_Info> thuChi_info = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        // Lấy tháng năm
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        SQLiteDatabase db = database.openDB();
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
                    String dateTime = csr.getString(6);
                    String[] tmp = dateTime.split("-");
                    int thang = Integer.parseInt(tmp[1]);
                    int nam = Integer.parseInt(tmp[2]);
                    if (nam == year && thang == month) {
                        thuChi_info.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return thuChi_info;
    }

    public static ArrayList<ThuChi_Info> ThuChi_DateTimeCusTom (Database database, String key, String datetime_From, String datetime_To) {
        ArrayList<ThuChi_Info> thuChi_info = new ArrayList<>();
        // Tách chuỗi để lấy giá trị tí dùng.
        String[] tmp1 = datetime_From.split("-");
        String[] tmp2 = datetime_To.split("-");
        // Tạo lịch với dữ liệu được đưa vào để so sánh cho dễ .-. Dữ liệu: dd-mm-yyyy
        Calendar date_from = Calendar.getInstance();
        date_from.set(Integer.parseInt(tmp1[2]), Integer.parseInt(tmp1[1]), Integer.parseInt(tmp1[0]), 00, 00, 00);
        Calendar date_to = Calendar.getInstance();
        date_to.set(Integer.parseInt(tmp2[2]), Integer.parseInt(tmp2[1]), Integer.parseInt(tmp2[0]), 23, 59, 59);
        SQLiteDatabase db = database.openDB();
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
                    String[] tmp3 = csr.getString(6).split("-");
                    Calendar check = Calendar.getInstance();
                    check.set(Integer.parseInt(tmp3[2]), Integer.parseInt(tmp3[1]), Integer.parseInt(tmp3[0]), 00, 00, 00);
                    if (check.after(date_from) && check.before(date_to) || check.equals(date_from) || check.equals(date_to)) {
                        thuChi_info.add(0, new ThuChi_Info (ID, loaiTien, hangMuc, thoiGian, ghiChu, tien));
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return thuChi_info;
    }
}
