package com.example.pufinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView nav_menu;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav_menu = findViewById(R.id.bottom_nav);
        fab = findViewById(R.id.fab);
        display(R.id.menu_home);

        nav_menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            display(item.getItemId());
            return true; }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddThuChi.class));
            }
        });
    }

    void display(int id){
        Fragment fragment = null;
        switch (id){
            case R.id.menu_home:
                fragment = new HomeFragment();
                break;
            case R.id.menu_vitien:
                fragment = new WalletFragment();
                break;
            case R.id.menu_account:
                fragment = new AccountFragment();
                break;
            case R.id.menu_analys:
                fragment = new ThongKeFragment();
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

}