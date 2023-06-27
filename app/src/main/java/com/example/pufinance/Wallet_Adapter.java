package com.example.pufinance;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

public class Wallet_Adapter extends RecyclerView.Adapter<Wallet_Adapter.Wallet_Swipe> {
    Context context;
    ArrayList<Wallet_Info> arrayList;
    Database database;
    onClick_Wallet onClickWallet;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public Wallet_Adapter(Context context, ArrayList<Wallet_Info> arrayList, Database database, onClick_Wallet onClick_wallet) {
        this.context = context;
        this.arrayList = arrayList;
        this.database = database;
        this.onClickWallet = onClick_wallet;
    }

    @NonNull
    @Override
    public Wallet_Swipe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_layout_item_wallet,parent,false);
        return new Wallet_Swipe(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Wallet_Swipe holder, int position) {
        Wallet_Info wallet_info = arrayList.get(position);
        if (wallet_info == null)
            return;
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(wallet_info.ID));
        // Set Data
        holder.tenVi.setText(wallet_info.tenVi);
        holder.tongTien.setText(LinhTinh.SplitNumber(wallet_info.tongTien));
        // OnClick Swipe
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickWallet.onClick(wallet_info, 1);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickWallet.onClick(wallet_info, 0);
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadWallet(wallet_info, context);
            }
        });
    }

    @Override
    public int getItemCount() {
        if ( arrayList != null)
            return arrayList.size();
        return 0;
    }

    public class Wallet_Swipe extends RecyclerView.ViewHolder {
        TextView tenVi, tongTien, del, edit;
        SwipeRevealLayout swipeRevealLayout;
        LinearLayout linearLayout;

        public Wallet_Swipe(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.Swipe_item_wallet);
            tenVi = itemView.findViewById(R.id.tenVi);
            tongTien = itemView.findViewById(R.id.tongTien);
            linearLayout = itemView.findViewById(R.id.onclick_Wallet);

            del = itemView.findViewById(R.id.delete_wallet);
            edit = itemView.findViewById(R.id.edit_wallet);
        }
    }

    private void LoadWallet(Wallet_Info wallet_info, Context context) {
        // Setup Dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wallet_info);
        Window window = dialog.getWindow();
        if (window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        // Load dữ liệu lên Dialog
        TextView tenVi = dialog.findViewById(R.id.wallet_info_txt_tenvi);
        TextView soTien = dialog.findViewById(R.id.wallet_info_txt_tien);
        TextView ghiChu = dialog.findViewById(R.id.wallet_info_txt_ghichu);
        tenVi.setText(wallet_info.tenVi);
        soTien.setText(LinhTinh.SplitNumber(wallet_info.tongTien));
        ghiChu.setText(wallet_info.ghiChu);
        dialog.show();
    }

    interface onClick_Wallet {
        void onClick(Wallet_Info wallet_info, int key);
    }
}
