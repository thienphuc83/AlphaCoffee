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
import com.example.alphacoffee.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> {
    Context context;
    ArrayList<User> mangKhachHang;

    public KhachHangAdapter(Context context, ArrayList<User> mangKhachHang) {
        this.context = context;
        this.mangKhachHang = mangKhachHang;
    }

    @NonNull
    @Override
    public KhachHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_khachhang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhachHangAdapter.ViewHolder holder, int position) {

        User user = mangKhachHang.get(position);
        holder.tvTenKH.setText(user.getName());
        holder.tvEmailKH.setText(user.getEmail());
        if (user.getImageURL().equals("default")) {
            holder.imgKH.setImageResource(R.drawable.example);
        } else {
            Picasso.with(context).load(user.getImageURL()).into(holder.imgKH);

        }


    }

    @Override
    public int getItemCount() {
        return mangKhachHang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgKH;
        TextView tvTenKH, tvEmailKH;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgKH = itemView.findViewById(R.id.imgkh);
            tvTenKH = itemView.findViewById(R.id.tvtenkh);
            tvEmailKH = itemView.findViewById(R.id.tvemailkh);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AccountDetailActivity.class);
                    intent.putExtra("accountdetail",mangKhachHang.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
