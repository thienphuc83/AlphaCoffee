package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alphacoffee.R;

public class HelpActivity extends AppCompatActivity {

    private TextView tvGiupDo;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        tvGiupDo = findViewById(R.id.tvgiupdo);
        imgBack = findViewById(R.id.imgbackgiupdo);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvGiupDo.setTypeface(typeface);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}