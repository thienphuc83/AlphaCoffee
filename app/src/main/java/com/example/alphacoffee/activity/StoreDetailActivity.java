package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.CommentAdapter;
import com.example.alphacoffee.model.CommentStore;
import com.example.alphacoffee.model.CuaHang;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class StoreDetailActivity extends AppCompatActivity {

    private TextView tvTenStoreDetail, tvGioStoreDetail, tvDiaChiStoreDetail;
    private ImageView imgHinhStoreDetail, imgCloseStoreDetail, imgSendBinhLuanStoreDetail;
    private RecyclerView rvBinhLuanStoreDetail;
    private LinearLayout layoutThemBinhLuanStoreDetail;
    private EditText edtNoiDungBinhLuanStoreDetail;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference mData;
    User user;
    CuaHang cuaHang;

    ArrayList<CommentStore> mangcmt;
    CommentAdapter commentAdapter;

    String idCH = null;
    String noidungcmt = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        //get data cuahang tu adapter
        GetIntent();

        // get data user
        SetQuyenBinhLuan();

        // set ẩn hiện nút send
        edtNoiDungBinhLuanStoreDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // set ẩn hiện nút send
                noidungcmt = edtNoiDungBinhLuanStoreDetail.getText().toString();
                imgSendBinhLuanStoreDetail.setVisibility(View.VISIBLE);
                if (noidungcmt.equals("")){
                    imgSendBinhLuanStoreDetail.setVisibility(View.GONE);
                }
            }
        });

        // insert cmt cuahang
        imgSendBinhLuanStoreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idKH = user.getUserId();
                String tenKH = user.getName();
                String hinhKH = user.getImageURL();
                String noidung = noidungcmt;

                Calendar calendar = new GregorianCalendar();
                String ngaytao = String.valueOf(calendar.getTime());

                idCH = cuaHang.getCuaHangId();

                InsertComment(noidung, ngaytao, tenKH, idKH, hinhKH, idCH);
            }
        });

        // get data cmt theo idCH
        mData.child("NhanXet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CommentStore commentStore = snapshot.getValue(CommentStore.class);
                assert commentStore!=null;

                String idcuahang = commentStore.getIdCH();
                String idcuahangnuane = cuaHang.getCuaHangId();
                if (idcuahang.equals(idcuahangnuane)){
                    mangcmt.add(new CommentStore(
                            commentStore.getIdCmt(),
                            commentStore.getNoiDung(),
                            commentStore.getNgayTao(),
                            commentStore.getTenKH(),
                            commentStore.getIdKH(),
                            commentStore.getHinhKH(),
                            commentStore.getIdCH()));
                    commentAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void InsertComment(String noidung, String ngaytao, String tenKH, String idKH, String hinhKH, String idCH) {
        String idCmt = mData.child("NhanXet").push().getKey();

        CommentStore commentStore = new CommentStore(idCmt,noidung,ngaytao,tenKH,idKH,hinhKH,idCH);
        mData.child("NhanXet").child(idCmt).setValue(commentStore).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(StoreDetailActivity.this, "Nhận xét thành công!", Toast.LENGTH_SHORT).show();
                    edtNoiDungBinhLuanStoreDetail.setText("");
                }
            }
        });


    }

    private void SetQuyenBinhLuan() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                assert user != null;

                //set quyền bình luận
                String loainguoidung= user.getType();
                if (loainguoidung.equals("Khách hàng")){
                    layoutThemBinhLuanStoreDetail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void AnhXa() {
        tvDiaChiStoreDetail = findViewById(R.id.tvdiachistoredetail);
        tvGioStoreDetail = findViewById(R.id.tvgiostoredetail);
        tvTenStoreDetail = findViewById(R.id.tvtenstoredetail);
        imgCloseStoreDetail = findViewById(R.id.imgclosestoredetail);
        imgHinhStoreDetail = findViewById(R.id.imgstoredetail);
        imgSendBinhLuanStoreDetail = findViewById(R.id.imgsendbinhluan);
        rvBinhLuanStoreDetail = findViewById(R.id.rvbinhluanstoredetail);
        layoutThemBinhLuanStoreDetail = findViewById(R.id.layoutbinhluanstoredetail);
        edtNoiDungBinhLuanStoreDetail = findViewById(R.id.edtnoidungbinhluan);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvTenStoreDetail.setTypeface(typeface);
        imgCloseStoreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mangcmt = new ArrayList<>();
        commentAdapter= new CommentAdapter(StoreDetailActivity.this,R.layout.item_comment,mangcmt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(StoreDetailActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvBinhLuanStoreDetail.setLayoutManager(layoutManager);
        rvBinhLuanStoreDetail.setAdapter(commentAdapter);


    }

    private void GetIntent() {
        cuaHang = (CuaHang) getIntent().getSerializableExtra("cuahangchitiet");
//        Toast.makeText(this, cuaHang.getTenCuaHang(), Toast.LENGTH_SHORT).show();
        tvTenStoreDetail.setText(cuaHang.getTenCuaHang());
        tvGioStoreDetail.setText(cuaHang.getGioMoCua());
        tvDiaChiStoreDetail.setText(cuaHang.getDiaChi());
        Picasso.with(this).load(cuaHang.getHinhAnh()).into(imgHinhStoreDetail);

    }

}