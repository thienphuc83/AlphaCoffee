package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AccountDetailActivity extends AppCompatActivity {

    private ImageView imgAvt, imgClose, imgEdtMoTa;
    private TextView tvTen, tvGioiTinh, tvSDT, tvSinhNhat, tvMoTa, tvEmail;
    private LinearLayout layoutSinhNhat;
    private RelativeLayout layoutMoTa;

    User user;
    DatabaseReference mData;

    String mota = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        GetIntent();

        imgEdtMoTa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khởi tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountDetailActivity.this);
                builder.setCancelable(false);
                //set layout cho dialog
                View view = LayoutInflater.from(AccountDetailActivity.this).inflate(R.layout.dialog_select_motanv, null);
                ImageView imgCloseEditMota = view.findViewById(R.id.imgcloseeditmotanv);
                LinearLayout tvFulltime = view.findViewById(R.id.layoutfulltime);
                LinearLayout tvPasttime = view.findViewById(R.id.layoutpasttime);
                LinearLayout tvOff = view.findViewById(R.id.layoutoff);
                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                imgCloseEditMota.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                tvFulltime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvMoTa.setText("Fulltime");
                        EditMota("Fulltime");
                        alertDialog.dismiss();
                    }
                });
                tvPasttime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvMoTa.setText("Pasttime");
                        EditMota("Pasttime");
                        alertDialog.dismiss();
                    }
                });
                tvOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvMoTa.setText("Nghỉ làm");
                        EditMota("Nghỉ làm");
                        alertDialog.dismiss();
                    }
                });
            }
        });

    }

    private void GetIntent() {
        user = (User) getIntent().getSerializableExtra("accountdetail");

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
        imgEdtMoTa = findViewById(R.id.imgeditmotadialogaccount);
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

    private void EditMota(String motamoi) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("note", motamoi);
        mData.child("Users").child(user.getUserId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AccountDetailActivity.this, "Cập nhật mô tả thành công!", Toast.LENGTH_SHORT).show();
                    imgEdtMoTa.setVisibility(View.GONE);
                }else {
                    Toast.makeText(AccountDetailActivity.this, "Lỗi! Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}