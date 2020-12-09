package com.example.alphacoffee.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.activity.BillDetailActivity;
import com.example.alphacoffee.model.Bill;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {

    Context context;
    ArrayList<Bill> donhangArrayList;

    public DonHangAdapter(Context context, ArrayList<Bill> donhangArrayList) {
        this.context = context;
        this.donhangArrayList = donhangArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_donhang,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Bill bill = donhangArrayList.get(position);

        holder.tvSTT.setText("0");
        String trangthai = bill.getTrangThai();
        if (trangthai.equals("default")) {
            holder.tvTrangThai.setText("Chưa xử lý");
            holder.tvTrangThai.setTextColor(Color.YELLOW);
        }
        holder.tvTenCH.setText(bill.getTenCH());
        holder.tvTenKH.setText(bill.getTenKH());
        holder.tvNgayTao.setText(bill.getNgayTao());

        // custom giá
        long giatien = bill.getTongtien();
        DecimalFormat decimalFormat =new DecimalFormat("###,###,###");
        holder.tvTongTien.setText(decimalFormat.format(giatien)+" đ");

    }

    @Override
    public int getItemCount() {
        return donhangArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSTT, tvTenKH,tvTenCH, tvTrangThai, tvNgayTao, tvTongTien;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvSTT = itemView.findViewById(R.id.tvsttdonhang);
            tvTenCH = itemView.findViewById(R.id.tvtencuahangdonhang);
            tvTenKH = itemView.findViewById(R.id.tvtenkhachhangdonhang);
            tvTrangThai = itemView.findViewById(R.id.tvtrangthaidonhang);
            tvNgayTao = itemView.findViewById(R.id.tvngaytaodonhang);
            tvTongTien = itemView.findViewById(R.id.tvtongtiendonhang);

        }
    }
}
