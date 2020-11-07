package com.example.alphacoffee.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.BillHistoryAdapter;
import com.example.alphacoffee.model.Bill;
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

public class FragmentBillAll extends Fragment {

    View view;
    TextView tvThongBao;
    RecyclerView rvBillHistoryAll;
    BillHistoryAdapter billHistoryAdapter;
    ArrayList<Bill> mangbillall;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference mData;

    User user;
    Bill bill;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_billall, container, false);
        rvBillHistoryAll = view.findViewById(R.id.rvbillhistoryall);
        tvThongBao = view.findViewById(R.id.tvthongbaobillall);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        mData = FirebaseDatabase.getInstance().getReference();

        mangbillall = new ArrayList<>();
        billHistoryAdapter = new BillHistoryAdapter(getContext(), R.layout.item_bill, mangbillall);
        rvBillHistoryAll.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBillHistoryAll.setAdapter(billHistoryAdapter);

        LoadData();

        return view;
    }

    private void LoadData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                assert user != null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mData.child("Bill").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                bill = snapshot.getValue(Bill.class);
//                Log.d("CCC", bill.getTenCH());
                if (bill == null) {
                    tvThongBao.setVisibility(View.VISIBLE);
                    rvBillHistoryAll.setVisibility(View.GONE);
                }

                String iduser = user.getUserId();
                String idusertrongbill = bill.getIdKH();

                // lấy bill theo idKH
                if (iduser.equals(idusertrongbill)) {
                    mangbillall.add(new Bill(bill.getIdBill(),
                            bill.getSoThuTu(),
                            bill.getNgayTao(),
                            bill.getTongtien(),
                            bill.getTrangThai(),
                            bill.getDiaDiem(),
                            bill.getGhiChu(),
                            bill.getLoaiThanhToan(),
                            bill.getTichDiem(),
                            bill.getIdKH(),
                            bill.getTenKH(),
                            bill.getTenCH(),
                            bill.getTenNV(),
                            bill.getIdNV()));
                    billHistoryAdapter.notifyDataSetChanged();
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

