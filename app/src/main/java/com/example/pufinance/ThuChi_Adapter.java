package com.example.pufinance;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

public class ThuChi_Adapter extends RecyclerView.Adapter<ThuChi_Adapter.ThuChi_Swipe> {
    Context context;
    ArrayList<ThuChi_Info> arrayList;
    Database database;
    onCLick_ThuChi onCLickThuChi;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public ThuChi_Adapter(Context context, ArrayList<ThuChi_Info> arrayList, Database database, onCLick_ThuChi onCLick_thuChi) {
        this.context = context;
        this.arrayList = arrayList;
        this.database = database;
        this.onCLickThuChi = onCLick_thuChi;
    }

    @NonNull
    @Override
    public ThuChi_Swipe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_layout_item_thuchi,parent,false);
        return new ThuChi_Swipe(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThuChi_Swipe holder, int position) {
        ThuChi_Info thuChi = arrayList.get(position);
        if (thuChi == null)
            return;
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(thuChi.ID));
        // Set Data
        holder.loaiTien.setText(thuChi.loaiTien);
        if (thuChi.loaiTien.equals("Thu"))
            holder.loaiTien.setTextColor(Color.parseColor("#318519"));
        else
            holder.loaiTien.setTextColor(Color.parseColor("#E30F0F"));
        holder.hangMuc.setText(thuChi.hangMuc);
        holder.dateTime.setText(thuChi.thoiGian);
        holder.ghiChu.setText(thuChi.ghiChu);
        holder.Tien.setText(LinhTinh.SplitNumber(thuChi.tien));
        // OnClick Swipe
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCLickThuChi.onClick(thuChi, 0);
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCLickThuChi.onClick(thuChi, 1);
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadThuChi(thuChi, context);
            }
        });
    }

    @Override
    public int getItemCount() {
        if ( arrayList != null)
            return arrayList.size();
        return 0;
    }

    public class ThuChi_Swipe extends RecyclerView.ViewHolder {
        TextView loaiTien, hangMuc, dateTime, Tien, ghiChu, del, edit;
        SwipeRevealLayout swipeRevealLayout;
        LinearLayout linearLayout;

        public ThuChi_Swipe(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.Swipe_item_thuchi);
            loaiTien = itemView.findViewById(R.id.loaiTien);
            hangMuc = itemView.findViewById(R.id.hangMuc);
            dateTime = itemView.findViewById(R.id.datetime);
            ghiChu = itemView.findViewById(R.id.ghiChu);
            Tien = itemView.findViewById(R.id.Tien);
            linearLayout = itemView.findViewById(R.id.onclick_ThuChi);

            del = itemView.findViewById(R.id.delete_thuchi);
            edit = itemView.findViewById(R.id.edit_thuchi);
        }
    }

    private void LoadThuChi(ThuChi_Info thuChi_info, Context context) {
        // Setup Dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thuchi_info);
        Window window = dialog.getWindow();
        if (window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        // Load dữ liệu lên Dialog
        TextView loaiTien = dialog.findViewById(R.id.thuchi_info_txt_loaitien);
        TextView soTien = dialog.findViewById(R.id.thuchi_info_txt_tien);
        TextView thoiGian = dialog.findViewById(R.id.thuchi_info_txt_date);
        TextView hangMuc = dialog.findViewById(R.id.thuchi_info__txt_hangmuc);
        TextView ghiChu = dialog.findViewById(R.id.thuchi_info_txt_ghichu);
        loaiTien.setText(thuChi_info.loaiTien);
        soTien.setText(LinhTinh.SplitNumber(thuChi_info.tien));
        thoiGian.setText(thuChi_info.thoiGian);
        hangMuc.setText(thuChi_info.hangMuc);
        ghiChu.setText(thuChi_info.ghiChu);
        dialog.show();
    }

    interface onCLick_ThuChi {
        void onClick(ThuChi_Info thuChi_info, int key);
    }
}
