package com.example.pufinance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    TextView home, register;
    Button login;
    EditText txtsdt, txtpasswd;
    ArrayList<Account_Info> listTK;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = new Database(Login.this);
        home = findViewById(R.id.login_txt_home);
        register = findViewById(R.id.login_txt_register);
        login = findViewById(R.id.login_btn_login);
        txtsdt = findViewById(R.id.login_txt_username);
        txtpasswd = findViewById(R.id.login_txt_password);
        // Chuyen Ve Home
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        });
        // Chuyen Dang Ky
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
        // Login Check
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean xetTk = false;
                String sdt = txtsdt.getText().toString();
                String passwd = txtpasswd.getText().toString();
                listTK = database.getALlAccount();
                for (int i = 0; i < listTK.size(); i++) {
                    Account_Info tkx = listTK.get(i);
                    if (tkx.sdt.matches(sdt) && tkx.matKhau.matches(passwd)) {
                        xetTk = true;
                        break;
                    }
                }
                if (xetTk == true) {
                    Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, MainActivity.class));

                } else {
                    Toast.makeText(Login.this, "Tài khoản hoặc mật khẩu không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            String tk = data.getStringExtra("taikhoan");
            String mk = data.getStringExtra("matkhau");
            txtsdt.setText(tk);
            txtpasswd.setText(mk);
        }
    }
}