package com.symbiz.test.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.symbiz.test.Fragment.GallaryFragment;
import com.symbiz.test.Model.SearchListModel;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;

import java.util.List;

public class GallaryAdapter extends RecyclerView.Adapter<GallaryAdapter.ViewHolder> {
    GallaryFragment context;
    List<SearchListModel> gallaryList;
    public GallaryAdapter(GallaryFragment gallaryFragment, List<SearchListModel> gallaryList) {
        this.context = gallaryFragment;
        this.gallaryList = gallaryList;
    }

    @NonNull
    @Override
    public GallaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item_list,parent,false);
        return new GallaryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GallaryAdapter.ViewHolder holder, int position) {
        APIClient ap = new APIClient();
        holder.txt_galleryName.setText(gallaryList.get(position).SMGalleryName);
        Glide.with(context).load(ap.URL+gallaryList.get(position).SMGalleryPath).into(holder.img_galleryImage);
    }

    @Override
    public int getItemCount() {
        return gallaryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_galleryImage;
        TextView txt_galleryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_galleryName = itemView.findViewById(R.id.txt_galleryName);
            img_galleryImage = itemView.findViewById(R.id.img_galleryImage);
        }
    }
}
