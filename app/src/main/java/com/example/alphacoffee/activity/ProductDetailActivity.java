package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.SanPham;
import com.example.alphacoffee.model.SanPhamOrder;
import com.example.alphacoffee.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import info.hoang8f.widget.FButton;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgClose, imgProductDetail, imgLikeProDuctDetail, imgCheckTopping;
    private TextView tvTenProductDetail, tvGiaProductDetail, tvSoLuongproductDetail,
            tvTongTienProductDetail, tvMoTaProductDetail,
            tvGiaSizeLonProductDetail, tvGiaSizeVuaProductDetail, tvGiaSizeNhoProductDetail,
            tvToppingProductDetail, tvGiaToppingProductDetail, tvLayoutTopping;
    private FButton btnTruProductDetail, btnCongProductDetail;
    private LinearLayout layoutTongTien, layoutLikeCongTruSoLuong, layoutToppingProductDetail;
    private RadioGroup radioGroupSizeProDuctDetail;


    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    User user;

    SanPham sanPham;

    int SoLuongSP = 1;
    int SLMoiNhat = 0;
    int GiaSize = 0;
    int GiaTopping = 0;
    int tongtien = 0;
    String Toppingsp = null;
    String Sizesp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        AnhXa();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        //User
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                assert user != null;

                String loainguoidung= user.getType();
                if (loainguoidung.equals("Khách hàng")){
                    layoutTongTien.setVisibility(View.VISIBLE);
                    layoutLikeCongTruSoLuong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Product
        GetProduct();

        //chon số lượng sp
        ChonSoLuong();

        //chọn topping cho sp
        ChonTopping();

        //chon size cho sp
        ChonSize();

        //tính tổng tiền khi thay đổi số lượng, size, thêm bớt topping
        TinhTongTien();

        layoutTongTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gửi dữ liệu của sp đc khách order sang hóa đơn.
                String idsp  = sanPham.getProductId();
                String tensp  = sanPham.getName();
                String hinhsp  = sanPham.getImageURL();
                // thay
                String sizesp  = Sizesp;
                String topping  = Toppingsp;
                int soluongsp  = SLMoiNhat;
                long tonggiasp  = tongtien;
                String idbill  = "default";
//                Toast.makeText(ProductDetailActivity.this, SLMoiNhat+"", Toast.LENGTH_SHORT).show();

                ProductActivity.mangsanphamorder.add(new SanPhamOrder(idsp,tensp,tonggiasp,sizesp,topping,hinhsp,soluongsp,idbill));
                Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();

                finish();

            }
        });


    }

    private void TinhTongTien() {
        // Tính tổng tiền
        tongtien = (SLMoiNhat * GiaSize)+ GiaTopping;
        tvTongTienProductDetail.setText(tongtien+"");

    }

    private void ChonSize() {
        Sizesp = "Vừa";
        GiaSize = Integer.parseInt(sanPham.getPriceM());
        TinhTongTien();

        radioGroupSizeProDuctDetail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbsizelonproductdetail:
//                        Toast.makeText(ProductDetailActivity.this, "Bạn chọn size lớn", Toast.LENGTH_SHORT).show();
                        Sizesp = "Lớn";
                        GiaSize = Integer.parseInt(sanPham.getPriceL());
                        TinhTongTien();
                        break;
                    case R.id.rbsizevuaproductdetail:
                        Sizesp = "Vừa";
                        GiaSize = Integer.parseInt(sanPham.getPriceM());
                        TinhTongTien();
                        break;
                    case R.id.rbsizenhoproductdetail:
                        Sizesp = "Nhỏ";
                        GiaSize = Integer.parseInt(sanPham.getPriceS());
                        TinhTongTien();
                        break;
                }
            }
        });

    }

    private void ChonTopping() {
        layoutToppingProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCheckTopping.setVisibility(View.VISIBLE);
                GiaTopping = Integer.parseInt(sanPham.getPriceTopping());
                Toppingsp = sanPham.getTopping();
                Toast.makeText(ProductDetailActivity.this, "Bạn đã thêm topping!", Toast.LENGTH_SHORT).show();
                TinhTongTien();
            }
        });

        imgCheckTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCheckTopping.setVisibility(View.GONE);
                GiaTopping = 0;
                Toppingsp = null;
                Toast.makeText(ProductDetailActivity.this, "Bạn đã bỏ chọn topping!", Toast.LENGTH_SHORT).show();
                TinhTongTien();

            }
        });
    }

    private void ChonSoLuong() {
        SLMoiNhat = SoLuongSP;
        tvSoLuongproductDetail.setText(SLMoiNhat+"");
        TinhTongTien();

        // set điều kiện để cộng trừ sl
        if (SLMoiNhat >= 10){
            btnCongProductDetail.setVisibility(View.INVISIBLE);
            btnTruProductDetail.setVisibility(View.VISIBLE);

        }else if (SLMoiNhat > 1){
            btnCongProductDetail.setVisibility(View.VISIBLE);
            btnTruProductDetail.setVisibility(View.VISIBLE);

        }else if (SLMoiNhat == 1){
            btnCongProductDetail.setVisibility(View.VISIBLE);
            btnTruProductDetail.setVisibility(View.INVISIBLE);
        }

        btnCongProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SLMoiNhat = SoLuongSP += 1;
                tvSoLuongproductDetail.setText(SLMoiNhat+"");
                if (SLMoiNhat >= 10){
                    btnCongProductDetail.setVisibility(View.INVISIBLE);
                    btnTruProductDetail.setVisibility(View.VISIBLE);

                }else if (SLMoiNhat > 1){
                    btnCongProductDetail.setVisibility(View.VISIBLE);
                    btnTruProductDetail.setVisibility(View.VISIBLE);

                }else if (SLMoiNhat == 1){
                    btnCongProductDetail.setVisibility(View.VISIBLE);
                    btnTruProductDetail.setVisibility(View.INVISIBLE);
                }
                TinhTongTien();
            }
        });
        btnTruProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SLMoiNhat = SoLuongSP -= 1;
                tvSoLuongproductDetail.setText(SLMoiNhat+"");
                if (SLMoiNhat >= 10){
                    btnCongProductDetail.setVisibility(View.INVISIBLE);
                    btnTruProductDetail.setVisibility(View.VISIBLE);

                }else if (SLMoiNhat > 1){
                    btnCongProductDetail.setVisibility(View.VISIBLE);
                    btnTruProductDetail.setVisibility(View.VISIBLE);

                }else if (SLMoiNhat == 1){
                    btnCongProductDetail.setVisibility(View.VISIBLE);
                    btnTruProductDetail.setVisibility(View.INVISIBLE);
                }
                TinhTongTien();
            }
        });


    }

    private void GetProduct() {
        sanPham= (SanPham) getIntent().getSerializableExtra("thongtinsanpham");

        String topping = sanPham.getTopping();
        if (!topping.equals("default")){
            tvLayoutTopping.setVisibility(View.VISIBLE);
            layoutToppingProductDetail.setVisibility(View.VISIBLE);
            tvToppingProductDetail.setText(sanPham.getTopping());
            //custom giá
            int giaTopping = Integer.parseInt(sanPham.getPriceTopping());
            DecimalFormat decimalFormat =new DecimalFormat("###,###,###");
            tvGiaToppingProductDetail.setText("+ "+decimalFormat.format(giaTopping)+" đ");
        }

        tvTenProductDetail.setText(sanPham.getName());
        tvMoTaProductDetail.setText(sanPham.getNote());

        // custom giá sản phẩm
        int giaL = Integer.parseInt(sanPham.getPriceL());
        int giaM = Integer.parseInt(sanPham.getPriceM());
        int giaS = Integer.parseInt(sanPham.getPriceS());
        DecimalFormat decimalFormat =new DecimalFormat("###,###,###");
        tvGiaProductDetail.setText(decimalFormat.format(giaM)+ " đ");
        tvGiaSizeLonProductDetail.setText(decimalFormat.format(giaL)+ " đ");
        tvGiaSizeVuaProductDetail.setText(decimalFormat.format(giaM)+ " đ");
        tvGiaSizeNhoProductDetail.setText(decimalFormat.format(giaS)+ " đ");
        tvTongTienProductDetail.setText(decimalFormat.format(giaM)+ " đ");

        Picasso.with(this).load(sanPham.getImageURL()).placeholder(R.mipmap.ic_app_foreground).into(imgProductDetail);
    }

    private void AnhXa() {
        imgClose = findViewById(R.id.imgcloseproductdetail);
        imgLikeProDuctDetail = findViewById(R.id.imglikeproductdetail);
        imgProductDetail = findViewById(R.id.imgproductdetail);
        imgCheckTopping = findViewById(R.id.imgchecktopping);
        tvTenProductDetail = findViewById(R.id.tvtenproductdetail);
        tvGiaProductDetail = findViewById(R.id.tvgiaproductdetail);
        tvSoLuongproductDetail = findViewById(R.id.tvsoluongproductdetail);
        tvTongTienProductDetail = findViewById(R.id.tvtongtienproductdetail);
        tvMoTaProductDetail = findViewById(R.id.tvmotaproductdetail);
        tvGiaSizeLonProductDetail = findViewById(R.id.tvgiasizelonproductdetail);
        tvGiaSizeVuaProductDetail = findViewById(R.id.tvgiasizevuaproductdetail);
        tvGiaSizeNhoProductDetail = findViewById(R.id.tvgiasizenhoproductdetail);
        tvToppingProductDetail = findViewById(R.id.tvtoppingproductdetail);
        tvGiaToppingProductDetail = findViewById(R.id.tvgiatoppingproductdetail);
        tvLayoutTopping = findViewById(R.id.tvlayouttoppingproductdetail);
        btnTruProductDetail = findViewById(R.id.btntruproductdetail);
        btnCongProductDetail = findViewById(R.id.btncongproductdetail);
        layoutTongTien = findViewById(R.id.layouttongtienproductdetail);
        layoutLikeCongTruSoLuong = findViewById(R.id.layoutlikeandcongtrusoluong);
        layoutToppingProductDetail = findViewById(R.id.layouttoppingproductdetail);
        radioGroupSizeProDuctDetail = findViewById(R.id.radiobuttonsizeproductdetail);



        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layoutTongTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this,BillActivity.class));
            }
        });

    }
}