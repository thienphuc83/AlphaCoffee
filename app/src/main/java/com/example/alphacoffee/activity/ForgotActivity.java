package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.alphacoffee.R;

public class ForgotActivity extends AppCompatActivity {
    public Button btBack,btRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        btBack=findViewById(R.id.btback);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btRegister=findViewById(R.id.btregister);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity();
            }
        });
    }
    public void RegisterActivity(){
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}