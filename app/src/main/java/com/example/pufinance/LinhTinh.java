package com.example.pufinance;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;

public class LinhTinh extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static String SplitNumber(int num) {
        String result = String.format("%,d", num);
        return result;
    }

    public static void DeleteThuChi(Database database, int ID) {
        SQLiteDatabase db = database.openDB();
        String sql = "DELETE FROM ThuChi " +
                "WHERE ID = " + ID;
        db.execSQL(sql);
        database.CloseDB(db);
    }

    public static void DeleteWallet(Database database, Wallet_Info wallet_info) {
        SQLiteDatabase db = database.openDB();
        String sql = "DELETE FROM Wallet " +
                "WHERE ID = " + wallet_info.ID;
        db.execSQL(sql);
        database.CloseDB(db);
    }

    public static int GetThu_All(Database database) {
        SQLiteDatabase db = database.openDB();
        int total = 0;
        String sql = "select * from ThuChi " +
                "where loaiTien = 'Thu'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int tien = csr.getInt(5);
                    total += tien;
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return total;
    }

    public static int GetChi_All(Database database) {
        SQLiteDatabase db = database.openDB();
        int total = 0;
        String sql = "select * from ThuChi " +
                "where loaiTien = 'Chi'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int tien = csr.getInt(5);
                    total += tien;
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return total;
    }

    public static int Thu_HomNay (Database database) {
        int tongThu = 0;
        Calendar calendar = Calendar.getInstance();
        // Lấy ngày tháng hôm nay
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = day + "-" + (month + 1) + "-" + year;
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where dateTime = '" + date + "' and loaiTien = 'Thu'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int tien = csr.getInt(5);
                    tongThu += tien;
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongThu;
    }

    public static int Chi_HomNay (Database database) {
        int tongChi = 0;
        Calendar calendar = Calendar.getInstance();
        // Lấy ngày tháng hôm nay
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = day + "-" + (month + 1) + "-" + year;
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where dateTime = '" + date + "' and loaiTien = 'Chi'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int tien = csr.getInt(5);
                    tongChi += tien;
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongChi;
    }

    public static int Thu_TuanNay (Database database) {
        int tongThu = 0;
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
        String sql = "select * from ThuChi where loaiTien = 'Thu'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
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
                                    tongThu += tien;
                            }
                            else {
                                if (first_month != last_month) {
                                    if (last_month == thang || first_month == thang) {
                                        if (ngay >= first_day || ngay <= last_day)
                                            tongThu += tien;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (nam == first_year || nam == last_year) {
                            if (thang == 12 || thang == 1) {
                                if (ngay >= first_day || ngay <= last_day)
                                    tongThu += tien;
                            }
                        }
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongThu;
    }

    public static int Chi_TuanNay (Database database) {
        int tongChi = 0;
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
        String sql = "select * from ThuChi where loaiTien = 'Chi'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
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
                                    tongChi += tien;
                            }
                            else {
                                if (first_month != last_month) {
                                    if (last_month == thang || first_month == thang) {
                                        if (ngay >= first_day || ngay <= last_day)
                                            tongChi += tien;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (nam == first_year || nam == last_year) {
                            if (thang == 12 || thang == 1) {
                                if (ngay >= first_day || ngay <= last_day)
                                    tongChi += tien;
                            }
                        }
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongChi;
    }

    public static int Thu_ThangNay (Database database) {
        int tongThu = 0;
        Calendar calendar = Calendar.getInstance();
        // Lấy tháng năm
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where loaiTien = 'Thu'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int tien = csr.getInt(5);
                    String dateTime = csr.getString(6);
                    String[] tmp = dateTime.split("-");
                    int thang = Integer.parseInt(tmp[1]);
                    int nam = Integer.parseInt(tmp[2]);
                    if (nam == year && thang == month) {
                        tongThu += tien;
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongThu;
    }

    public static int Chi_ThangNay (Database database) {
        int tongChi = 0;
        Calendar calendar = Calendar.getInstance();
        // Lấy tháng năm
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where loaiTien = 'Chi'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int tien = csr.getInt(5);
                    String dateTime = csr.getString(6);
                    String[] tmp = dateTime.split("-");
                    int thang = Integer.parseInt(tmp[1]);
                    int nam = Integer.parseInt(tmp[2]);
                    if (nam == year && thang == month) {
                        tongChi += tien;
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongChi;
    }

    public static int Thu_HomQua (Database database) {
        int tongThu = 0;
        Calendar calendar = Calendar.getInstance();
        // Chỉnh lịch lùi 1 hôm
        calendar.add(calendar.DAY_OF_MONTH, -1);
        // Lấy ngày tháng hôm qua
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = day + "-" + (month + 1) + "-" + year;
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where dateTime = '" + date + "' and loaiTien = 'Thu'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int tien = csr.getInt(5);
                    tongThu += tien;
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongThu;
    }

    public static int Chi_HomQua (Database database) {
        int tongChi = 0;
        Calendar calendar = Calendar.getInstance();
        // Chỉnh lịch lùi 1 hôm
        calendar.add(calendar.DAY_OF_MONTH, -1);
        // Lấy ngày tháng hôm qua
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = day + "-" + (month + 1) + "-" + year;
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where dateTime = '" + date + "' and loaiTien = 'Chi'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int tien = csr.getInt(5);
                    tongChi += tien;
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongChi;
    }

    public static int Thu_TuanTruoc (Database database) {
        int tongThu = 0;
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
        String sql = "select * from ThuChi where loaiTien = 'Thu'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
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
                                    tongThu += tien;
                            }
                            else {
                                if (first_month != last_month) {
                                    if (last_month == thang || first_month == thang) {
                                        if (ngay >= first_day || ngay <= last_day)
                                            tongThu += tien;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (nam == first_year || nam == last_year) {
                            if (thang == 12 || thang == 1) {
                                if (ngay >= first_day || ngay <= last_day)
                                    tongThu += tien;
                            }
                        }
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongThu;
    }

    public static int Chi_TuanTruoc (Database database) {
        int tongChi = 0;
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
        String sql = "select * from ThuChi where loaiTien = 'Chi'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
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
                                    tongChi += tien;
                            }
                            else {
                                if (first_month != last_month) {
                                    if (last_month == thang || first_month == thang) {
                                        if (ngay >= first_day || ngay <= last_day)
                                            tongChi += tien;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (nam == first_year || nam == last_year) {
                            if (thang == 12 || thang == 1) {
                                if (ngay >= first_day || ngay <= last_day)
                                    tongChi += tien;
                            }
                        }
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongChi;
    }

    public static int Thu_ThangTruoc (Database database) {
        int tongThu = 0;
        Calendar calendar = Calendar.getInstance();
        // Lấy tháng năm
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where loaiTien = 'Thu'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int tien = csr.getInt(5);
                    String dateTime = csr.getString(6);
                    String[] tmp = dateTime.split("-");
                    int thang = Integer.parseInt(tmp[1]);
                    int nam = Integer.parseInt(tmp[2]);
                    if (nam == year && thang == month) {
                        tongThu += tien;
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongThu;
    }

    public static int Chi_ThangTruoc (Database database) {
        int tongChi = 0;
        Calendar calendar = Calendar.getInstance();
        // Lấy tháng năm
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where loaiTien = 'Chi'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int tien = csr.getInt(5);
                    String dateTime = csr.getString(6);
                    String[] tmp = dateTime.split("-");
                    int thang = Integer.parseInt(tmp[1]);
                    int nam = Integer.parseInt(tmp[2]);
                    if (nam == year && thang == month) {
                        tongChi += tien;
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongChi;
    }

    public static int Thu_DateTimeCusTom (Database database, String datetime_From, String datetime_To) {
        int tongThu = 0;
        // Tách chuỗi để lấy giá trị tí dùng.
        String[] tmp1 = datetime_From.split("-");
        String[] tmp2 = datetime_To.split("-");
        // Tạo lịch với dữ liệu được đưa vào để so sánh cho dễ .-. Dữ liệu: dd-mm-yyyy
        Calendar date_from = Calendar.getInstance();
        date_from.set(Integer.parseInt(tmp1[2]), Integer.parseInt(tmp1[1]), Integer.parseInt(tmp1[0]), 00, 00, 00);
        Calendar date_to = Calendar.getInstance();
        date_to.set(Integer.parseInt(tmp2[2]), Integer.parseInt(tmp2[1]), Integer.parseInt(tmp2[0]), 23, 59, 59);
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where loaiTien = 'Thu'";
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
                        tongThu += tien;
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongThu;
    }

    public static int Chi_DateTimeCusTom (Database database, String datetime_From, String datetime_To) {
        int tongChi = 0;
        // Tách chuỗi để lấy giá trị tí dùng.
        String[] tmp1 = datetime_From.split("-");
        String[] tmp2 = datetime_To.split("-");
        // Tạo lịch với dữ liệu được đưa vào để so sánh cho dễ .-. Dữ liệu: dd-mm-yyyy
        Calendar date_from = Calendar.getInstance();
        date_from.set(Integer.parseInt(tmp1[2]), Integer.parseInt(tmp1[1]), Integer.parseInt(tmp1[0]), 00, 00, 00);
        Calendar date_to = Calendar.getInstance();
        date_to.set(Integer.parseInt(tmp2[2]), Integer.parseInt(tmp2[1]), Integer.parseInt(tmp2[0]), 23, 59, 59);
        SQLiteDatabase db = database.openDB();
        String sql = "select * from ThuChi where loaiTien = 'Chi'";
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
                        tongChi += tien;
                    }
                } while (csr.moveToNext());
            }
        }
        database.CloseDB(db);
        return tongChi;
    }
}