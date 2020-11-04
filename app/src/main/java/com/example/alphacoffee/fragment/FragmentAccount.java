package com.example.alphacoffee.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alphacoffee.R;
import com.example.alphacoffee.activity.BillHistoryActivity;
import com.example.alphacoffee.activity.HelpActivity;
import com.example.alphacoffee.activity.InfoAccountActivity;
import com.example.alphacoffee.activity.LoginActivity;
import com.example.alphacoffee.activity.MemberActivity;
import com.example.alphacoffee.activity.OrderHistoryActivity;
import com.example.alphacoffee.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentAccount extends Fragment {

    View view, viewKeNgangLichSu;
    private LinearLayout layoutThanhVien, layoutThongTinTaiKhoan, layoutLichSuKH, layoutLichSuNV, layoutGiupDo, layoutDangXuat, layoutHoaDonNhanVien;
    private TextView tvTenTaiKhoan, tvLoaiNguoiDung;
    private CircleImageView imgKhachHang;
    private ImageView imgIconLevel;


    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container,false);

        // code
        AnhXa();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        LoadData();
        return view;
    }

    private void LoadData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                assert user != null;
                String loainguoidung = user.getType();
                if (loainguoidung.equals("Nhân viên")){
                    tvLoaiNguoiDung.setText("Nhân viên");
                    layoutHoaDonNhanVien.setVisibility(View.VISIBLE);
                    layoutLichSuNV.setVisibility(View.VISIBLE);
                    viewKeNgangLichSu.setVisibility(View.VISIBLE);
                }else {
                    imgIconLevel.setVisibility(View.VISIBLE);
                    layoutThanhVien.setVisibility(View.VISIBLE);
                    layoutLichSuKH.setVisibility(View.VISIBLE);
                    viewKeNgangLichSu.setVisibility(View.VISIBLE);
                }
                tvTenTaiKhoan.setText(user.getName());
                if (user.getImageURL().equals("default")){
                    imgKhachHang.setImageResource(R.drawable.example);
                }else {
                    Picasso.with(getContext()).load(user.getImageURL()).into(imgKhachHang);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void AnhXa() {
        tvTenTaiKhoan = view.findViewById(R.id.tvtenkhachhang);
        tvLoaiNguoiDung = view.findViewById(R.id.tvloainguoidung);
        imgKhachHang = view.findViewById(R.id.imganhkhachhang);
        imgIconLevel = view.findViewById(R.id.imgiconlevel);
        layoutThanhVien= view.findViewById(R.id.layoutthealphacoffee);
        layoutThongTinTaiKhoan= view.findViewById(R.id.layoutthongtintaikhoan);
        layoutLichSuKH= view.findViewById(R.id.layoutlichsukhachhang);
        layoutLichSuNV= view.findViewById(R.id.layoutlichsunhanvien);
        layoutGiupDo= view.findViewById(R.id.layoutgiupdo);
        layoutDangXuat= view.findViewById(R.id.layoutdangxuat);
        layoutHoaDonNhanVien= view.findViewById(R.id.layouthoadonnhanvien);
        viewKeNgangLichSu= view.findViewById(R.id.viewkenganglichsu);

        //set font tvlogan
        Typeface typeface= Typeface.createFromAsset(getResources().getAssets(),"fonts/NABILA.TTF");
        tvTenTaiKhoan.setTypeface(typeface);

        layoutThanhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MemberActivity.class));
            }
        });

        layoutThongTinTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), InfoAccountActivity.class));
            }
        });

        layoutLichSuKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BillHistoryActivity.class));
            }
        });
        layoutLichSuNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OrderHistoryActivity.class));
            }
        });
        layoutGiupDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HelpActivity.class));
            }
        });

        layoutDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                // xóa hết ngăn sếp, chặn quay lại activity trước đó.
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }
}
