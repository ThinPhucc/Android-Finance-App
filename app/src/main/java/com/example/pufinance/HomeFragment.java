package com.example.pufinance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment implements ThuChi_Adapter.onCLick_ThuChi{
    RecyclerView recyclerView;
    ThuChi_Adapter thuChi_adapter;
    ArrayList<ThuChi_Info> arrayList;
    TextView sodu, thu, chi, xemtatca;
    Database database;
    DatePickerDialog.OnDateSetListener listen;
    String thoiGian = "";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database = new Database(getContext());
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sodu = view.findViewById(R.id.fmt_home_txt_sodu);
        thu = view.findViewById(R.id.fmt_home_txt_thu);
        chi = view.findViewById(R.id.fmt_home_txt_chi);
        xemtatca = view.findViewById(R.id.fmt_home_txt_loc);
        // Set So du
        int num = database.TongTienWallet();
        sodu.setText(LinhTinh.SplitNumber(num));
        //
        recyclerView = view.findViewById(R.id.fmt_home_lstview_thuchi);
        arrayList = database.get10ThuChi();
        thuChi_adapter = new ThuChi_Adapter(getContext(), arrayList, database, this);
        //RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(thuChi_adapter);
        // Set Thu Chi
        SetThuChi();
        //
        xemtatca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), All_ThuChi.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(ThuChi_Info thuChi_info, int key) {
        if (key == 0) {
            String[] time_split = thuChi_info.thoiGian.split(" ");
            thoiGian = time_split[2];
            // Setup Dialog
            final Dialog dialog = new Dialog(getContext());
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
            ArrayAdapter<CharSequence> adapter_loaitien = ArrayAdapter.createFromResource(getContext(),
                    R.array.spiner_item, R.layout.custom_text_spinner);
            adapter_loaitien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            loaiTien[0].setAdapter(adapter_loaitien);
            loaiTien[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ArrayList<String> tmp = Hangmuc(i);
                    ArrayAdapter<String> adapter_hangmuc = new ArrayAdapter(getContext(), R.layout.custom_text_spinner, tmp);
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
                    DatePickerDialog datetime = new DatePickerDialog(getContext(), R.style.Theme_AppCompat_Dialog_MinWidth,
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
                        Toast.makeText(getContext(), "Mời nhập số tiền", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Thành Công", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    arrayList.clear();
                    arrayList.addAll(database.get10ThuChi());
                    thuChi_adapter.notifyDataSetChanged();
                    SetThuChi();
                }
            });
            dialog.show();
        }
        else {
            LinhTinh.DeleteThuChi(database, thuChi_info.ID);
            arrayList.clear();
            arrayList.addAll(database.get10ThuChi());
            thuChi_adapter.notifyDataSetChanged();
            SetThuChi();
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

    public void SetThuChi() {
        int tienthu = 0, tienchi = 0;
        for (ThuChi_Info thuChi_Info : arrayList) {
            if (thuChi_Info.loaiTien.equals("Thu"))
                tienthu += thuChi_Info.tien;
            else
                tienchi += thuChi_Info.tien;
        }
        thu.setText(LinhTinh.SplitNumber(tienthu));
        chi.setText(LinhTinh.SplitNumber(tienchi));
    }
}