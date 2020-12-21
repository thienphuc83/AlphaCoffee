package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.MenuAdapter;
import com.example.alphacoffee.model.SanPham;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class ManagerMenuActivity extends AppCompatActivity {

    private TextView tvTitle;
    private RecyclerView rvDoAn, rvDoUong;
    private ImageView imgBack, imgThem;
    private MenuAdapter doanAdapter, douongAdapter;
    private ArrayList<SanPham> mangdoan, mangdouong;

    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_menu);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        GetDataMenu();

        mData.child("SanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GetDataMenu();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                GetDataMenu();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void GetDataMenu() {
        mangdoan.clear();
        mangdouong.clear();

        mData.child("SanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
//                Log.d("AAA", sanPham.getName());
                assert sanPham != null;
                String loaisanpham = sanPham.getType();
                if (loaisanpham.equals("Đồ ăn")) {
                    mangdoan.add(new SanPham(
                            sanPham.getProductId(),
                            sanPham.getName(),
                            sanPham.getPriceL(),
                            sanPham.getPriceM(),
                            sanPham.getPriceS(),
                            sanPham.getNote(),
                            sanPham.getTopping(),
                            sanPham.getPriceTopping(),
                            sanPham.getLike(),
                            sanPham.getType(),
                            sanPham.getImageURL()));
                    doanAdapter.notifyDataSetChanged();
                }

                if (loaisanpham.equals("Đồ uống")) {
                    mangdouong.add(new SanPham(
                            sanPham.getProductId(),
                            sanPham.getName(),
                            sanPham.getPriceL(),
                            sanPham.getPriceM(),
                            sanPham.getPriceS(),
                            sanPham.getNote(),
                            sanPham.getTopping(),
                            sanPham.getPriceTopping(),
                            sanPham.getLike(),
                            sanPham.getType(),
                            sanPham.getImageURL()));
                    Collections.reverse(mangdouong);
                    douongAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
                    previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String
                    previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void AnhXa() {
        tvTitle = findViewById(R.id.tvmenumanager);
        imgBack = findViewById(R.id.imgbackmenumanager);
        imgThem = findViewById(R.id.imgthemmenumanager);
        rvDoAn = findViewById(R.id.rvdoan);
        rvDoUong = findViewById(R.id.rvdouong);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvTitle.setTypeface(typeface);

        mangdoan = new ArrayList<>();
        doanAdapter = new MenuAdapter(this, mangdoan);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvDoAn.setLayoutManager(linearLayoutManager);
        rvDoAn.setAdapter(doanAdapter);

        mangdouong = new ArrayList<>();
        Collections.reverse(mangdouong);
        douongAdapter = new MenuAdapter(this, mangdouong);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvDoUong.setLayoutManager(gridLayoutManager);
        rvDoUong.setAdapter(douongAdapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManagerMenuActivity.this, ThemMenuActivity.class));
            }
        });
    }
}