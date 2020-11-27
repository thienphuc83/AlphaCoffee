package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.CuaHang;
import com.example.alphacoffee.model.SanPham;
import com.example.alphacoffee.model.TinTuc;
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

    EditText edtTen,edtMota,edtHinh;
    Button btnThem,btnHuy;
    DatabaseReference mdata;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        edtTen = findViewById(R.id.edtTen);
        edtHinh = findViewById(R.id.edtHinh);
        edtMota = findViewById(R.id.edtMoTa);
        tv = findViewById(R.id.tv);

        btnThem = findViewById(R.id.btnThem);
        btnHuy = findViewById(R.id.btnHuy);

        mdata = FirebaseDatabase.getInstance().getReference();


        // thêm pass để tạo tài khoản nhân viên
//        btnThem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // thêm pass để tạo tài khoản nhân viên
////                String pass = edtTen.getText().toString().trim();
////                mdata.child("PassTaoNhanVien").setValue(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        Toast.makeText(ExampleActivity.this, "Đổi pass thành công!", Toast.LENGTH_SHORT).show();
////                    }
////                });
//
//            }
//        });


        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTen.getText().toString();
                String hinh = edtHinh.getText().toString();
                String mota = edtMota.getText().toString();


                String tintucId = mdata.child("TinTuc").push().getKey();

                TinTuc tinTuc = new TinTuc(tintucId,ten,hinh,mota);
                mdata.child("TinTuc").child(tintucId).setValue(tinTuc).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ExampleActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTen.setText("");
                edtMota.setText("");
                edtHinh.setText("");
//                mdata.child("PassTaoNhanVien").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Toast.makeText(ExampleActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        });





    }
}