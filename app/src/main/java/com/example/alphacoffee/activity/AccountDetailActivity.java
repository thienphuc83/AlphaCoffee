package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.User;
import com.squareup.picasso.Picasso;

public class AccountDetailActivity extends Activity {

    private ImageView imgAvt, imgClose;
    private TextView tvTen, tvGioiTinh, tvSDT, tvSinhNhat, tvMoTa, tvEmail;
    private LinearLayout layoutSinhNhat, layoutMoTa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        AnhXa();

        GetIntent();
    }

    private void GetIntent() {
        User user = (User) getIntent().getSerializableExtra("accountdetail");

        tvEmail.setText(user.getEmail());
        tvTen.setText(user.getName());
        tvSDT.setText(user.getPhone());
        tvGioiTinh.setText(user.getGender());

        if (user.getImageURL().equals("default")){
            imgAvt.setImageResource(R.drawable.example);
        }else {
            Picasso.with(this).load(user.getImageURL()).into(imgAvt);
        }

        if (user.getType().equals("Nhân viên")){
            layoutMoTa.setVisibility(View.VISIBLE);
            tvMoTa.setText(user.getNote());
        }else if (user.getType().equals("Khách hàng")){
            layoutSinhNhat.setVisibility(View.VISIBLE);
            tvSinhNhat.setText(user.getBirthday());
        }

    }

    private void AnhXa() {
        imgAvt = findViewById(R.id.imgdialogacount);
        imgClose = findViewById(R.id.imgclosedialogaccount);
        tvEmail = findViewById(R.id.tvemaildialogaccount);
        tvTen = findViewById(R.id.tvtendialogaccount);
        tvGioiTinh = findViewById(R.id.tvgioitinhdialogaccount);
        tvSDT = findViewById(R.id.tvsdtdialogaccount);
        tvMoTa = findViewById(R.id.tvmotadialogaccount);
        tvSinhNhat = findViewById(R.id.tvngaysinhdialogaccount);
        layoutMoTa = findViewById(R.id.layoutmotadialogaccount);
        layoutSinhNhat = findViewById(R.id.layoutsinhnhatdialogaccount);


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}