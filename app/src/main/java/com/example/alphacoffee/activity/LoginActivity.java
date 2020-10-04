package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.alphacoffee.R;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout rellay1, rellay2;
    public Button btLogin,btRegister,btForgot;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 5000); //2000 is the timeout for the splash

        btRegister=findViewById(R.id.btregister);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity();
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
    public void RegisterActivity(){
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    public void ForgotActivity(){
        Intent intent=new Intent(this,ForgotActivity.class);
        startActivity(intent);
    }


}