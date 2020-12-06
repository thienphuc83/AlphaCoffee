package com.example.alphacoffee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.activity.AccountDetailActivity;
import com.example.alphacoffee.activity.AdminManagerActivity;
import com.example.alphacoffee.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder> {

    Context context;
    ArrayList<User> mangNhanVien;

    public NhanVienAdapter(Context context, ArrayList<User> mangNhanVien) {
        this.context = context;
        this.mangNhanVien = mangNhanVien;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nhanvien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = mangNhanVien.get(position);
        holder.tvTenNV.setText(user.getName());
        holder.tvMotaNV.setText(user.getNote());
        if (user.getImageURL().equals("default")){
            holder.imgNV.setImageResource(R.drawable.example);
        }else {
            Picasso.with(context).load(user.getImageURL()).into(holder.imgNV);

        }


    }

    @Override
    public int getItemCount() {
        return mangNhanVien.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgNV;
        TextView tvTenNV, tvMotaNV;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            imgNV = itemView.findViewById(R.id.imgnhanvien);
            tvTenNV = itemView.findViewById(R.id.tvtennhanvien);
            tvMotaNV = itemView.findViewById(R.id.tvmotanhanvien);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AccountDetailActivity.class);
                    intent.putExtra("accountdetail",mangNhanVien.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
