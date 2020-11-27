package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.ViewPagerBillHistoryAdapter;
import com.example.alphacoffee.adapter.ViewPagerProductAdapter;
import com.example.alphacoffee.fragment.FragmentBillAll;
import com.example.alphacoffee.fragment.FragmentBillDone;
import com.example.alphacoffee.fragment.FragmentOrderDangXuLy;
import com.example.alphacoffee.fragment.FragmentOrderDone;
import com.google.android.material.tabs.TabLayout;

public class OrderHistoryActivity extends AppCompatActivity {

    private TextView tvOrderHistory;
    private ImageView imgBackOrderHistory;
    private ViewPager viewPagerOrderHistory;
    private TabLayout tabLayoutOrderHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        AnhXa();

        Init();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        AnhXa();

        Init();
    }

    private void AnhXa() {
        tvOrderHistory = findViewById(R.id.tvorderhistory);
        imgBackOrderHistory = findViewById(R.id.imgbackorderhistory);
        viewPagerOrderHistory = findViewById(R.id.viewpagerorderhistory);
        tabLayoutOrderHistory = findViewById(R.id.tablayoutorderhistory);


        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvOrderHistory.setTypeface(typeface);

        imgBackOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Init() {

        ViewPagerProductAdapter viewPagerProductAdapter = new ViewPagerProductAdapter(getSupportFragmentManager());
        viewPagerProductAdapter.AddFragment(new FragmentOrderDangXuLy(),"Đang xử lý");
        viewPagerProductAdapter.AddFragment(new FragmentOrderDone(),"Hoàn thành");

        viewPagerOrderHistory.setAdapter(viewPagerProductAdapter);
        tabLayoutOrderHistory.setupWithViewPager(viewPagerOrderHistory);
    }
}