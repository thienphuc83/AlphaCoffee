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
import com.example.alphacoffee.activity.OrderActivity;
import com.example.alphacoffee.model.Bill;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    Context context;
    ArrayList<Bill> billArrayList;

    public OrderAdapter(Context context, ArrayList<Bill> billArrayList) {
        this.context = context;
        this.billArrayList = billArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Bill bill = billArrayList.get(position);

        String stt = bill.getSoThuTu();
        String trangthai = bill.getTrangThai();

        if (stt.equals("default")){
            holder.tvSTT.setText("0");
        }else {
            holder.tvSTT.setText(bill.getSoThuTu());
        }

        if (trangthai.equals("default")){
            holder.tvTrangThai.setText("Chưa xử lý");
            holder.tvTrangThai.setTextColor(Color.YELLOW);
        }

        holder.tvTenCH.setText(bill.getTenCH());
        holder.tvNgayTao.setText(bill.getNgayTao());

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
        Button btnOrder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSTT = itemView.findViewById(R.id.tvsttorder);
            tvTenCH = itemView.findViewById(R.id.tvtencuahangorder);
            tvTrangThai = itemView.findViewById(R.id.tvtrangthaiorder);
            tvNgayTao = itemView.findViewById(R.id.tvngaytaoorder);
            tvTongTien = itemView.findViewById(R.id.tvtongtienorder);
            btnOrder = itemView.findViewById(R.id.btnorder);

            btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderActivity.class);
                    intent.putExtra("order", billArrayList.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
