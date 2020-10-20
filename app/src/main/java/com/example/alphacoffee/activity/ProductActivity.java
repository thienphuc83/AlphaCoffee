package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.ViewPagerProductAdapter;
import com.example.alphacoffee.fragment.FragmentDoAn;
import com.example.alphacoffee.fragment.FragmentDoUong;
import com.example.alphacoffee.fragment.FragmentPhoBien;
import com.google.android.material.tabs.TabLayout;

public class ProductActivity extends AppCompatActivity {

    private TextView tvMenu;
    private ImageView imgBack, imgShoppingCart;
    private ViewPager viewPagerProduct;
    private TabLayout tabLayoutProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        AnhXa();
        Init();
    }

    private void Init() {

        ViewPagerProductAdapter viewPagerProductAdapter = new ViewPagerProductAdapter(getSupportFragmentManager());
        viewPagerProductAdapter.AddFragment(new FragmentPhoBien(),"Phổ biến");
        viewPagerProductAdapter.AddFragment(new FragmentDoUong(),"Thức uống");
        viewPagerProductAdapter.AddFragment(new FragmentDoAn(), "Đồ ăn");
        viewPagerProduct.setAdapter(viewPagerProductAdapter);
        tabLayoutProduct.setupWithViewPager(viewPagerProduct);

    }

    private void AnhXa() {
        viewPagerProduct = findViewById(R.id.viewpagerproduct);
        tabLayoutProduct= findViewById(R.id.tablayoutproduct);
        tvMenu= findViewById(R.id.tvmenu);
        imgBack= findViewById(R.id.imgbackmenu);
        imgShoppingCart= findViewById(R.id.imgshoppingcartmenu);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvMenu.setTypeface(typeface);

        imgShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}