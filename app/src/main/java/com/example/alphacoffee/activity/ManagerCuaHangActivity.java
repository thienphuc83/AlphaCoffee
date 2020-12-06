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

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.CuaHangManagerAdapter;
import com.example.alphacoffee.adapter.TinTucManagerAdapter;
import com.example.alphacoffee.model.CuaHang;
import com.example.alphacoffee.model.TinTuc;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManagerCuaHangActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView imgBack, imgThem;
    private RecyclerView rvCuaHang;

    private CuaHangManagerAdapter cuaHangManagerAdapter;
    private ArrayList<CuaHang> mangcuahang;
    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_cua_hang);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        GetDataCuaHang();

        //cập nhật lại nếu có xóa hoặc thay đổi tintuc
        mData.child("CuaHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GetDataCuaHang();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                GetDataCuaHang();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetDataCuaHang() {
        mangcuahang.clear();
        //lay tintuc
        mData.child("CuaHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CuaHang cuaHang = snapshot.getValue(CuaHang.class);

                mangcuahang.add(new CuaHang(
                        cuaHang.getCuaHangId(),
                        cuaHang.getTenCuaHang(),
                        cuaHang.getSoDienThoai(),
                        cuaHang.getDiaChi(),
                        cuaHang.getGioMoCua(),
                        cuaHang.getHinhAnh()));
                cuaHangManagerAdapter.notifyDataSetChanged();
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
        tvTitle = findViewById(R.id.tvcuahangmanager);
        imgBack = findViewById(R.id.imgbackcuahangmanager);
        imgThem = findViewById(R.id.imgthemcuahangmanager);
        rvCuaHang = findViewById(R.id.rvcuahangmanager);


        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvTitle.setTypeface(typeface);

        //load data cuahang
        mangcuahang = new ArrayList<>();
        cuaHangManagerAdapter = new CuaHangManagerAdapter(this, mangcuahang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvCuaHang.setLayoutManager(layoutManager);
        rvCuaHang.setAdapter(cuaHangManagerAdapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerCuaHangActivity.this, ThemCuaHangActivity.class));
            }
        });

    }
}