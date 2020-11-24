package com.symbiz.test.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.symbiz.test.Fragment.WebsiteFragment;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {
    WebsiteFragment context;
    List<CommonModel> bannerList;

    public BannerAdapter(WebsiteFragment websiteFragment, List<CommonModel> bannerList) {
        this.context = websiteFragment;
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public BannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.banner_list_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.ViewHolder holder, int position) {
        APIClient ap = new APIClient();
        holder.txt_bannerTitle.setText(bannerList.get(position).bTitle);
        holder.txt_bannersubTitle.setText(bannerList.get(position).bSubTitle);
        Glide.with(context).load(ap.URL+bannerList.get(position).bPhotoPath)
                .into(holder.img_banner);
        Log.e("TEST","Banner Title :"+bannerList.get(position).bSubTitle);
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_bannerTitle,txt_bannersubTitle;
        ImageView img_banner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_banner = itemView.findViewById(R.id.img_banner);
            txt_bannerTitle = itemView.findViewById(R.id.txt_bannerTitle);
            txt_bannersubTitle = itemView.findViewById(R.id.txt_bannersubTitle);
        }
    }
}
