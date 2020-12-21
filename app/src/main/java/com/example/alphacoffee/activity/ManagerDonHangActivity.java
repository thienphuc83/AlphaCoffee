package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.DonHangAdapter;
import com.example.alphacoffee.model.Bill;
import com.example.alphacoffee.model.CuaHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class ManagerDonHangActivity extends AppCompatActivity {

    private TextView tvDonHang;
    private ImageView imgBackDonHang;
    private RecyclerView rvDonHang;

    private ArrayList<Bill> mangdonhang;
    private DonHangAdapter donHangAdapter;

    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_don_hang);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        GetData();

        mData.child("Bill").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GetData();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                GetData();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void GetData() {
        mangdonhang.clear();
        //lay tintuc
        mData.child("Bill").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Bill bill = snapshot.getValue(Bill.class);

                if (bill.getTrangThai().equals("default")){
                    mangdonhang.add(new Bill(
                            bill.getIdBill(),
                            bill.getSoThuTu(),
                            bill.getNgayTao(),
                            bill.getTongtien(),
                            bill.getTrangThai(),
                            bill.getDiaDiem(),
                            bill.getGhiChu(),
                            bill.getLoaiThanhToan(),
                            bill.getTichDiem(),
                            bill.getIdKH(),
                            bill.getTenKH(),
                            bill.getTenCH(),
                            bill.getTenNV(),
                            bill.getIdNV()));
                    donHangAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void AnhXa() {
        tvDonHang = findViewById(R.id.tvdonhangmanager);
        imgBackDonHang = findViewById(R.id.imgbackdonhangmanager);
        rvDonHang = findViewById(R.id.rvdonhangmanager);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvDonHang.setTypeface(typeface);

        imgBackDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mangdonhang = new ArrayList<>();
        donHangAdapter = new DonHangAdapter(this, mangdonhang);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvDonHang.setLayoutManager(linearLayoutManager);
        rvDonHang.setAdapter(donHangAdapter);
    }
}