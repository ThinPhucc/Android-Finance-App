package com.example.pufinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewOnReceiveContentListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.DayOfWeek;
import java.util.ArrayList;

public class Register extends AppCompatActivity {
    TextView backTo;
    EditText txtHo, txtTen, txtSdt, txtPasswd, txtRetype_passwd;
    CheckBox accept;
    Button register;
    Database database;
    ArrayList<Account_Info> listTk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FindID();
        database = new Database(Register.this);
        // Back Login
        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
        // Check Register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ho = txtHo.getText().toString();
                String ten = txtTen.getText().toString();
                String sdt = txtSdt.getText().toString();
                String pwd = txtPasswd.getText().toString();
                String retype_pwd = txtRetype_passwd.getText().toString();

                boolean xetTk = true;
                listTk = database.getALlAccount();
                for (int i = 0; i < listTk.size(); i++) {
                    Account_Info tkx = listTk.get(i);
                    if (tkx.sdt.matches(sdt)) {
                        xetTk = false;
                        break;
                    }
                }
                if (xetTk == false) {
                    txtSdt.setError("Số điện thoại đã tồn tại");
                }
                if (ho.isEmpty()) {
                    txtHo.setError("Không được để trống");
                } else {
                    if (ten.isEmpty()) {
                        txtTen.setError("Không được để trống");
                    } else {
                        if (sdt.length() != 9) {
                            txtSdt.setError("Sai định dạng");
                        } else {
                            if (pwd.isEmpty()) {
                                txtPasswd.setError("Không được để trống");
                            } else {
                                if (retype_pwd.isEmpty()) {
                                    txtRetype_passwd.setError("Không được để trống");
                                } else {
                                    if (!retype_pwd.matches(pwd)) {
                                        txtRetype_passwd.setError("Mật khẩu không khớp!");
                                    }
                                    else {
                                        // Add Tai Khoan
                                        Account_Info account_info = new Account_Info(sdt, pwd);
                                        if (xetTk) {
                                            database.insertAccount(account_info);
                                            Toast.makeText(Register.this, "Thành công", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent();
                                            i.putExtra("taikhoan", sdt);
                                            i.putExtra("matkhau", pwd);
                                            setResult(RESULT_OK, i);
                                            finish();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void FindID() {
        backTo = findViewById(R.id.register_plaintxt_back);
        txtHo = findViewById(R.id.register_txt_ho);
        txtTen = findViewById(R.id.register_txt_ten);
        txtSdt = findViewById(R.id.register_txt_phonenumber);
        txtPasswd = findViewById(R.id.register_txt_passwd);
        txtRetype_passwd = findViewById(R.id.register_txt_retypepasswd);
        accept = findViewById(R.id.register_checkbox_accept);
        register = findViewById(R.id.register_btn_register);
    }
}