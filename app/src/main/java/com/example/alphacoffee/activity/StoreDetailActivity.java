package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.CuaHang;

public class StoreDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);



        GetIntent();
    }

    private void GetIntent() {
        CuaHang cuaHang = (CuaHang) getIntent().getSerializableExtra("cuahangchitiet");
        Toast.makeText(this, cuaHang.getTenCuaHang(), Toast.LENGTH_SHORT).show();

    }


}