package com.example.pufinance;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddWalletFragment extends Fragment {
    TextView close;
    Button save;
    EditText txtmoney, txtnameWallet, txtghiChu;
    String tenVi, ghiChu;
    int tongTien;
    Database database;

    public AddWalletFragment() {
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
        database = new Database(getContext());
        return inflater.inflate(R.layout.fragment_add_wallet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        close = view.findViewById(R.id.fgm_addwalet_txt_close);
        txtmoney = view.findViewById(R.id.fgm_addwallet_txt_sotien);
        txtnameWallet = view.findViewById(R.id.fgm_addwallet_txt_tenvi);
        txtghiChu = view.findViewById(R.id.addtthuchi_txt_ghichu);
        save = view.findViewById(R.id.fgm_addwallet_btn_luu);
        // Dong Fragmnt
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getParentFragmentManager().beginTransaction();
                trans.replace(R.id.frameLayout, new WalletFragment());
                trans.addToBackStack("");
                trans.commit();
            }
        });
        // Luu Vi
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu cho ví
                tenVi = txtnameWallet.getText().toString();
                tongTien = Integer.parseInt(txtmoney.getText().toString());
                ghiChu = txtghiChu.getText().toString();
                // Add
                Wallet_Info tmp = new Wallet_Info(tongTien, tenVi, ghiChu);
                database.insertWallet(tmp);

                txtmoney.setText("");
                txtnameWallet.setText("");
                Toast.makeText(getContext(), "Thành Công", Toast.LENGTH_SHORT).show();
            }
        });

    }
}