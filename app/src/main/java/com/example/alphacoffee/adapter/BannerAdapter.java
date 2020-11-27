package com.example.alphacoffee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.Banner;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    private List<Banner> banners;
    private ViewPager2 viewPager2;

    public BannerAdapter(List<Banner> banners, ViewPager2 viewPager2) {
        this.banners = banners;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setImg(banners.get(position));

        if (position == banners.size() - 2){
            viewPager2.post(runnable);
        }

    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView img;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgbanner);
        }

        void setImg(Banner banner){
            img.setImageResource(banner.getImage());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            banners.addAll(banners);
            notifyDataSetChanged();
        }
    };}
