package com.example.alphacoffee.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.activity.StoreDetailActivity;
import com.example.alphacoffee.activity.SuaXoaCuaHangActivity;
import com.example.alphacoffee.model.CuaHang;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CuaHangManagerAdapter extends RecyclerView.Adapter<CuaHangManagerAdapter.ViewHolder> {

    Context context;
    ArrayList<CuaHang> cuaHangArrayList;

    public CuaHangManagerAdapter(Context context, ArrayList<CuaHang> cuaHangArrayList) {
        this.context = context;
        this.cuaHangArrayList = cuaHangArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_cuahang, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CuaHang cuaHang = cuaHangArrayList.get(position);
        holder.tvTenCuaHang.setText(cuaHang.getTenCuaHang());
        holder.tvSDTCuaHang.setText(cuaHang.getSoDienThoai());
        holder.tvGioMoCua.setText(cuaHang.getGioMoCua());
        holder.tvDiaChi.setText(cuaHang.getDiaChi());
        Picasso.with(context).load(cuaHang.getHinhAnh()).into(holder.imgAnhCuaHang);
    }

    @Override
    public int getItemCount() {
        return cuaHangArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTenCuaHang, tvSDTCuaHang, tvGioMoCua, tvDiaChi;
        ImageView imgAnhCuaHang;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvTenCuaHang = itemView.findViewById(R.id.tvtencuahang);
            tvSDTCuaHang = itemView.findViewById(R.id.tvsdtcuahang);
            tvGioMoCua = itemView.findViewById(R.id.tvgiomocua);
            tvDiaChi = itemView.findViewById(R.id.tvdiachicuahang);
            imgAnhCuaHang = itemView.findViewById(R.id.imgcuahang);

            //set font tvlogan
            Typeface typeface= Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/NABILA.TTF");
            tvTenCuaHang.setTypeface(typeface);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SuaXoaCuaHangActivity.class);
                    intent.putExtra("suaxoacuahang", cuaHangArrayList.get(getPosition()));
                    context.startActivity(intent);
                }
            });

        }
    }
}

