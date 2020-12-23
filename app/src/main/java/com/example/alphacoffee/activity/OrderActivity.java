package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.SanPhamOrderAdapter;
import com.example.alphacoffee.model.Bill;
import com.example.alphacoffee.model.SanPhamOrder;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import info.hoang8f.widget.FButton;

public class OrderActivity extends AppCompatActivity {

    private ImageView imgBackOrder;
    private TextView tvTitleOrder, tvTichDiemOrder, tvSTTOrder, tvTenNVOrder, tvTenCHOrder,
            tvNgayTaoOrder, tvKieuNhanOrder, tvLoaiThanhToanOrder,
            tvTrangThaiOrder, tvTenKHOrder, tvGhiChuOrder, tvTongTienOrder;
    private RecyclerView rvSPOOrder;
    private LinearLayout layoutNhanOrder;

    private SanPhamOrderAdapter sanPhamOrderAdapter;
    private ArrayList<SanPhamOrder> mangspoorder;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference mData;
    DatabaseReference databaseReference;

    Bill bill;
    User user;

    String STT = "";
    String tenNV = "";
    String idNV = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        // lấy id và name của nhanvien
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                assert user != null;

                tenNV = user.getName();
                idNV = user.getUserId();
                tvTenNVOrder.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        GetIntent();

        GetDataSanPhamOrder();

//        LaySTTChoOrder();

        //nhận order
        layoutNhanOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                int min = 0;
                int max = 10000;
                int random = ThreadLocalRandom.current().nextInt(min, max);
                tvSTTOrder.setText(STT);
                STT = String.valueOf(random);


                // insert thêm stt, idnv, tennv, trạng thái cho bill
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("soThuTu", STT);
                hashMap.put("idNV", user.getUserId());
                hashMap.put("tenNV", user.getName());
                hashMap.put("trangThai", "Đang xử lý");

                mData.child("Bill").child(bill.getIdBill()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OrderActivity.this, "Đã nhận order!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OrderActivity.this, OrderHistoryActivity.class));
                            finish();
                        }
                    }
                });

            }
        });

    }

//    private void LaySTTChoOrder() {
//        tvSTTOrder.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View v) {
////                // khởi tạo dialog
////                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
////                builder.setCancelable(false);
////                //set layout cho dialog
////                View view = LayoutInflater.from(OrderActivity.this).inflate(R.layout.dialog_stt_order, null);
////                ImageView imgClose = view.findViewById(R.id.imgclosesttorder);
////                final EditText edtSTT = view.findViewById(R.id.edtsttorder);
////                Button btnXong = view.findViewById(R.id.btnsttorder);
////
////                //mở dialog
////                builder.setView(view);
////                final AlertDialog alertDialog = builder.create();
////                alertDialog.show();
////
////                imgClose.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        alertDialog.dismiss();
////                    }
////                });
////
////                btnXong.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        STT = edtSTT.getText().toString().trim();
////                        tvSTTOrder.setText(STT);
////                        alertDialog.dismiss();
////                    }
////                });
//
//
//            }
//        });
//    }

    private void AnhXa() {
        imgBackOrder = findViewById(R.id.imgbackoder);
        tvTitleOrder = findViewById(R.id.tvorder);
        tvTichDiemOrder = findViewById(R.id.tvtichdiemorder);
        tvSTTOrder = findViewById(R.id.tvsttorder);
        tvTenNVOrder = findViewById(R.id.tvtennhanvienorder);
        tvTenCHOrder = findViewById(R.id.tvtencuahangorder);
        tvNgayTaoOrder = findViewById(R.id.tvngaytaoorder);
        tvKieuNhanOrder = findViewById(R.id.tvkieunhanorder);
        tvLoaiThanhToanOrder = findViewById(R.id.tvkieuthanhtoanorder);
        tvTrangThaiOrder = findViewById(R.id.tvtrangthaiorder);
        tvTenKHOrder = findViewById(R.id.tvtenkhachhangorder);
        tvGhiChuOrder = findViewById(R.id.tvghichuorder);
        tvTongTienOrder = findViewById(R.id.tvtongtienorder);
        rvSPOOrder = findViewById(R.id.rvspoorder);
        layoutNhanOrder = findViewById(R.id.layoutnhanorder);

        imgBackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvTitleOrder.setTypeface(typeface);

        mangspoorder = new ArrayList<>();
        sanPhamOrderAdapter = new SanPhamOrderAdapter(this, mangspoorder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvSPOOrder.setLayoutManager(layoutManager);
        rvSPOOrder.setAdapter(sanPhamOrderAdapter);

    }

    private void GetIntent() {
        bill = (Bill) getIntent().getSerializableExtra("order");
//        Toast.makeText(this, bill.getTenCH(), Toast.LENGTH_SHORT).show();

        tvTichDiemOrder.setText(bill.getTichDiem());
        tvTenCHOrder.setText(bill.getTenCH());
        tvNgayTaoOrder.setText(bill.getNgayTao());
        tvKieuNhanOrder.setText(bill.getDiaDiem());
        tvLoaiThanhToanOrder.setText(bill.getLoaiThanhToan());
        tvTrangThaiOrder.setText("Đang nhận order");
        tvGhiChuOrder.setText(bill.getGhiChu());
        tvTenKHOrder.setText(bill.getTenKH());


        // custom giá
        long tongtien = bill.getTongtien();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTongTienOrder.setText(decimalFormat.format(tongtien) + " đ");


    }

    private void GetDataSanPhamOrder() {
        mData.child("SanPhamOrder").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPhamOrder sanPhamOrder = snapshot.getValue(SanPhamOrder.class);

                String idhdspo = sanPhamOrder.getIdBill();
                String idhd = bill.getIdBill();
                if (idhdspo.equals(idhd)) {
                    mangspoorder.add(new SanPhamOrder(
                            sanPhamOrder.getId(),
                            sanPhamOrder.getName(),
                            sanPhamOrder.getPrice(),
                            sanPhamOrder.getSize(),
                            sanPhamOrder.getTopping(),
                            sanPhamOrder.getImageURL(),
                            sanPhamOrder.getSoluong(),
                            sanPhamOrder.getIdBill()));
                    sanPhamOrderAdapter.notifyDataSetChanged();
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

}