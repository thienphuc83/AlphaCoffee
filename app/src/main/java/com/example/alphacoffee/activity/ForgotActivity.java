package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alphacoffee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotActivity extends AppCompatActivity {

    private TextView tvQuenMatKhau,tvThongBao;
    private ProgressBar progressBarQuenMatKhau;
    private EditText edtEmailQuenMatKhau;
    private Button btnLayMatKhauMoi;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        AnhXa();

        firebaseAuth = FirebaseAuth.getInstance();
        btnLayMatKhauMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarQuenMatKhau.setVisibility(View.VISIBLE);
                firebaseAuth.fetchSignInMethodsForEmail(edtEmailQuenMatKhau.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.getResult().getSignInMethods().isEmpty()){
                            // trả về kết quả là rỗng thì..
                            progressBarQuenMatKhau.setVisibility(View.GONE);
                            tvThongBao.setText("Email này chưa đăng ký, bạn cần tạo tài khoản mới với email này!");
                        }else {
                            //
                            firebaseAuth.sendPasswordResetEmail(edtEmailQuenMatKhau.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBarQuenMatKhau.setVisibility(View.GONE);
                                    if (task.isSuccessful()){
                                        tvThongBao.setText("Một email đặt lại mật khẩu đã được gửi đến email của bạn!");
                                        finish();
                                    }else {
                                        // lỗi
                                        tvThongBao.setText("Lỗi! Vui lòng thử lại");

                                    }
                                }
                            });
                        }
                    }
                });

            }
        });


    }

    private void AnhXa() {
        tvQuenMatKhau = findViewById(R.id.tvquenmatkhau);
        tvThongBao = findViewById(R.id.tvthongbaoquenmatkhau);
        progressBarQuenMatKhau = findViewById(R.id.progressbarquenmatkhau);
        edtEmailQuenMatKhau = findViewById(R.id.edtemailquenmatkhau);
        btnLayMatKhauMoi = findViewById(R.id.btnlaymatkhaumoi);

        //set font tvlogan
        Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        tvQuenMatKhau.setTypeface(typeface);
    }
}