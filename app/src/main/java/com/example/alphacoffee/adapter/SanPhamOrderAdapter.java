package com.example.alphacoffee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.SanPhamOrder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SanPhamOrderAdapter extends RecyclerView.Adapter<SanPhamOrderAdapter.ViewHolder> {

    Context context;
    ArrayList<SanPhamOrder> sanPhamOrderArrayList;

    public SanPhamOrderAdapter(Context context, ArrayList<SanPhamOrder> sanPhamOrderArrayList) {
        this.context = context;
        this.sanPhamOrderArrayList = sanPhamOrderArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_product_order,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SanPhamOrder sanPhamOrder = sanPhamOrderArrayList.get(position);
        holder.tvTenSPOrder.setText(sanPhamOrder.getName());

        // custom giá
        long giatien = sanPhamOrder.getPrice();
        DecimalFormat decimalFormat =new DecimalFormat("###,###,###");
        holder.tvGiaSPOrder.setText(decimalFormat.format(giatien)+" đ");

        holder.tvSizeSPOrder.setText(sanPhamOrder.getSize());
        holder.tvSoLuongSPOrder.setText(sanPhamOrder.getSoluong()+"");
        Picasso.with(context).load(sanPhamOrder.getImageURL()).placeholder(R.mipmap.ic_app_foreground).into(holder.imgHinhSPOrder);

    }

    @Override
    public int getItemCount() {
        return sanPhamOrderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHinhSPOrder;
        TextView tvTenSPOrder, tvSizeSPOrder, tvGiaSPOrder, tvSoLuongSPOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSPOrder = itemView.findViewById(R.id.imgsanphamorder);
            tvTenSPOrder = itemView.findViewById(R.id.tvtensanphamorder);
            tvSizeSPOrder = itemView.findViewById(R.id.tvsizesanphamorder);
            tvSoLuongSPOrder = itemView.findViewById(R.id.tvsoluongsanphamorder);
            tvGiaSPOrder = itemView.findViewById(R.id.tvgiasanphamorder);

        }
    }
}
