package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.SanPham;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExampleActivity extends AppCompatActivity {

//    TRANG EXAMPLE NÀY DÙNG ĐỂ TEST NHA QUÍ DỊ
//    DatabaseReference mdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

//        mdata = FirebaseDatabase.getInstance().getReference();
//
//        SanPham sanPham = new SanPham(
//                "Trà ôlong vãi",
//                "39000",
//                "Là thức uống 'bắt vị' được lấy cảm hứng từ trái Vải - thức quả tròn đầy, quen thuộc trong cuộc sống người Việt - kết hợp cùng Trà Oolong làm từ những lá trà tươi hảo hạng.",
//                "Vừa",
//                "default",
//                "7",
//                "Đồ uống",
//                "https://firebasestorage.googleapis.com/v0/b/alphacoffee-c5245.appspot.com/o/Tr%C3%A0%20oolong%20v%C3%A3i.jpg?alt=media&token=e5a7ef82-5905-4f50-8887-d84c2b024ea3");
//
//        mdata.child("SanPham").push().setValue(sanPham);
//

    }
}