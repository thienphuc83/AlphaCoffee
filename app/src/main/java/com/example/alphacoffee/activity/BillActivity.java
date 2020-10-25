package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.SanPham;
import com.example.alphacoffee.model.SanPhamOrder;

public class BillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        // nhận dữ liệu sp được kh order.
        SanPhamOrder sp= (SanPhamOrder) getIntent().getSerializableExtra("sanphamorder");
//        Toast.makeText(this, sp.getSoluong()+""+sp.getName()+sp.getSize(), Toast.LENGTH_SHORT).show();

    }
}