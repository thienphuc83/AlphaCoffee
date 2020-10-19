package com.example.alphacoffee.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.CuaHangAdapter;
import com.example.alphacoffee.model.CuaHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentStore extends Fragment {

    View view;
    private TextView tvCuaHang;
    private RecyclerView rvCuaHang;
    CuaHangAdapter cuaHangAdapter;
    ArrayList<CuaHang> mangCuaHang;

    DatabaseReference mData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store, container,false);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        mangCuaHang = new ArrayList<>();
        cuaHangAdapter = new CuaHangAdapter(getContext(),mangCuaHang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvCuaHang.setLayoutManager(layoutManager);
        rvCuaHang.setAdapter(cuaHangAdapter);

        LoadData();

        return view;
    }

    private void LoadData() {
        mData.child("CuaHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CuaHang cuaHang = snapshot.getValue(CuaHang.class);
//                Log.d("AAA", cuaHang.getTenCuaHang());

                //code lấy hóa đơn của khách hàng
//                String email = cuaHang.getEmail();
//                if (email.equals("thanh@gmail.com")){
                mangCuaHang.add(new CuaHang(cuaHang.getTenCuaHang(),
                        cuaHang.getSoDienThoai(),
                        cuaHang.getDiaChi(),
                        cuaHang.getGioMoCua(),
                        cuaHang.getHinhAnh()));

                cuaHangAdapter.notifyDataSetChanged();
//            }

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
        rvCuaHang = view.findViewById(R.id.rvcuahang);
        tvCuaHang = view.findViewById(R.id.tvcuahang);
        //set font tvlogan
        Typeface typeface= Typeface.createFromAsset(getResources().getAssets(),"fonts/NABILA.TTF");
        tvCuaHang.setTypeface(typeface);

    }
}
