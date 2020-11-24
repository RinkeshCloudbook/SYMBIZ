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

public class WebSearviceAdapter extends RecyclerView.Adapter<WebSearviceAdapter.ViewHolder> {
    WebsiteFragment context;
    List<CommonModel> serviceList;

    public WebSearviceAdapter(WebsiteFragment websiteFragment, List<CommonModel> serviceList) {
        this.context = websiteFragment;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.webservice_item_list,parent,false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull WebSearviceAdapter.ViewHolder holder, int i) {
        holder.txt_service_title.setText(serviceList.get(i).sTitle);
        holder.txt_service_dec.setText(serviceList.get(i).sDec);
        APIClient ap = new APIClient();
        Log.e("TEST","Get Icon URl :"+ap.URL+serviceList.get(i).sIconPath);
        //holder.floting_icon.setImageResource(Integer.parseInt(ap.URL+serviceList.get(i).sIconPath));
        Glide.with(context).load(ap.URL+serviceList.get(i).sIconPath)
                .into(holder.img_service);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_service_title,txt_service_dec;
        ImageView img_service;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_service_title = itemView.findViewById(R.id.txt_service_title);
            txt_service_dec = itemView.findViewById(R.id.txt_service_dec);
            img_service = itemView.findViewById(R.id.img_service);
        }
    }
}
