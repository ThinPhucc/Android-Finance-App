package com.example.pufinance;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WalletFragment extends Fragment implements Wallet_Adapter.onClick_Wallet{
    TextView tongtien;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    ArrayList<Wallet_Info> arrayList;
    Wallet_Adapter wallet_adapter;
    Database database;

    public WalletFragment() {
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
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tongtien = view.findViewById(R.id.fgm_wallet_txt_tongtien);
        recyclerView = view.findViewById(R.id.fgm_wallet_listview);
        fab = view.findViewById(R.id.fgm_wallet_fab);
        // RecyclerView
        arrayList = database.getAllWallet();
        wallet_adapter = new Wallet_Adapter(getContext(), arrayList, database, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(wallet_adapter);
        // Tong Tien
        tongtien.setText(LinhTinh.SplitNumber(database.TongTienWallet()));
        //Goi dialog
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getParentFragmentManager().beginTransaction();
                trans.replace(R.id.frameLayout, new AddWalletFragment());
                trans.addToBackStack("");
                trans.commit();
            }
        });
    }

    @Override
    public void onClick(Wallet_Info wallet_info, int key) {
        if (key == 0) {
            // Setup Dialog
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_edit_wallet);
            Window window = dialog.getWindow();
            if (window == null)
                return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);
            // Load dữ liệu lên Dialog
            TextView tenVi = dialog.findViewById(R.id.wallet_edit_txt_tenvi);
            EditText soTien = dialog.findViewById(R.id.wallet_edit_txt_sotien);
            EditText ghiChu = dialog.findViewById(R.id.wallet_edit_txt_ghichu);
            tenVi.setText(wallet_info.tenVi);
            soTien.setText(String.valueOf(wallet_info.tongTien));
            ghiChu.setText(wallet_info.ghiChu);
            // Bắt sự kiện huỷ - lưu
            Button huy, luu;
            huy = dialog.findViewById(R.id.wallet_edit_btn_huy);
            luu = dialog.findViewById(R.id.wallet_edit_btn_luu);
            huy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            luu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    database.EditWallet(new Wallet_Info(wallet_info.ID, Integer.parseInt(soTien.getText().toString()), tenVi.getText().toString(), ghiChu.getText().toString()));
                    Toast.makeText(getContext(), "Thành Công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    arrayList.clear();
                    arrayList.addAll(database.getAllWallet());
                    wallet_adapter.notifyDataSetChanged();
                    tongtien.setText(LinhTinh.SplitNumber(database.TongTienWallet()));
                }
            });
            dialog.show();
        }
        else {
            LinhTinh.DeleteWallet(database, wallet_info);
            arrayList.clear();
            arrayList.addAll(database.getAllWallet());
            wallet_adapter.notifyDataSetChanged();
            tongtien.setText(LinhTinh.SplitNumber(database.TongTienWallet()));
        }
    }
}