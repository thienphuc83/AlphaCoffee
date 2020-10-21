package com.example.alphacoffee.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.ProductAdapter;
import com.example.alphacoffee.model.SanPham;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentDoAn extends Fragment {

    View view;

    private EditText edtTimDoAn;
    private RecyclerView rvDoAn;
    ProductAdapter doAnAdapter;
    ArrayList<SanPham> mangDoAn;

    DatabaseReference mData;

    private String nameproduct = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doan,container,false);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        mangDoAn =new ArrayList<>();
        doAnAdapter = new ProductAdapter(getContext(),mangDoAn);
        GridLayoutManager layoutManager= new GridLayoutManager(getContext(),2);
        rvDoAn.setLayoutManager(layoutManager);
        rvDoAn.setAdapter(doAnAdapter);

        LoadData();

//        tìm kiếm
        edtTimDoAn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tentimkiem = edtTimDoAn.getText().toString();
                if (tentimkiem.length()>=2){
                    String tentimkieminhoa = tentimkiem.toLowerCase();//cho viết hoa hết
                    //tiến hành kiểm tra
                    nameproduct =tentimkieminhoa;
                    KiemTra();
                }else if (tentimkiem.length()==0){
                    //nếu ô tìm kiếm ko có đã ko có nhập vào hay đã xóa hết thì sẽ update lại và gán mảng data ban đầu vào
                    doAnAdapter.UpdateKhiTimKiem(mangDoAn);
                }
            }
        });

        return view;
    }

    private void KiemTra() {
        ArrayList<SanPham> arrTen = null;
        if (nameproduct.length() >= 2) {
            arrTen = new ArrayList<>();
            for (SanPham sanPham1 : mangDoAn) {
                String sp = sanPham1.getName().toLowerCase();
                if (sp.indexOf(nameproduct) >= 0) {
                    arrTen.add(sanPham1);
                }
            }
        } else {
            arrTen = mangDoAn;
        }
        // update dữ liệu sau khi lọc
        doAnAdapter.UpdateKhiTimKiem(arrTen);
    }

    private void LoadData() {

        mData.child("SanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
//                Log.d("AAA", sanPham.getName());
                assert sanPham != null;
                String loaisanpham= sanPham.getType();
                if (loaisanpham.equals("Đồ ăn")){
                    mangDoAn.add(new SanPham(
                            sanPham.getProductId(),
                            sanPham.getName(),
                            sanPham.getPriceL(),
                            sanPham.getPriceM(),
                            sanPham.getPriceS(),
                            sanPham.getNote(),
                            sanPham.getTopping(),
                            sanPham.getPriceTopping(),
                            sanPham.getLike(),
                            sanPham.getType(),
                            sanPham.getImageURL()));
                    doAnAdapter.notifyDataSetChanged();
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
        rvDoAn= view.findViewById(R.id.rvdoan);
        edtTimDoAn= view.findViewById(R.id.edttimkiemdoan);
    }
}
