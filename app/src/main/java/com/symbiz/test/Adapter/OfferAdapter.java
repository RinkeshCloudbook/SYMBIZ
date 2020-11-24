package com.symbiz.test.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.symbiz.test.Fragment.OffersFragment;
import com.symbiz.test.Model.SearchListModel;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {
    OffersFragment context;
    List<SearchListModel> offerList;

    public OfferAdapter(OffersFragment offersFragment, List<SearchListModel> offerList) {
        this.context = offersFragment;
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_item_list,parent,false);
        return new OfferAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        APIClient ap = new APIClient();
        Glide.with(context).load(ap.URL+offerList.get(position).SMofferPath).into(holder.img_offerList);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_offerList;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_offerList = itemView.findViewById(R.id.img_offerList);
        }
    }
}
