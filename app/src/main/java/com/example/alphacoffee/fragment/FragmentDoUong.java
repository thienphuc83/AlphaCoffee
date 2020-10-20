package com.example.alphacoffee.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.ProductAdapter;
import com.example.alphacoffee.model.SanPham;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FragmentDoUong extends Fragment {
    View view;
    private RecyclerView rvDoUong;
    private EditText edtTimDoUong;
    ProductAdapter doUongAdapter;
    ArrayList<SanPham> mangDoUong;

    DatabaseReference mData;

    private String nameproduct= "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_douong, container,false);
        
        mData = FirebaseDatabase.getInstance().getReference();
        
        AnhXa();

        mangDoUong =new ArrayList<>();
        doUongAdapter = new ProductAdapter(getContext(),mangDoUong);
        GridLayoutManager layoutManager= new GridLayoutManager(getContext(),2);
        rvDoUong.setLayoutManager(layoutManager);
        rvDoUong.setAdapter(doUongAdapter);

        LoadData();

        // tìm kiếm sản phẩm
        edtTimDoUong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tentimkiem = edtTimDoUong.getText().toString();
                if (tentimkiem.length()>=2){
                    String tentimkieminhoa = tentimkiem.toLowerCase();//cho viết hoa hết
                    //tiến hành kiểm tra
                    nameproduct =tentimkieminhoa;
                    KiemTra();
                }else if (tentimkiem.length()==0){
                   //nếu ô tìm kiếm ko có đã ko có nhập vào hay đã xóa hết thì sẽ update lại và gán mảng data ban đầu vào
                    doUongAdapter.UpdateKhiTimKiem(mangDoUong);
                }
            }
        });

        return view;
    }

    private void KiemTra() {
        ArrayList<SanPham> arrTen = null;
        if (nameproduct.length() >= 2) {
            arrTen = new ArrayList<>();
            for (SanPham sanPham1 : mangDoUong) {
                String sp = sanPham1.getName().toLowerCase();
                if (sp.indexOf(nameproduct) >= 0) {
                    arrTen.add(sanPham1);
                }
            }
        } else {
            arrTen = mangDoUong;
        }
        // update dữ liệu sau khi lọc
        doUongAdapter.UpdateKhiTimKiem(arrTen);
    }

    private void LoadData() {
        mData.child("SanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
//                Log.d("AAA", sanPham.getName());
                String id = snapshot.getKey();
                assert sanPham != null;

                String loaisanpham= sanPham.getType();
                if (loaisanpham.equals("Đồ uống")){
                    mangDoUong.add(new SanPham(sanPham.getName(),
                            sanPham.getPrice(),
                            sanPham.getNote(),
                            sanPham.getSize(),
                            sanPham.getTopping(),
                            sanPham.getLike(),
                            sanPham.getType(),
                            sanPham.getImageURL()));
                    doUongAdapter.notifyDataSetChanged();
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
        rvDoUong = view.findViewById(R.id.rvdouong);
        edtTimDoUong = view.findViewById(R.id.edttimkiemdouong);
    }
}
