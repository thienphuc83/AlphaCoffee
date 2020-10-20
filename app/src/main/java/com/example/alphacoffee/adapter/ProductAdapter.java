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
import com.example.alphacoffee.activity.LoginActivity;
import com.example.alphacoffee.model.SanPham;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    ArrayList<SanPham> sanPhamArrayList;

    public ProductAdapter(Context context, ArrayList<SanPham> sanPhamArrayList) {
        this.context = context;
        this.sanPhamArrayList = sanPhamArrayList;
    }

    public void UpdateKhiTimKiem(ArrayList<SanPham> arr){
        this.sanPhamArrayList = arr;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sanPham = sanPhamArrayList.get(position);
        holder.tvTenProduct.setText(sanPham.getName());
        holder.tvGiaProduct.setText(sanPham.getPrice()+" đ");
        Picasso.with(context).load(sanPham.getImageURL()).into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return sanPhamArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTenProduct, tvGiaProduct;
        ImageView imgProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGiaProduct = itemView.findViewById(R.id.tvgiaproduct);
            tvTenProduct = itemView.findViewById(R.id.tvtenproduct);
            imgProduct = itemView.findViewById(R.id.imgproduct);

        }
    }
}
