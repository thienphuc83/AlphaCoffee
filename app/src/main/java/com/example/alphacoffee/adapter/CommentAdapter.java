package com.example.alphacoffee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.activity.StoreDetailActivity;
import com.example.alphacoffee.model.CommentStore;
import com.example.alphacoffee.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    ArrayList<CommentStore> commentStoreArrayList;

    public CommentAdapter(Context context, int item_comment, ArrayList<CommentStore> commentStoreArrayList) {
        this.context = context;
        this.commentStoreArrayList = commentStoreArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentStore commentStore = commentStoreArrayList.get(position);
        holder.tvTenKH.setText(commentStore.getTenKH());
        holder.tvNgayTao.setText(commentStore.getNgayTao());
        holder.tvNoiDung.setText(commentStore.getNoiDung());
        Picasso.with(context).load(commentStore.getHinhKH()).placeholder(R.drawable.example).into(holder.imgHinhKH);

    }

    @Override
    public int getItemCount() {
        return commentStoreArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTenKH, tvNgayTao, tvNoiDung;
        CircleImageView imgHinhKH;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKH = itemView.findViewById(R.id.tvtenkhcomment);
            tvNgayTao = itemView.findViewById(R.id.tvngaytaocomment);
            tvNoiDung = itemView.findViewById(R.id.tvnoidungcomment);
            imgHinhKH = itemView.findViewById(R.id.imghinhkhcomment);

        }
    }
}
