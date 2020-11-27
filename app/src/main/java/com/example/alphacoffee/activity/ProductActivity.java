package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
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
import com.example.alphacoffee.model.CuaHang;
import com.example.alphacoffee.model.SanPhamOrder;
import com.example.alphacoffee.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private TextView tvMenu;
    private ImageView imgBack, imgShoppingCart, imgDot;
    private ViewPager viewPagerProduct;
    private TabLayout tabLayoutProduct;

    public static ArrayList<SanPhamOrder> mangsanphamorder;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        AnhXa();

        // set quyền xem giỏ hàng
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                String loainguoidung = user.getType();
                if (loainguoidung.equals("Khách hàng")){
                    imgShoppingCart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Init();

        // khi thoát về cửa hàng và chọn cửa hàng lại vẫn sẽ hiện thông báo khi có sp trong giỏ
        if (mangsanphamorder.size()>0){
            imgDot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //cập nhật lại xem đã có sp nào trong mảng hay chưa
        if (mangsanphamorder.size()>0){
            imgDot.setVisibility(View.VISIBLE);
        }else if (mangsanphamorder.size()==0){
            imgDot.setVisibility(View.GONE);
        }

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
        imgDot= findViewById(R.id.imgdotproduct);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvMenu.setTypeface(typeface);

        imgShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductActivity.this, BillActivity.class));
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ProductActivity.this, HomeActivity.class));
                finish();
            }
        });

        //cấp phát vùng bộ nhớ cho mảng toàn cục
        if (mangsanphamorder != null){

        }else {
            mangsanphamorder = new ArrayList<>();

        }

    }
}