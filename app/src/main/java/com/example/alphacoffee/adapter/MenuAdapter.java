package com.example.alphacoffee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.activity.ProductDetailActivity;
import com.example.alphacoffee.activity.SuaXoaMenuActivity;
import com.example.alphacoffee.model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
    Context context;
    ArrayList<SanPham> sanPhamArrayList;

    public MenuAdapter(Context context, ArrayList<SanPham> sanPhamArrayList) {
        this.context = context;
        this.sanPhamArrayList = sanPhamArrayList;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_product, parent, false);

        return new MenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {
        SanPham sanPham = sanPhamArrayList.get(position);
        holder.tvTenProduct.setText(sanPham.getName());
        // custom giá
        int giatien = Integer.parseInt(sanPham.getPriceM());
        DecimalFormat decimalFormat =new DecimalFormat("###,###,###");
        holder.tvGiaProduct.setText(decimalFormat.format(giatien)+" đ");

        Picasso.with(context).load(sanPham.getImageURL()).placeholder(R.mipmap.ic_app_foreground).into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return sanPhamArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTenProduct, tvGiaProduct;
        ImageView imgProduct;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvGiaProduct = itemView.findViewById(R.id.tvgiaproduct);
            tvTenProduct = itemView.findViewById(R.id.tvtenproduct);
            imgProduct = itemView.findViewById(R.id.imgproduct);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, SuaXoaMenuActivity.class);
                    intent.putExtra("suaxoamenu", sanPhamArrayList.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

}
