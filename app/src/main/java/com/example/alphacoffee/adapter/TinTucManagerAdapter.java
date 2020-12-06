package com.example.alphacoffee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.activity.NewspaperDetailActivity;
import com.example.alphacoffee.activity.SuaXoaTinTucActivity;
import com.example.alphacoffee.model.TinTuc;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TinTucManagerAdapter extends RecyclerView.Adapter<TinTucManagerAdapter.ViewHolder> {

    Context context;
    ArrayList<TinTuc> tinTucArrayList;

    public TinTucManagerAdapter(Context context, ArrayList<TinTuc> tinTucArrayList) {
        this.context = context;
        this.tinTucArrayList = tinTucArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tintuc,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TinTuc tinTuc = tinTucArrayList.get(position);

        holder.tvTen.setText(tinTuc.getTenTinTuc());
        holder.tvNoiDung.setText(tinTuc.getNoiDung());

        Picasso.with(context).load(tinTuc.getHinhAnh()).into(holder.imgHinh);


    }

    @Override
    public int getItemCount() {
        return tinTucArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHinh;
        TextView tvTen, tvNoiDung;
        Button btnChiTiet;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.imgtintuc);
            tvNoiDung = itemView.findViewById(R.id.tvnoidungtintuc);
            tvTen = itemView.findViewById(R.id.tvtentintuc);
            btnChiTiet = itemView.findViewById(R.id.btnchitiettintuc);

            btnChiTiet.setText("Sửa");
            btnChiTiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SuaXoaTinTucActivity.class);
                    intent.putExtra("suaxoatintuc",tinTucArrayList.get(getPosition()));
                    context.startActivity(intent);
                }
            });

        }
    }
}