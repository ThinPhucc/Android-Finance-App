package com.example.pufinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class All_ThuChi extends AppCompatActivity implements ThuChi_Adapter.onCLick_ThuChi{
    Spinner spinner;
    TextView thu, chi, locThu, locChi, timeFrom, timeTo;
    RecyclerView recyclerView;
    ThuChi_Adapter thuChi_adapter;
    Database database;
    Button back;
    LinearLayout khac;
    String key = "Thu", datetime_From = "", datetime_To = "";
    DatePickerDialog.OnDateSetListener listenFrom, listenTo, listen;
    ArrayList<ThuChi_Info> List_ThuChi;
    String thoiGian = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_thu_chi);
        // Khởi tạo
        thu = findViewById(R.id.allthuchi_txt_thu);
        chi = findViewById(R.id.allthuchi_txt_chi);
        locThu = findViewById(R.id.allthuchi_txt_locthu);
        locChi = findViewById(R.id.allthuchi_txt_locchi);
        timeFrom = findViewById(R.id.allthuchu_txt_from);
        timeTo = findViewById(R.id.allthuchu_txt_to);
        recyclerView = findViewById(R.id.allthuchi_recyclerview);
        back = findViewById(R.id.allthuchi_btn_back);
        khac = findViewById(R.id.linearLayout_khac);
        database = new Database(this);
        khac.setVisibility(LinearLayout.GONE);
        // Load data
        List_ThuChi = database.getAllThuChi(key);
        thuChi_adapter = new ThuChi_Adapter(this, List_ThuChi, database, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(thuChi_adapter);

        // Spinner
        spinner = findViewById(R.id.allthuchi_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.custom_text_spinner_allthuchi, thoiGian());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LocThuChi(i);
                if (i == 7) {
                    List_ThuChi.clear();
                    thuChi_adapter.notifyDataSetChanged();
                    timeFrom.setText(null);
                    timeTo.setText(null);
                    datetime_From = ""; datetime_To = "";
                    thu.setText("0");
                    chi.setText("0");
                    khac.setVisibility(LinearLayout.VISIBLE);
                }
                else
                khac.setVisibility(LinearLayout.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Lấy ngày tháng hiện tại
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Khởi tạo Listen để Dialog DateTime có thể nghe lựa chọn ng dùng.
        listenFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearr, int monthh, int dayy) {
                Calendar tmp = Calendar.getInstance();
                tmp.set(yearr, monthh, dayy);
                monthh = monthh + 1;
                if (tmp.get(Calendar.DAY_OF_WEEK) == 1){
                    String datetime = "CN" + ": " +
                            dayy + "-" + monthh + "-" + yearr;
                    timeFrom.setText(datetime);
                }
                else {
                    String datetime = "T" + tmp.get(Calendar.DAY_OF_WEEK) + ": " +
                            dayy + "-" + monthh + "-" + yearr;
                    timeFrom.setText(datetime);
                }
                // Lưu dữ liệu và gọi hàm để xử lý
                datetime_From = dayy + "-" + monthh + "-" + yearr;
                Filter_DateTime();
            }
        };
        listenTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearr, int monthh, int dayy) {
                Calendar tmp = Calendar.getInstance();
                tmp.set(yearr, monthh, dayy);
                monthh = monthh + 1;
                if (tmp.get(Calendar.DAY_OF_WEEK) == 1){
                    String datetime = "CN" + ": " +
                            dayy + "-" + monthh + "-" + yearr;
                    timeTo.setText(datetime);
                }
                else {
                    String datetime = "T" + tmp.get(Calendar.DAY_OF_WEEK) + ": " +
                            dayy + "-" + monthh + "-" + yearr;
                    timeTo.setText(datetime);
                }
                // Lưu dữ liệu và gọi hàm để xử lý
                datetime_To = dayy + "-" + monthh + "-" + yearr;
                Filter_DateTime();
            }
        };
        // Set DatePicker cho TextView với ngày tháng bên trên.
        timeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datetime = new DatePickerDialog(All_ThuChi.this, R.style.Theme_AppCompat_Dialog_MinWidth,
                        listenFrom , year, month, day);
                datetime.getWindow().setBackgroundDrawable(new ColorDrawable(0xC8ED365A));
                datetime.show();
            }
        });
        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datetime = new DatePickerDialog(All_ThuChi.this, R.style.Theme_AppCompat_Dialog_MinWidth,
                        listenTo , year, month, day);
                datetime.getWindow().setBackgroundDrawable(new ColorDrawable(0xC8ED365A));
                datetime.show();
            }
        });

        // Lọc Thu
        locThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = "Thu";
                locThu.setBackgroundResource(R.drawable.botron_top_left_locthuchi);
                locChi.setBackgroundColor(Color.parseColor("#FFFFFF"));
                LocThuChi(spinner.getSelectedItemPosition());
                if (spinner.getSelectedItemPosition() == 7)
                    Filter_DateTime();
            }
        });

        // Lọc Chi
        locChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = "Chi";
                locChi.setBackgroundResource(R.drawable.botron_top_right_locthuchi);
                locThu.setBackgroundColor(Color.parseColor("#FFFFFF"));
                LocThuChi(spinner.getSelectedItemPosition());
                if (spinner.getSelectedItemPosition() == 7)
                    Filter_DateTime();
            }
        });

        // Back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(All_ThuChi.this, MainActivity.class));
            }
        });
    }

    public ArrayList<String> thoiGian() {
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add("Tất cả");
        tmp.add("Hôm nay");
        tmp.add("Hôm qua");
        tmp.add("Tuần này");
        tmp.add("Tuần trước");
        tmp.add("Tháng này");
        tmp.add("Tháng trước");
        tmp.add("Khác");
        return tmp;
    }

    public void LocThuChi(int i) {
        int thu = 0;
        int chi = 0;
        switch (i) {
            case 0:
                thu = LinhTinh.GetThu_All(database);
                chi = LinhTinh.GetChi_All(database);
                Load_RecyclerView(database.getAllThuChi(key), thu, chi);
                break;
            case 1:
                thu = LinhTinh.Thu_HomNay(database);
                chi = LinhTinh.Chi_HomNay(database);
                Load_RecyclerView(LocThuChi.ThuChi_HomNay(database, key), thu, chi);
                break;
            case 2:
                thu = LinhTinh.Thu_HomQua(database);
                chi = LinhTinh.Chi_HomQua(database);
                Load_RecyclerView(LocThuChi.ThuChi_HomQua(database, key), thu, chi);
                break;
            case 3:
                thu = LinhTinh.Thu_TuanNay(database);
                chi = LinhTinh.Chi_TuanNay(database);
                Load_RecyclerView(LocThuChi.ThuChi_TuanNay(database, key), thu, chi);
                break;
            case 4:
                thu = LinhTinh.Thu_TuanTruoc(database);
                chi = LinhTinh.Chi_TuanTruoc(database);
                Load_RecyclerView(LocThuChi.ThuChi_TuanTruoc(database, key), thu, chi);
                break;
            case 5:
                thu = LinhTinh.Thu_ThangNay(database);
                chi = LinhTinh.Chi_ThangNay(database);
                Load_RecyclerView(LocThuChi.ThuChi_ThangNay(database, key), thu, chi);
                break;
            case 6:
                thu = LinhTinh.Thu_ThangTruoc(database);
                chi = LinhTinh.Chi_ThangTruoc(database);
                Load_RecyclerView(LocThuChi.ThuChi_ThangTruoc(database, key), thu, chi);
                break;
        }
    }

    public void Load_RecyclerView(ArrayList<ThuChi_Info> arrayList, int thuu, int chii) {
        List_ThuChi.clear();
        List_ThuChi.addAll(arrayList);
        thuChi_adapter.notifyDataSetChanged();
        thu.setText(LinhTinh.SplitNumber(thuu));
        chi.setText(LinhTinh.SplitNumber(chii));
    }

    public void Filter_DateTime() {
        if (datetime_From == "" || datetime_To == "")
            return;
        else {
            List_ThuChi.clear();
            List_ThuChi.addAll(LocThuChi.ThuChi_DateTimeCusTom(database, key, datetime_From, datetime_To));
            thuChi_adapter.notifyDataSetChanged();
            thu.setText(LinhTinh.SplitNumber(LinhTinh.Thu_DateTimeCusTom(database, datetime_From, datetime_To)));
            chi.setText(LinhTinh.SplitNumber(LinhTinh.Chi_DateTimeCusTom(database, datetime_From, datetime_To)));
        }
    }

    @Override
    public void onClick(ThuChi_Info thuChi_info, int keyy) {
        if (keyy == 0) {
            String[] time_split = thuChi_info.thoiGian.split(" ");
            thoiGian = time_split[2];
            // Setup Dialog
            final Dialog dialog = new Dialog(All_ThuChi.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_edit_thuchi);
            Window window = dialog.getWindow();
            if (window == null)
                return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);
            // Setup View
            final EditText[] Tien = new EditText[1];
            final EditText[] ghiChu = new EditText[1];
            TextView dateTime;
            final Spinner[] loaiTien = new Spinner[1];
            final Spinner[] hangMuc = new Spinner[1];
            Tien[0] = dialog.findViewById(R.id.thuchi_edit_txt_tien);
            ghiChu[0] = dialog.findViewById(R.id.thuchi_edit_txt_ghichu);
            dateTime = dialog.findViewById(R.id.thuchi_edit_txt_date);
            loaiTien[0] = dialog.findViewById(R.id.edit_spinner_loaitien);
            hangMuc[0] = dialog.findViewById(R.id.edit_spinner_hangmuc);
            // Spinner
            ArrayAdapter<CharSequence> adapter_loaitien = ArrayAdapter.createFromResource(All_ThuChi.this,
                    R.array.spiner_item, R.layout.custom_text_spinner);
            adapter_loaitien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            loaiTien[0].setAdapter(adapter_loaitien);
            loaiTien[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ArrayList<String> tmp = Hangmuc(i);
                    ArrayAdapter<String> adapter_hangmuc = new ArrayAdapter(All_ThuChi.this, R.layout.custom_text_spinner, tmp);
                    adapter_hangmuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    hangMuc[0].setAdapter(adapter_hangmuc);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) { }
            });
            // Set data
            Tien[0].setText(String.valueOf(thuChi_info.tien));
            loaiTien[0].setSelection(adapter_loaitien.getPosition(thuChi_info.loaiTien + ""));
            dateTime.setText(thuChi_info.thoiGian);
            hangMuc[0].setSelection(adapter_loaitien.getPosition(thuChi_info.hangMuc + ""));
            ghiChu[0].setText(thuChi_info.ghiChu);
            // DateTime Dialog
            listen = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yearr, int monthh, int dayy) {
                    Calendar tmp = Calendar.getInstance();
                    tmp.set(yearr, monthh, dayy);
                    monthh = monthh + 1;
                    if (tmp.get(Calendar.DAY_OF_WEEK) == 1){
                        String datetime = "Chủ Nhật" + ": " +
                                dayy + "-" + monthh + "-" + yearr;
                        dateTime.setText(datetime);
                    }
                    else {
                        String datetime = "Thứ " + tmp.get(Calendar.DAY_OF_WEEK) + ": " +
                                dayy + "-" + monthh + "-" + yearr;
                        dateTime.setText(datetime);
                    }
                    // Lấy thời gian
                    thoiGian = dayy + "-" + monthh + "-" + yearr;
                }
            };
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            dateTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datetime = new DatePickerDialog(All_ThuChi.this, R.style.Theme_AppCompat_Dialog_MinWidth,
                            listen , year, month, day);
                    datetime.getWindow().setBackgroundDrawable(new ColorDrawable(0xC8ED365A));
                    datetime.show();
                }
            });
            dialog.findViewById(R.id.thuchi_edit_btn_huy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.thuchi_edit_btn_luu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Tien[0].getText().toString().equals("")) {
                        Toast.makeText(All_ThuChi.this, "Mời nhập số tiền", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        LinhTinh.DeleteThuChi(database, thuChi_info.ID);
                        // Lấy dữ liệu
                        String spinner_loaitien = loaiTien[0].getSelectedItem().toString();
                        String spinner_hangmuc = hangMuc[0].getSelectedItem().toString();
                        String ThoiGian = dateTime.getText().toString();
                        String GhiChu = ghiChu[0].getText().toString();
                        int tien = Integer.parseInt(Tien[0].getText().toString());
                        // Add
                        ThuChi_Info tmp = new ThuChi_Info(spinner_loaitien, spinner_hangmuc, ThoiGian, GhiChu, tien);
                        database.insertThuChi(tmp, thoiGian);
                        Toast.makeText(All_ThuChi.this, "Thành Công", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    LinhTinh.DeleteThuChi(database, thuChi_info.ID);
                    LocThuChi(spinner.getSelectedItemPosition());
                    if (spinner.getSelectedItemPosition() == 7)
                        Filter_DateTime();
                }
            });
            dialog.show();
        }
        else {
            LinhTinh.DeleteThuChi(database, thuChi_info.ID);
            LocThuChi(spinner.getSelectedItemPosition());
            if (spinner.getSelectedItemPosition() == 7)
                Filter_DateTime();
        }
    }

    public ArrayList<String> Hangmuc (int i){
        ArrayList<String> tmp = new ArrayList<>();
        if (i == 0){
            tmp.add("Tiền Lương");
            tmp.add("Thu Nợ");
            tmp.add("Tiền Thưởng");
        }
        else {
            if (i == 1){
                tmp.add("Đi Chợ");
                tmp.add("Ăn Uống");
                tmp.add("Đi Chơi");
                tmp.add("Tiền Điện");
                tmp.add("Tiền Nước");
                tmp.add("Gas");
                tmp.add("Tiền Lương");
                tmp.add("Mua Sắm");
                tmp.add("Cho Vay");
            }
        }
        return tmp;
    }
    // END
}