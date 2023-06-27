package com.example.pufinance;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class ThongKeFragment extends Fragment {
    Spinner spinner;
    PieChart pieChart;
    Database database;
    TextView timeFrom, timeTo;
    DatePickerDialog.OnDateSetListener listenFrom, listenTo;
    String datetime_From = "", datetime_To = "";
    LinearLayout khac;

    public ThongKeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thong_ke, container, false);
        database = new Database(getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.thongke_spinner);
        timeFrom = view.findViewById(R.id.thongke_txt_from);
        timeTo = view.findViewById(R.id.thongke_txt_to);
        khac = view.findViewById(R.id.linearLayout_thongke_khac);
        khac.setVisibility(LinearLayout.GONE);
        // Setup PieChart
        pieChart = view.findViewById(R.id.pieChart);
        PieChart(0);
        // Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), R.layout.custom_text_spinner_thongke, thoiGian());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // onClick Spinner >> set data PieChart
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PieChart(i);
                if (i == 7) {
                    timeFrom.setText(null);
                    timeTo.setText(null);
                    datetime_From = ""; datetime_To = "";
                    khac.setVisibility(LinearLayout.VISIBLE);
                }
                else
                    khac.setVisibility(LinearLayout.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // Custom DateTime
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
                DatePickerDialog datetime = new DatePickerDialog(getContext(), R.style.Theme_AppCompat_Dialog_MinWidth,
                        listenFrom , year, month, day);
                datetime.getWindow().setBackgroundDrawable(new ColorDrawable(0xC8ED365A));
                datetime.show();
            }
        });
        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datetime = new DatePickerDialog(getContext(), R.style.Theme_AppCompat_Dialog_MinWidth,
                        listenTo , year, month, day);
                datetime.getWindow().setBackgroundDrawable(new ColorDrawable(0xC8ED365A));
                datetime.show();
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

    public void PieChart (int i) {
        int thu = 0;
        int chi = 0;
        switch (i) {
            case 0:
                thu = LinhTinh.GetThu_All(database);
                chi = LinhTinh.GetChi_All(database);
                LoadPieChart(thu, chi);
                break;
            case 1:
                thu = LinhTinh.Thu_HomNay(database);
                chi = LinhTinh.Chi_HomNay(database);
                LoadPieChart(thu, chi);
                break;
            case 2:
                thu = LinhTinh.Thu_HomQua(database);
                chi = LinhTinh.Chi_HomQua(database);
                LoadPieChart(thu, chi);
                break;
            case 3:
                thu = LinhTinh.Thu_TuanNay(database);
                chi = LinhTinh.Chi_TuanNay(database);
                LoadPieChart(thu, chi);
                break;
            case 4:
                thu = LinhTinh.Thu_TuanTruoc(database);
                chi = LinhTinh.Chi_TuanTruoc(database);
                LoadPieChart(thu, chi);
                break;
            case 5:
                thu = LinhTinh.Thu_ThangNay(database);
                chi = LinhTinh.Chi_ThangNay(database);
                LoadPieChart(thu, chi);
                break;
            case 6:
                thu = LinhTinh.Thu_ThangTruoc(database);
                chi = LinhTinh.Chi_ThangTruoc(database);
                LoadPieChart(thu, chi);
                break;
        }
    }

    public void LoadPieChart (int thu, int chi) {
        ArrayList<PieEntry> Thu_Chi = new ArrayList<>();
        Thu_Chi.add( new PieEntry(thu, "Thu"));
        Thu_Chi.add( new PieEntry(chi, "Chi"));

        PieDataSet pieDataSet = new PieDataSet(Thu_Chi, "");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setSliceSpace(3);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Thu Chi");
        pieChart.setCenterTextSize(25);
        pieChart.animate();
        // Các ô thông tin cho biểu đồ
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(20);
        legend.setFormSize(20);
        legend.setFormToTextSpace(5);
        // Reload PieChart
        pieChart.invalidate();
    }

    public void Filter_DateTime() {
        if (datetime_From == "" || datetime_To == "")
            return;
        else {
            int thu = LinhTinh.Thu_DateTimeCusTom(database, datetime_From, datetime_To);
            int chi = LinhTinh.Chi_DateTimeCusTom(database, datetime_From, datetime_To);
            LoadPieChart(thu, chi);
        }
    }
}