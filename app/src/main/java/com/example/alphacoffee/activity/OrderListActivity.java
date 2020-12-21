package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.BillHistoryAdapter;
import com.example.alphacoffee.adapter.OrderAdapter;
import com.example.alphacoffee.model.Bill;
import com.example.alphacoffee.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {

    private ImageView imgBackOrderList;
    private TextView tvOrderList, tvThongBao;
    private RecyclerView rvOrderList;
    private OrderAdapter orderAdapter;
    private ArrayList<Bill> mangorder;

    DatabaseReference mData;

    Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        LoadData();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        AnhXa();

        LoadData();

    }

    private void AnhXa() {
        tvOrderList = findViewById(R.id.tvorderlist);
        tvThongBao = findViewById(R.id.tvthongbaoorder);
        imgBackOrderList = findViewById(R.id.imgbackorderlist);
        rvOrderList = findViewById(R.id.rvorderlist);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvOrderList.setTypeface(typeface);

        mangorder = new ArrayList<>();
        orderAdapter = new OrderAdapter(this,mangorder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvOrderList.setLayoutManager(layoutManager);
        rvOrderList.setAdapter(orderAdapter);

        imgBackOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void LoadData() {

        mData.child("Bill").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                bill = snapshot.getValue(Bill.class);
//                Log.d("CCC", bill.getTenCH());
                String trangthai = bill.getTrangThai();
                // lấy bill theo idKH
                if (trangthai.equals("default")) {
                    mangorder.add(new Bill(bill.getIdBill(),
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
                    orderAdapter.notifyDataSetChanged();
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

}