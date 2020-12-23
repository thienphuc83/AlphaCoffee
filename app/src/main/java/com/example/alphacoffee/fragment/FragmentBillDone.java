package com.example.alphacoffee.fragment;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

import java.util.ArrayList;

public class FragmentBillDone extends Fragment {

    View view;
    TextView tvThongBao;
    RecyclerView rvBillHistoryDone;
    BillHistoryAdapter billHistoryAdapter;
    ArrayList<Bill> mangbilldone;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference mData;

    User user;
    Bill bill, cobillthaydoi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_billdone,container,false);
        rvBillHistoryDone = view.findViewById(R.id.rvbillhistorydone);
        tvThongBao = view.findViewById(R.id.tvthongbaobilldone);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        mData = FirebaseDatabase.getInstance().getReference();

        mangbilldone = new ArrayList<>();
        billHistoryAdapter = new BillHistoryAdapter(getContext(),R.layout.item_bill,mangbilldone);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvBillHistoryDone.setLayoutManager(linearLayoutManager);
        rvBillHistoryDone.setAdapter(billHistoryAdapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("my notifi", "my notifi", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }


        //lấy data user
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

        //lấy data bill
        mData.child("Bill").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                bill = snapshot.getValue(Bill.class);
//                Log.d("CCC", bill.getTenCH());

                String iduser = user.getUserId();
                String idusertrongbill = bill.getIdKH();
                String trangthai = bill.getTrangThai();

                // lấy bill theo idKH
                if (iduser.equals(idusertrongbill)) {
                    if (trangthai.equals("Hoàn thành")){
                        mangbilldone.add(new Bill(bill.getIdBill(),
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
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // nếu có data thay đổi thì load lại bill
//                LoadData();
                // thông báo cho người dùng: đơn hàng đã hoàn thành và đang giao hàng
                // thông báo cho người dùng: đơn hàng đã đc nhận và đang xử lý
                cobillthaydoi = snapshot.getValue(Bill.class);
                String idKHdangxem = user.getUserId();
                String idKHbillthaydoi = cobillthaydoi.getIdKH();
                String trangthaibillthaydoi =  cobillthaydoi.getTrangThai();
                if (idKHdangxem.equals(idKHbillthaydoi)){
                    if (  trangthaibillthaydoi.equals("Hoàn thành")){
//                        Toast.makeText(getContext(), "Có 1 đơn đã hoàn thành,bật thông báo tại đây!", Toast.LENGTH_SHORT).show();

                        // bật thông báo ngay đây
                        NotificationCompat.Builder builder= new NotificationCompat.Builder(getContext(),"my notifi");
                        builder.setContentTitle("The Alpha Coffee");
                        builder.setContentText("Đơn hàng đã xong!");
                        builder.setSmallIcon(R.drawable.ic_icon_coffee);
                        builder.setAutoCancel(true);
                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
                        notificationManagerCompat.notify(1, builder.build());


                        LoadData();
                    }else {
                        LoadData();
                    }

                }else if (!idKHdangxem.equals(idKHbillthaydoi)){
//                    Toast.makeText(getContext(), "có đơn hoàn thành nhưng của tài khoản khác! aheee", Toast.LENGTH_SHORT).show();
                    LoadData();
                }
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

    private void LoadData() {
        //clear mảng
        mangbilldone.clear();
        // load lại data sau thay đổi
        mData.child("Bill").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                bill = snapshot.getValue(Bill.class);
//                Log.d("CCC", bill.getTenCH());

                String iduser = user.getUserId();
                String idusertrongbill = bill.getIdKH();
                String trangthai = bill.getTrangThai();

                // lấy bill theo idKH
                if (iduser.equals(idusertrongbill)) {
                    if (trangthai.equals("Hoàn thành")){
                        mangbilldone.add(new Bill(bill.getIdBill(),
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
