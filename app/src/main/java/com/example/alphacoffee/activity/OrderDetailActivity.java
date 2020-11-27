package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.SanPhamOrderAdapter;
import com.example.alphacoffee.model.Bill;
import com.example.alphacoffee.model.SanPhamOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDiemOrderDetail, tvSTTOrderDetail, tvTenKHOrderDetail,
            tvTenCHOrderDetail, tvNgayTaoOrderDetail, tvKieuNhanOrderDetail, tvKieuThanhToanOrderDetail,
            tvTrangThaiOrderDetail, tvDaXongOrderDetail, tvTenNVOrderDetail, tvGhiChuOrderDetail, tvTongTienOrderDetail;
    private ImageView imgBack;
    private RecyclerView rvListSPOrder;

    SanPhamOrderAdapter sanPhamOrderDetailAdapter;
    ArrayList<SanPhamOrder> mangsporderdetail;

    DatabaseReference mData;

    Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        GetInTent();

        GetDataSanPhamOrder();

        // cập nhật hóa đơn đã xong
        tvDaXongOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //khoi tao dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setCancelable(false); // bấm ngoài vùng
                //set layout cho dialog
                View view = LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.dialog_daxong_orderdetail, null);
                TextView tvCoCapNhat = view.findViewById(R.id.tvcocapnhat);
                TextView tvKhongCapNhat = view.findViewById(R.id.tvkhongcapnhat);

                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                tvKhongCapNhat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                tvCoCapNhat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mData.child("Bill").child(bill.getIdBill()).child("trangThai").setValue("Hoàn thành").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(OrderDetailActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                tvDaXongOrderDetail.setVisibility(View.GONE);
                                tvTrangThaiOrderDetail.setText("Đã xong");
                                tvTrangThaiOrderDetail.setTextColor(getResources().getColor(R.color.colorGreen));
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

            }
        });
    }

    private void GetDataSanPhamOrder() {
        mData.child("SanPhamOrder").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPhamOrder sanPhamOrder = snapshot.getValue(SanPhamOrder.class);

                String idhdspo = sanPhamOrder.getIdBill();
                String idhd = bill.getIdBill();
                if (idhdspo.equals(idhd)) {
                    mangsporderdetail.add(new SanPhamOrder(
                            sanPhamOrder.getId(),
                            sanPhamOrder.getName(),
                            sanPhamOrder.getPrice(),
                            sanPhamOrder.getSize(),
                            sanPhamOrder.getTopping(),
                            sanPhamOrder.getImageURL(),
                            sanPhamOrder.getSoluong(),
                            sanPhamOrder.getIdBill()));
                    sanPhamOrderDetailAdapter.notifyDataSetChanged();
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

    private void AnhXa() {
        imgBack = findViewById(R.id.imgbackorderdetail);
        rvListSPOrder = findViewById(R.id.rvsanphamorderorderdetail);
        tvTitle = findViewById(R.id.tvorderdetail);
        tvSTTOrderDetail = findViewById(R.id.tvsttorderdetail);
        tvTenKHOrderDetail = findViewById(R.id.tvtenkhachhangorderdetail);
        tvTenCHOrderDetail = findViewById(R.id.tvtencuahangorderdetail);
        tvNgayTaoOrderDetail = findViewById(R.id.tvngaytaoorderdetail);
        tvKieuNhanOrderDetail = findViewById(R.id.tvkieunhanorderdetail);
        tvKieuThanhToanOrderDetail = findViewById(R.id.tvkieuthanhtoanorderdetail);
        tvTrangThaiOrderDetail = findViewById(R.id.tvtrangthaiorderdetail);
        tvTenNVOrderDetail = findViewById(R.id.tvtennhanvienorderdetail);
        tvGhiChuOrderDetail = findViewById(R.id.tvghichuorderdetail);
        tvDiemOrderDetail = findViewById(R.id.tvtichdiemorderdetail);
        tvDaXongOrderDetail = findViewById(R.id.tvdaxongorderdetail);
        tvTongTienOrderDetail = findViewById(R.id.tvtongtienorderdetail);


        mangsporderdetail = new ArrayList<>();
        sanPhamOrderDetailAdapter = new SanPhamOrderAdapter(this, mangsporderdetail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvListSPOrder.setLayoutManager(layoutManager);
        rvListSPOrder.setAdapter(sanPhamOrderDetailAdapter);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvTitle.setTypeface(typeface);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GetInTent() {
        bill = (Bill) getIntent().getSerializableExtra("orderchitiet");

//        Toast.makeText(this, bill.getTenKH(), Toast.LENGTH_SHORT).show();
//        //set data
        String trangthai = bill.getTrangThai();

        if (trangthai.equals("Đang xử lý")) {
            tvTrangThaiOrderDetail.setText(bill.getTrangThai());
            tvTrangThaiOrderDetail.setTextColor(getResources().getColor(R.color.colorBlue));
            tvDaXongOrderDetail.setVisibility(View.VISIBLE);
        }else if (trangthai.equals("Hoàn thành")){
            tvTrangThaiOrderDetail.setTextColor(getResources().getColor(R.color.colorGreen));
            tvTrangThaiOrderDetail.setText(bill.getTrangThai());
        }

        tvDiemOrderDetail.setText(bill.getTichDiem());
        tvTenKHOrderDetail.setText(bill.getTenKH());
        tvTenCHOrderDetail.setText(bill.getTenCH());
        tvNgayTaoOrderDetail.setText(bill.getNgayTao());
        tvKieuNhanOrderDetail.setText(bill.getDiaDiem());
        tvKieuThanhToanOrderDetail.setText(bill.getLoaiThanhToan());
        tvGhiChuOrderDetail.setText(bill.getGhiChu());
        tvTenNVOrderDetail.setText(bill.getTenNV());

        // custom giá
        long tongtien = bill.getTongtien();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTongTienOrderDetail.setText(decimalFormat.format(tongtien) + " đ");

    }
}