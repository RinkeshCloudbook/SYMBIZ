package com.symbiz.test.Adapter;

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

public class PartnerAdapter extends RecyclerView.Adapter<PartnerAdapter.ViewHolder> {
    WebsiteFragment context;
    List<CommonModel> partneList;

    public PartnerAdapter(WebsiteFragment websiteFragment, List<CommonModel> partneList) {
        this.context = websiteFragment;
        this.partneList = partneList;
    }

    @NonNull
    @Override
    public PartnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partner_item_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnerAdapter.ViewHolder holder, int position) {
        APIClient ap = new APIClient();
        Glide.with(context).load(ap.URL+partneList.get(position).pLogopath)
                .into(holder.img_partnerLogo);
        holder.txt_partnerName.setText(partneList.get(position).pName);
        holder.txt_partnerWebsite.setText(partneList.get(position).pWebLink);
    }

    @Override
    public int getItemCount() {
        return partneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_partnerLogo;
        TextView txt_partnerName,txt_partnerWebsite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_partnerLogo = itemView.findViewById(R.id.img_partnerLogo);
            txt_partnerName = itemView.findViewById(R.id.txt_partnerName);
            txt_partnerWebsite = itemView.findViewById(R.id.txt_partnerWebsite);
        }
    }
}
