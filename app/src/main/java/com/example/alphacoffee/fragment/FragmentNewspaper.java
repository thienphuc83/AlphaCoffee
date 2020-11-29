package com.example.alphacoffee.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.BannerAdapter;
import com.example.alphacoffee.adapter.TinTucAdapter;
import com.example.alphacoffee.model.Banner;
import com.example.alphacoffee.model.TinTuc;
import com.example.alphacoffee.model.User;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class FragmentNewspaper extends Fragment {

    View view;

    private CircleImageView imgHinh;
    private TextView tvTen, tvDiem;
    private RecyclerView rvTinTuc;
    private LinearLayout layoutDiem;
    private ViewPager2 viewPager2;


    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference mData;
    User user;

    TinTucAdapter tinTucAdapter;
    ArrayList<TinTuc> mangtintuc;

    Handler handler= new Handler();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_newspaper, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        //lấy data user
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                assert user != null;

                Picasso.with(getContext()).load(user.getImageURL()).placeholder(R.drawable.example).into(imgHinh);
                tvTen.setText(user.getName());
                //set hiện điểm của khách hàng
//                String loai = user.getType();
//                if (loai.equals("Khách hàng")) {
//                    layoutDiem.setVisibility(View.VISIBLE);
//                    String diem = user.getPoint();
//                    if (diem.equals("default")){
//                        tvDiem.setText("0");
//                    }else {
//                        tvDiem.setText(user.getPoint());
//                    }
//
//                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //lay tintuc
        mData.child("TinTuc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TinTuc tinTuc = snapshot.getValue(TinTuc.class);

                mangtintuc.add(new TinTuc(
                        tinTuc.getTinTucId(),
                        tinTuc.getTenTinTuc(),
                        tinTuc.getNoiDung(),
                        tinTuc.getHinhAnh()));
                tinTucAdapter.notifyDataSetChanged();


//
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


        return view;
    }

    private void AnhXa() {
        imgHinh = view.findViewById(R.id.imghinhaccount);
        tvTen = view.findViewById(R.id.tvtenaccount);
        tvDiem = view.findViewById(R.id.tvdiemaccount);
        rvTinTuc = view.findViewById(R.id.rvtintuc);
        layoutDiem = view.findViewById(R.id.layoutdiem);
        viewPager2 = view.findViewById(R.id.viewpagerslide);

        //banner
        List<Banner> banners = new ArrayList<>();
        banners.add(new Banner(R.drawable.banner1));
        banners.add(new Banner(R.drawable.banner2));
        banners.add(new Banner(R.drawable.banner3));

        viewPager2.setAdapter(new BannerAdapter(banners,viewPager2));

        //chỉnh hiệu ứng
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        // auto
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,3000);
            }
        });


        //load data tintuc
        mangtintuc = new ArrayList<>();
        tinTucAdapter = new TinTucAdapter(getContext(), mangtintuc);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvTinTuc.setLayoutManager(new GridLayoutManager(getContext(),2));
        rvTinTuc.setAdapter(tinTucAdapter);


    }

    // chạy slide
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };
}
