package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.TinTuc;
import com.squareup.picasso.Picasso;

public class NewspaperDetailActivity extends AppCompatActivity {

    private ImageView imgClose, imgTinTuc;
    private TextView tvTitle, tvTenTinTuc, tvNoiDung;
    TinTuc tinTuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspaper_detail);

        AnhXa();

        GetIntentData();


    }

    private void GetIntentData() {
        tinTuc = (TinTuc) getIntent().getSerializableExtra("tintucchitiet");
        tvTenTinTuc.setText(tinTuc.getTenTinTuc());
        tvNoiDung.setText(tinTuc.getNoiDung());

        Picasso.with(this).load(tinTuc.getHinhAnh()).into(imgTinTuc);
    }

    private void AnhXa() {
        imgClose = findViewById(R.id.imgclosetintucdetail);
        imgTinTuc = findViewById(R.id.imgtintucdetail);
        tvNoiDung = findViewById(R.id.tvnoidungtintucdetail);
        tvTenTinTuc = findViewById(R.id.tvtentintucdetail);
        tvTitle = findViewById(R.id.tvtintucdetail);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvTitle.setTypeface(typeface);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}