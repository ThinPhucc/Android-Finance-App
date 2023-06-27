package com.example.pufinance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AccountFragment extends Fragment {
    public AccountFragment() {
        // Required empty public constructor
    }
    ListView listView;
    Adapter_Cardview adapter_cardview;
    String[] arraylist = {"Profile","Đổi Mật Khẩu","Cài Đặt Chung","Về Chúng Tôi","Xoá Tài Khoản","Đăng Xuất"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter_cardview = new Adapter_Cardview(arraylist, getContext());
        listView = view.findViewById(R.id.fragmnt_account_lstview);
        listView.setAdapter(adapter_cardview);
        Event(listView);
    }

    private class Adapter_Cardview extends BaseAdapter{
        String[] arrayList;
        Context context;

        public Adapter_Cardview(String[] arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return arrayList.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.cardview_account_fragmnt_listview, viewGroup, false);
            TextView txt = view.findViewById(R.id.cardview_account_fragmnt_txt);
            txt.setText(arrayList[i]);
            return view;
        }
    }

    private void Event(ListView listView){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    startActivity(new Intent(AccountFragment.this.getActivity(), InfoAccount.class));
                }
                if(i == 1) {
                    startActivity(new Intent(AccountFragment.this.getActivity(), ChangePass.class));
                }
                if(i == 2) {
                    startActivity(new Intent(AccountFragment.this.getActivity(), InfoAccount.class));
                }
                if(i == 3) {
                    startActivity(new Intent(AccountFragment.this.getActivity(), InfoAccount.class));
                }
                if(i == 4) {
                    startActivity(new Intent(AccountFragment.this.getActivity(), InfoAccount.class));
                }
                if(i == 5) {
                    startActivity(new Intent(AccountFragment.this.getActivity(), Login.class));
                }
            }
        });
    }
}