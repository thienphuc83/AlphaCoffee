package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.SanPhamOrderAdapter;
import com.example.alphacoffee.fragment.FragmentBillAll;
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

public class BillDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDiemBillDetail, tvSTTBillDetail, tvTenKHBillDetail,
            tvTenCHBillDetail, tvNgayTaoBillDetail, tvKieuNhanBillDetail, tvKieuThanhToanBillDetail,
            tvTrangThaiBillDetail, tvHuyDonBillDetail, tvTenNVBillDetail, tvGhiChuBillDetail, tvTongTienBillDetail;
    private ImageView imgBack;
    private RecyclerView rvListSPOrder;

    SanPhamOrderAdapter sanPhamOrderAdapter;
    ArrayList<SanPhamOrder> mangsporder;

    DatabaseReference mData;

    Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        GetInTent();

        GetDataSanPhamOrder();

        // hủy đơn
        tvHuyDonBillDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //khoi tao dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(BillDetailActivity.this);
                builder.setCancelable(false); // bấm ngoài vùng
                //set layout cho dialog
                View view = LayoutInflater.from(BillDetailActivity.this).inflate(R.layout.dialog_huydon_billdetail, null);
                TextView tvCoHuy = view.findViewById(R.id.tvcohuydon);
                TextView tvKhongHuy = view.findViewById(R.id.tvkhonghuydon);

                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                tvKhongHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                tvCoHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mData.child("Bill").child(bill.getIdBill()).child("trangThai").setValue("Hủy đơn").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(BillDetailActivity.this, "Hủy đơn thành công!", Toast.LENGTH_SHORT).show();
                                tvHuyDonBillDetail.setVisibility(View.GONE);
                                tvTrangThaiBillDetail.setText("Đã hủy");
                                tvTrangThaiBillDetail.setTextColor(getResources().getColor(R.color.colorRed));
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
                    mangsporder.add(new SanPhamOrder(
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

    private void GetInTent() {
        bill = (Bill) getIntent().getSerializableExtra("hoadonchitiet");

//        Toast.makeText(this, bill.getTenKH(), Toast.LENGTH_SHORT).show();
//        //set data
        String stt = bill.getSoThuTu();
        String trangthai = bill.getTrangThai();
        String tennhanvien = bill.getTenNV();

        if (stt.equals("default")) {
            tvSTTBillDetail.setText("0");
        } else {
            tvSTTBillDetail.setText(bill.getSoThuTu());
        }

        if (trangthai.equals("default")) {
            tvTrangThaiBillDetail.setText("Chưa xử lý");
            tvHuyDonBillDetail.setVisibility(View.VISIBLE);
        } else if (trangthai.equals("Hủy đơn")) {
            tvTrangThaiBillDetail.setTextColor(getResources().getColor(R.color.colorRed));
            tvTrangThaiBillDetail.setText(bill.getTrangThai());
        } else {
            tvTrangThaiBillDetail.setText(bill.getTrangThai());
        }

        if (tennhanvien.equals("default")) {
            tvTenNVBillDetail.setText("Chưa có");
        } else {
            tvTenNVBillDetail.setText(bill.getTenNV());
        }

        tvDiemBillDetail.setText(bill.getTichDiem());
        tvTenKHBillDetail.setText(bill.getTenKH());
        tvTenCHBillDetail.setText(bill.getTenCH());
        tvNgayTaoBillDetail.setText(bill.getNgayTao());
        tvKieuNhanBillDetail.setText(bill.getDiaDiem());
        tvKieuThanhToanBillDetail.setText(bill.getLoaiThanhToan());
        tvGhiChuBillDetail.setText(bill.getGhiChu());

        // custom giá
        long tongtien = bill.getTongtien();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTongTienBillDetail.setText(decimalFormat.format(tongtien) + " đ");

    }

    private void AnhXa() {
        imgBack = findViewById(R.id.imgbackbilldetail);
        rvListSPOrder = findViewById(R.id.rvsanphamorderbilldetail);
        tvTitle = findViewById(R.id.tvbilldetail);
        tvSTTBillDetail = findViewById(R.id.tvsttbilldetail);
        tvTenKHBillDetail = findViewById(R.id.tvtenkhachhangbilldetail);
        tvTenCHBillDetail = findViewById(R.id.tvtencuahangbilldetail);
        tvNgayTaoBillDetail = findViewById(R.id.tvngaytaobilldetail);
        tvKieuNhanBillDetail = findViewById(R.id.tvkieunhanbilldetail);
        tvKieuThanhToanBillDetail = findViewById(R.id.tvkieuthanhtoanbilldetail);
        tvTrangThaiBillDetail = findViewById(R.id.tvtrangthaibilldetail);
        tvTenNVBillDetail = findViewById(R.id.tvtennhanvienbilldetail);
        tvGhiChuBillDetail = findViewById(R.id.tvghichubilldetail);
        tvDiemBillDetail = findViewById(R.id.tvtichdiembilldetail);
        tvHuyDonBillDetail = findViewById(R.id.tvhuydonbilldetail);
        tvTongTienBillDetail = findViewById(R.id.tvtongtienbilldetail);


        mangsporder = new ArrayList<>();
        sanPhamOrderAdapter = new SanPhamOrderAdapter(this, mangsporder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvListSPOrder.setLayoutManager(layoutManager);
        rvListSPOrder.setAdapter(sanPhamOrderAdapter);

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
}