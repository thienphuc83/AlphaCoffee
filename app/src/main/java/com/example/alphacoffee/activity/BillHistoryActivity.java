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
import com.example.alphacoffee.fragment.FragmentDoAn;
import com.example.alphacoffee.fragment.FragmentDoUong;
import com.example.alphacoffee.fragment.FragmentPhoBien;
import com.google.android.material.tabs.TabLayout;

public class BillHistoryActivity extends AppCompatActivity {


    private TextView tvBillHistory;
    private ImageView imgBackBillHistory;
    private ViewPager viewPagerBillHistory;
    private TabLayout tabLayoutBillHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_history);

        AnhXa();

        Init();

    }

    private void AnhXa() {
        tvBillHistory = findViewById(R.id.tvbillhistory);
        imgBackBillHistory = findViewById(R.id.imgbackbillhistory);
        viewPagerBillHistory = findViewById(R.id.viewpagerbillhistory);
        tabLayoutBillHistory = findViewById(R.id.tablayoutbillhistory);


        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvBillHistory.setTypeface(typeface);

        imgBackBillHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Init() {

        ViewPagerBillHistoryAdapter viewPagerBillHistoryAdapter = new ViewPagerBillHistoryAdapter(getSupportFragmentManager());
        viewPagerBillHistoryAdapter.AddFragment(new FragmentBillAll(),"Tất cả");
        viewPagerBillHistoryAdapter.AddFragment(new FragmentBillDone(),"Hoàn thành");

        viewPagerBillHistory.setAdapter(viewPagerBillHistoryAdapter);
        tabLayoutBillHistory.setupWithViewPager(viewPagerBillHistory);

    }
}