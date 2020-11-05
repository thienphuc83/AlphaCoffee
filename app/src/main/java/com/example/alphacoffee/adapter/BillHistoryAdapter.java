package com.example.alphacoffee.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class BillHistoryAdapter extends RecyclerView.Adapter<BillHistoryAdapter.ViewHolder> {

    Context context;
    ArrayList<Bill> billArrayList;

    public BillHistoryAdapter(Context context, int item_bill, ArrayList<Bill> billArrayList) {
        this.context = context;
        this.billArrayList = billArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_bill,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Bill bill = billArrayList.get(position);
        holder.tvSTT.setText(bill.getSoThuTu());
        holder.tvTenCH.setText(bill.getTenCH());
        holder.tvTrangThai.setText(bill.getTrangThai());
        holder.tvNgayTao.setText(bill.getNgayTao());
        holder.tvTongTien.setText(bill.getTongtien()+"");


    }

    @Override
    public int getItemCount() {
        return billArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSTT, tvTenCH, tvTrangThai, tvNgayTao, tvTongTien;
        Button btnChiTiet;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvSTT = itemView.findViewById(R.id.tvsttbillhistory);
            tvTenCH = itemView.findViewById(R.id.tvtencuahangbillhistory);
            tvTrangThai = itemView.findViewById(R.id.tvtrangthaibillhistory);
            tvNgayTao = itemView.findViewById(R.id.tvngaytaobillhistory);
            tvTongTien = itemView.findViewById(R.id.tvtongtienbillhistory);
            btnChiTiet = itemView.findViewById(R.id.btnchitietbillhistory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BillDetailActivity.class);
                    intent.putExtra("hoadonchitiet", billArrayList.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}