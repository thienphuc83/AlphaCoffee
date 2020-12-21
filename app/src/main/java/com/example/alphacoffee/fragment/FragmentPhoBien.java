package com.example.alphacoffee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class FragmentPhoBien extends Fragment {
    View view;
    private RecyclerView rvPhoBien;
    ProductAdapter phoBienAdapter;
    ArrayList<SanPham> mangPhoBien;

    DatabaseReference mData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_phobien, container, false);
        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();

        mangPhoBien = new ArrayList<>();
        phoBienAdapter = new ProductAdapter(getContext(), mangPhoBien);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvPhoBien.setLayoutManager(layoutManager);
        rvPhoBien.setAdapter(phoBienAdapter);

        LoadData();

        return view;
    }

    private void LoadData() {
        mData.child("SanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
//                Log.d("AAA", sanPham.getName());
                assert sanPham != null;
                String like = sanPham.getLike();
                int like1 = Integer.parseInt(like.trim());
                if (!like.equals("default") && like1 > 10) {
                    mangPhoBien.add(new SanPham(
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
                    phoBienAdapter.notifyDataSetChanged();
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
        rvPhoBien = view.findViewById(R.id.rvphobien);
    }
}
