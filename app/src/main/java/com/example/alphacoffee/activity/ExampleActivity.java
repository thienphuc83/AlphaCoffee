package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.CuaHang;
import com.example.alphacoffee.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ExampleActivity extends AppCompatActivity {
    //    TRANG EXAMPLE NÀY DÙNG ĐỂ TEST NHA QUÍ DỊ

    EditText edtTen,
            edtDiaChi,edtSDT,edtGio,edtHinh;
    Button btnThem,btnHuy;
    DatabaseReference mdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        edtTen = findViewById(R.id.edttenSP);
        edtGio = findViewById(R.id.edtGio);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtHinh = findViewById(R.id.edttenHinh);

        btnThem = findViewById(R.id.btnThem);
        btnHuy = findViewById(R.id.btnHuy);

        mdata = FirebaseDatabase.getInstance().getReference();


        // thêm pass để tạo tài khoản nhân viên
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = edtTen.getText().toString().trim();
                mdata.child("PassTaoNhanVien").setValue(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ExampleActivity.this, "Đổi pass thành công!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


//        btnThem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String ten = edtTen.getText().toString();
//                String hinh = edtHinh.getText().toString();
//                String gio = edtGio.getText().toString();
//                String diachi = edtDiaChi.getText().toString();
//                String sdt = edtSDT.getText().toString();
//
//                String cuahangId = mdata.child("CuaHang").push().getKey();
//
//                CuaHang cuaHang = new CuaHang(cuahangId,ten,sdt,diachi,gio,hinh);
//                mdata.child("CuaHang").child(cuahangId).setValue(cuaHang).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(ExampleActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                edtTen.setText("");
//                edtGio.setText("");
//                edtSDT.setText("");
//                edtDiaChi.setText("");
//                edtHinh.setText("");
                mdata.child("PassTaoNhanVien").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Toast.makeText(ExampleActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }
}