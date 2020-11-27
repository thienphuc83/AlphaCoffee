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
import com.example.alphacoffee.activity.OrderDetailActivity;
import com.example.alphacoffee.model.Bill;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<Bill> billArrayList;

    public OrderHistoryAdapter(Context context, ArrayList<Bill> billArrayList) {
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

        String trangthai = bill.getTrangThai();

        if (trangthai.equals("Hoàn thành")){
            holder.tvTrangThai.setText(bill.getTrangThai());
            holder.tvTrangThai.setTextColor(Color.GREEN);
        }else if (trangthai.equals("Đang xử lý")){
            holder.tvTrangThai.setText(bill.getTrangThai());
            holder.tvTrangThai.setTextColor(Color.BLUE);
        }

        holder.tvTenCH.setText(bill.getTenCH());
        holder.tvNgayTao.setText(bill.getNgayTao());
        holder.tvSTT.setText(bill.getSoThuTu());

        // custom giá
        long giatien = bill.getTongtien();
        DecimalFormat decimalFormat =new DecimalFormat("###,###,###");
        holder.tvTongTien.setText(decimalFormat.format(giatien)+" đ");

    }

    @Override
    public int getItemCount() {
        return billArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSTT, tvTenCH, tvTrangThai, tvNgayTao, tvTongTien;
        Button btnChiTiet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSTT = itemView.findViewById(R.id.tvsttbillhistory);
            tvTenCH = itemView.findViewById(R.id.tvtencuahangbillhistory);
            tvTrangThai = itemView.findViewById(R.id.tvtrangthaibillhistory);
            tvNgayTao = itemView.findViewById(R.id.tvngaytaobillhistory);
            tvTongTien = itemView.findViewById(R.id.tvtongtienbillhistory);
            btnChiTiet = itemView.findViewById(R.id.btnchitietbillhistory);

            btnChiTiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("orderchitiet", billArrayList.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }


}
