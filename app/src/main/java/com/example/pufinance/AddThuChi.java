package com.example.pufinance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddThuChi extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner_loaitien, spinner_hangmuc;
    TextView txtdate;
    EditText txtTien, txtghiChu;
    String loaiTien, hangMuc, thoiGian, ghiChu;
    String dateTime = "";
    int Tien;
    Button save, back;
    DatePickerDialog.OnDateSetListener listen;
    Database database;
    int Parent_Activity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thu_chi);
        database = new Database(this);

        txtTien = findViewById(R.id.addthuchi_txt_tien);
        txtghiChu = findViewById(R.id.addtthuchi_txt_ghichu);
        txtdate = findViewById(R.id.addthuchi_txt_date);
        save = findViewById(R.id.addthuchi_btn_save);
        back = findViewById(R.id.addthuchi_btn_back);
        spinner_loaitien = findViewById(R.id.addthuchi_spinner_loaitien);
        spinner_hangmuc = findViewById(R.id.addthuchi_spinner_hangmuc);

        // Spinner Loại Tiền > onLick để set data cho Hạng Mục
        spinner_loaitien.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter_loaitien = ArrayAdapter.createFromResource(this,
                R.array.spiner_item, R.layout.custom_text_spinner);
        adapter_loaitien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_loaitien.setAdapter(adapter_loaitien);

        // Date time
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String sDate = "";
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1){
            sDate = "Chủ Nhật" + ": " +
                    calendar.get(Calendar.DAY_OF_MONTH) + "-"
                    + (calendar.get(Calendar.MONTH) + 1)
                    + "-" + calendar.get(Calendar.YEAR);
        }
        else {
            sDate = "Thứ " + calendar.get(Calendar.DAY_OF_WEEK) + ": " +
                    calendar.get(Calendar.DAY_OF_MONTH) + "-"
                    + (calendar.get(Calendar.MONTH) + 1)
                    + "-" + calendar.get(Calendar.YEAR);
        }
        txtdate.setText(sDate);
        // Lưu dateTime nếu người dùng không thay đổi thời gian, nếu không lưu thì thời gian ban đầu sẽ null
        dateTime = day + "-" + (month + 1) + "-" + year;
        // Lưu dữ liệu Dialog
        listen = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearr, int monthh, int dayy) {
                Calendar tmp = Calendar.getInstance();
                tmp.set(yearr, monthh, dayy);
                monthh = monthh + 1;
                if (tmp.get(Calendar.DAY_OF_WEEK) == 1){
                    String datetime = "Chủ Nhật" + ": " +
                            dayy + "-" + monthh + "-" + yearr;
                    txtdate.setText(datetime);
                }
                else {
                    String datetime = "Thứ " + tmp.get(Calendar.DAY_OF_WEEK) + ": " +
                            dayy + "-" + monthh + "-" + yearr;
                    txtdate.setText(datetime);
                }
                // Lấy thời gian
                dateTime = dayy + "-" + monthh + "-" + yearr;
            }
        };

        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datetime = new DatePickerDialog(AddThuChi.this, R.style.Theme_AppCompat_Dialog_MinWidth,
                        listen , year, month, day);
                datetime.getWindow().setBackgroundDrawable(new ColorDrawable(0xC8ED365A));
                datetime.show();
            }
        });
        // Add thu chi
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtTien.getText().toString().equals("")) {
                    Toast.makeText(AddThuChi.this, "Mời nhập số tiền", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Lấy dữ liệu
                    loaiTien = spinner_loaitien.getSelectedItem().toString();
                    hangMuc = spinner_hangmuc.getSelectedItem().toString();
                    thoiGian = txtdate.getText().toString();
                    ghiChu = txtghiChu.getText().toString();
                    Tien = Integer.parseInt(txtTien.getText().toString());
                    // Add
                    ThuChi_Info tmp = new ThuChi_Info(loaiTien, hangMuc, thoiGian, ghiChu, Tien);
                    database.insertThuChi(tmp, dateTime);
                    Toast.makeText(AddThuChi.this, "Thành Công", Toast.LENGTH_SHORT).show();
                    txtTien.setText("");
                    txtghiChu.setText("");
                }
            }
        });

        //Back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Parent_Activity == 1)
                    startActivity(new Intent(AddThuChi.this, MainActivity.class));
                if (Parent_Activity == 2)
                    startActivity(new Intent(AddThuChi.this, All_ThuChi.class));
                else
                    startActivity(new Intent(AddThuChi.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayList<String> tmp = Hangmuc(i);
        ArrayAdapter<String> adapter_hangmuc = new ArrayAdapter(this, R.layout.custom_text_spinner, tmp);
        adapter_hangmuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hangmuc.setAdapter(adapter_hangmuc);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

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
}