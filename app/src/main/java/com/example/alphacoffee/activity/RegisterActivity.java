package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Objects;

import info.hoang8f.widget.FButton;

public class RegisterActivity extends AppCompatActivity {
    private TextView tvDangKy;
    private EditText edtEmailDangKy, edtMatKhauDangKy, edtTenDangKy, edtSDTDangKy;
    private Button btnDangKy;
    private ProgressBar progressBarDangKy;
    private RadioGroup radioGroupDangKy;
    private RadioGroup radioGroupCheck;

    FirebaseAuth auth;
    DatabaseReference databaseReference, mData;

    String passtaonhanvienmoi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AnhXa();

        auth = FirebaseAuth.getInstance();

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("PassTaoNhanVien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                passtaonhanvienmoi = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = edtTenDangKy.getText().toString();
                final String email = edtEmailDangKy.getText().toString();
                final String pass = edtMatKhauDangKy.getText().toString();
                final String phone = edtSDTDangKy.getText().toString();
                int checkedId = radioGroupDangKy.getCheckedRadioButtonId();
                int checkedId1 = radioGroupCheck.getCheckedRadioButtonId();
                RadioButton select_gender = radioGroupDangKy.findViewById(checkedId);
                RadioButton select_nguoidung = radioGroupCheck.findViewById(checkedId1);
                if (select_gender == null) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng chọn giới tính!", Toast.LENGTH_SHORT).show();
                } else if (select_nguoidung == null) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng chọn người dùng!", Toast.LENGTH_SHORT).show();
                } else {
                    final String gender = select_gender.getText().toString();
                    final String nguoidung = select_nguoidung.getText().toString();
                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
                        Toast.makeText(RegisterActivity.this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    } else if (nguoidung.equals("Nhân viên")){

                        // khởi tạo dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setCancelable(false);
                        //set layout cho dialog
                        View view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.dialog_pass_taonhanvien, null);
                        final EditText edtpassss = view.findViewById(R.id.edtpassdangkynhanvien);
                        ImageView imgclose = view.findViewById(R.id.imgclosepassdangky);
                        Button btnkiemtrapass = view.findViewById(R.id.btnpassdangky);

                        //mở dialog
                        builder.setView(view);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        btnkiemtrapass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String passnhapvao = edtpassss.getText().toString().trim();
                                if (passnhapvao.isEmpty()){
                                    Toast.makeText(RegisterActivity.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
                                }else {
                                    if (passnhapvao.equals(passtaonhanvienmoi)){
                                        DangKy(name, email, pass, phone, gender, nguoidung);
                                        alertDialog.dismiss();
                                    }else {
                                        Toast.makeText(RegisterActivity.this, "Pass sai! Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        imgclose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                    }else {
                        DangKy(name, email, pass, phone, gender, nguoidung);

                    }
                }
            }
        });

    }

    private void DangKy(final String name, final String email, String pass, final String phone, final String gender, final  String nguoidung) {
        progressBarDangKy.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    String userId = user.getUid();

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("userId", userId);
                    hashMap.put("name", name);
                    hashMap.put("email", email);
                    hashMap.put("phone", phone);
                    hashMap.put("gender", gender);
                    hashMap.put("type", nguoidung);
                    hashMap.put("birthday", "default");
                    hashMap.put("point", "default");
                    hashMap.put("note", "default");
                    hashMap.put("imageURL", "default");

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBarDangKy.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                // xóa hết ngăn sếp, chặn quay lại activity trước đó.
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    progressBarDangKy.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void AnhXa() {
        tvDangKy = findViewById(R.id.tvdangky);
        edtEmailDangKy = findViewById(R.id.edtemaildangky);
        edtMatKhauDangKy = findViewById(R.id.edtmatkhaudangky);
        edtSDTDangKy = findViewById(R.id.edtsdtdangky);
        edtTenDangKy = findViewById(R.id.edttendangky);
        btnDangKy = findViewById(R.id.btndangky);
        progressBarDangKy = findViewById(R.id.progressbardangky);
        radioGroupDangKy = findViewById(R.id.radiobuttondangky);
        radioGroupCheck = findViewById(R.id.radioCheck);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvDangKy.setTypeface(typeface);

    }

}