package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private TextView tvDangNhap, tvQuenMatKhau, tvDangKyNgay;
    private EditText edtEmail,edtPass;
    private Button btnDangNhap;
    private ProgressBar progressBarDangNhap;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AnhXa();

        auth = FirebaseAuth.getInstance();

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();
                if (TextUtils.isEmpty(email)|| TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this, "Điền đủ thông tin!", Toast.LENGTH_SHORT).show();
                }else{
                    DangNhap(email,pass);
                }
            }
        });


    }

    private void DangNhap(String email, String pass) {
        progressBarDangNhap.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void AnhXa() {
        tvDangNhap = findViewById(R.id.tvdangnhap);
        tvDangKyNgay = findViewById(R.id.tvdangkyngay);
        tvQuenMatKhau = findViewById(R.id.tvquamanhinhquenmatkhau);
        edtEmail = findViewById(R.id.edtemaildangnhap);
        edtPass = findViewById(R.id.edtmatkhaudangnhap);
        btnDangNhap = findViewById(R.id.btndangnhap);
        progressBarDangNhap = findViewById(R.id.progressbardangnhap);

        Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        tvDangNhap.setTypeface(typeface);

        tvDangKyNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
            }
        });
    }


}