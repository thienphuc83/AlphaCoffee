package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.TinTucAdapter;
import com.example.alphacoffee.adapter.TinTucManagerAdapter;
import com.example.alphacoffee.model.TinTuc;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManagerTinTucActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView imgBack, imgThem;
    private RecyclerView rvTinTuc;

    private TinTucManagerAdapter tinTucAdapter;
    private ArrayList<TinTuc> mangtintuc;
    DatabaseReference mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_tin_tuc);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        GetDataTinTuc();

        //cập nhật lại nếu có xóa hoặc thay đổi tintuc
        mData.child("TinTuc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GetDataTinTuc();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                GetDataTinTuc();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetDataTinTuc() {
        mangtintuc.clear();
        //lay tintuc
        mData.child("TinTuc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TinTuc tinTuc = snapshot.getValue(TinTuc.class);

                mangtintuc.add(new TinTuc(
                        tinTuc.getTinTucId(),
                        tinTuc.getTenTinTuc(),
                        tinTuc.getNoiDung(),
                        tinTuc.getHinhAnh()));
                tinTucAdapter.notifyDataSetChanged();


//
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
        tvTitle = findViewById(R.id.tvtintucmanager);
        imgBack = findViewById(R.id.imgbacktintucmanager);
        imgThem = findViewById(R.id.imgthemtintucmanager);
        rvTinTuc = findViewById(R.id.rvtintucmanager);


        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvTitle.setTypeface(typeface);

        //load data tintuc
        mangtintuc = new ArrayList<>();
        tinTucAdapter = new TinTucManagerAdapter(this, mangtintuc);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvTinTuc.setLayoutManager(new GridLayoutManager(this,2));
        rvTinTuc.setAdapter(tinTucAdapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerTinTucActivity.this, ThemTinTucActivity.class));
            }
        });

    }
}