package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.alphacoffee.R;

public class RegisterActivity extends AppCompatActivity {
    public Button btBack, btForgot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btBack=findViewById(R.id.btback);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btForgot=findViewById(R.id.btforgot);
        btForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotActivity();
            }
        });
    }
    public void ForgotActivity(){
        Intent intent=new Intent(this,ForgotActivity.class);
        startActivity(intent);
    }
}