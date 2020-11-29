package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.KhachHangAdapter;
import com.example.alphacoffee.adapter.NhanVienAdapter;
import com.example.alphacoffee.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminManagerActivity extends AppCompatActivity {

    private ImageView imgBack, imgThem;
    private TextView tvTitle, tvTaoPassNV;
    private EditText edtPasstaoNV;
    private RecyclerView rvNhanVien, rvKhachHang;

    DatabaseReference mData;
    User user;

    NhanVienAdapter nhanVienAdapter;
    KhachHangAdapter khachHangAdapter;
    ArrayList<User> mangnhanvien, mangkhachhang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        PassTaoNV();

        // lấy data của nhân viên và khách hàng
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                user = snapshot.getValue(User.class);
                String loai = user.getType();
                if (loai.equals("Nhân viên")){
                    mangnhanvien.add(new User(
                            user.getUserId(),
                            user.getName(),
                            user.getEmail(),
                            user.getPhone(),
                            user.getGender(),
                            user.getType(),
                            user.getBirthday(),
                            user.getPoint(),
                            user.getNote(),
                            user.getImageURL()));
                    nhanVienAdapter.notifyDataSetChanged();
                }
                if (loai.equals("Khách hàng")){
                    Log.d("AAA", user.getName());
                    mangkhachhang.add(new User(
                            user.getUserId(),
                            user.getName(),
                            user.getEmail(),
                            user.getPhone(),
                            user.getGender(),
                            user.getType(),
                            user.getBirthday(),
                            user.getPoint(),
                            user.getNote(),
                            user.getImageURL()));
                    khachHangAdapter.notifyDataSetChanged();
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

    private void PassTaoNV() {
        // lấy pass cũ show lên
        mData.child("PassTaoNhanVien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                edtPasstaoNV.setText(snapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // tạo pass mới
        tvTaoPassNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passtaonv = edtPasstaoNV.getText().toString().trim();
                mData.child("PassTaoNhanVien").setValue(passtaonv).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AdminManagerActivity.this, "Tạo pass mới thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    private void AnhXa() {
        imgBack = findViewById(R.id.imgbackadminmanager);
        imgThem = findViewById(R.id.imgthemadminmanager);
        tvTitle = findViewById(R.id.tvtitleadminmanager);
        tvTaoPassNV = findViewById(R.id.tvtaopassnvadminmanager);
        edtPasstaoNV = findViewById(R.id.edtpasstaonvadminmanager);
        rvNhanVien = findViewById(R.id.rvnhanvien);
        rvKhachHang = findViewById(R.id.rvkhachhang);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvTitle.setTypeface(typeface);

        //nhân viên
        mangnhanvien = new ArrayList<>();
        nhanVienAdapter = new NhanVienAdapter(this, mangnhanvien);
        LinearLayoutManager linearLayout= new LinearLayoutManager(this);
        linearLayout.setOrientation(RecyclerView.HORIZONTAL);
        rvNhanVien.setLayoutManager(linearLayout);
        rvNhanVien.setAdapter(nhanVienAdapter);

        //khách hàng
        mangkhachhang = new ArrayList<>();
        khachHangAdapter = new KhachHangAdapter(this, mangkhachhang);
        LinearLayoutManager linearLayout1= new LinearLayoutManager(this);
        linearLayout1.setOrientation(RecyclerView.VERTICAL);
        rvKhachHang.setLayoutManager(linearLayout1);
        rvKhachHang.setAdapter(khachHangAdapter);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khởi tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminManagerActivity.this);
                builder.setCancelable(false);
                //set layout cho dialog
                View view = LayoutInflater.from(AdminManagerActivity.this).inflate(R.layout.dialog_manager, null);

                ImageView imgClose = view.findViewById(R.id.imgclosemanager);
                TextView tvTitle = view.findViewById(R.id.tvtitlemanager);
                LinearLayout layoutMenu = view.findViewById(R.id.layoutmanagermenu);
                LinearLayout layoutCuaHang = view.findViewById(R.id.layoutmanagercuahang);
                LinearLayout layoutTinTuc = view.findViewById(R.id.layoutmanagertintuc);
                LinearLayout layoutDonHang = view.findViewById(R.id.layoutmanagerdonhang);

                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                //set font tvlogan
                Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
                tvTitle.setTypeface(typeface);

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                layoutMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AdminManagerActivity.this, ManagerMenuActivity.class ));
                        alertDialog.dismiss();
                    }
                });
                layoutCuaHang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AdminManagerActivity.this, ManagerCuaHangActivity.class ));
                        alertDialog.dismiss();
                    }
                });
                layoutTinTuc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AdminManagerActivity.this, ManagerTinTucActivity.class ));
                        alertDialog.dismiss();
                    }
                });
                layoutDonHang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AdminManagerActivity.this, ManagerDonHangActivity.class ));
                        alertDialog.dismiss();
                    }
                });
            }
        });

    }
}