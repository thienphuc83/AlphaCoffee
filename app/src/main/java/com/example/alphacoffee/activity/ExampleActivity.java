package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExampleActivity extends AppCompatActivity {
    //    TRANG EXAMPLE NÀY DÙNG ĐỂ TEST NHA QUÍ DỊ

    EditText edtTen,
            edtGiaL,edtGiaM,edtGiaS,edtHinh,edtGiaTopping,edtTopping,edtType,edtLike,edtMota;
    Button btnThem,btnHuy;
    DatabaseReference mdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        edtTen = findViewById(R.id.edttenSP);
        edtGiaL = findViewById(R.id.edtGiaL);
        edtGiaM = findViewById(R.id.edtGiaM);
        edtGiaS = findViewById(R.id.edtGiaS);
        edtHinh = findViewById(R.id.edttenHinh);
        edtGiaTopping = findViewById(R.id.edtGiaTopping);
        edtLike = findViewById(R.id.edtLike);
        edtTopping = findViewById(R.id.edtTopping);
        edtMota = findViewById(R.id.edtMota);
        edtType = findViewById(R.id.edtType);
        btnThem = findViewById(R.id.btnThem);
        btnHuy = findViewById(R.id.btnHuy);

        mdata = FirebaseDatabase.getInstance().getReference();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTen.getText().toString();
                String hinh = edtHinh.getText().toString();
                String gial = edtGiaL.getText().toString();
                String giam = edtGiaM.getText().toString();
                String gias = edtGiaS.getText().toString();
                String giatop = edtGiaTopping.getText().toString();
                String top = edtTopping.getText().toString();
                String loai = edtType.getText().toString();
                String mota = edtMota.getText().toString();
                String like = edtLike.getText().toString();

                String productId = mdata.child("SanPham").push().getKey();

                SanPham sanPham = new SanPham(productId,ten,gial,giam,gias,mota,top,giatop,like,loai,hinh);
                mdata.child("SanPham").child(productId).setValue(sanPham).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                edtGiaL.setText("");
                edtGiaM.setText("");
                edtGiaS.setText("");
                edtGiaTopping.setText("");
                edtHinh.setText("");
                edtLike.setText("");
                edtMota.setText("");
                edtType.setText("");
                edtTopping.setText("");
            }
        });


    }
}