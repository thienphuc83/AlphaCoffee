package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.ViewPagerProductAdapter;
import com.example.alphacoffee.fragment.FragmentDoAn;
import com.example.alphacoffee.fragment.FragmentDoUong;
import com.example.alphacoffee.fragment.FragmentPhoBien;
import com.example.alphacoffee.model.CuaHang;
import com.example.alphacoffee.model.SanPham;
import com.example.alphacoffee.model.SanPhamOrder;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private TextView tvMenu;
    private ImageView imgBack, imgShoppingCart;
    private ViewPager viewPagerProduct;
    private TabLayout tabLayoutProduct;

    public static ArrayList<SanPhamOrder> mangsanphamorder;
    public static String tencuahang = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        AnhXa();
        Init();

        IntentCuaHang();

    }



    private void IntentCuaHang() {
        // nhận đối tượng cửa hàng từ fragnment cửa hàng
        CuaHang cuaHang = (CuaHang) getIntent().getSerializableExtra("cuahang");
        tencuahang = cuaHang.getTenCuaHang();
//        Toast.makeText(this, cuaHang.getTenCuaHang(), Toast.LENGTH_SHORT).show();

        // nhận lại tên cửa hàng từ bên bill
//        String tenCH = getIntent().getStringExtra("tenCH");
//        tencuahang = tenCH;
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
                startActivity(new Intent(ProductActivity.this,BillActivity.class));
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //cấp phát vùng bộ nhớ cho cái mảng toàn cục
        if (mangsanphamorder != null){

        }else {
            mangsanphamorder = new ArrayList<>();
        }
    }
}