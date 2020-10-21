package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.SanPham;
import com.example.alphacoffee.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

    }

    private void GetProduct() {
        SanPham sanPham= (SanPham) getIntent().getSerializableExtra("thongtinsanpham");

        String topping = sanPham.getTopping();
        if (!topping.equals("default")){
            tvLayoutTopping.setVisibility(View.VISIBLE);
            layoutToppingProductDetail.setVisibility(View.VISIBLE);
            tvToppingProductDetail.setText(sanPham.getTopping());
            tvGiaToppingProductDetail.setText("+ "+sanPham.getPriceTopping()+" đ");
        }

        tvTenProductDetail.setText(sanPham.getName());
        tvMoTaProductDetail.setText(sanPham.getNote());
        tvGiaProductDetail.setText(sanPham.getPriceM()+" đ");
        tvGiaSizeLonProductDetail.setText(sanPham.getPriceL()+" đ");
        tvGiaSizeVuaProductDetail.setText(sanPham.getPriceM()+" đ");
        tvGiaSizeNhoProductDetail.setText(sanPham.getPriceS()+" đ");
        tvTongTienProductDetail.setText(sanPham.getPriceM()+ " đ");

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

    }
}