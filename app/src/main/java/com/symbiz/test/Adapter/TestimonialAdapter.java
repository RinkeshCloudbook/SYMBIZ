package com.symbiz.test.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.symbiz.test.Fragment.WebsiteFragment;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;

import java.util.List;

public class TestimonialAdapter extends RecyclerView.Adapter<TestimonialAdapter.ViewHolder> {
    WebsiteFragment context;
    List<CommonModel> testimonialList;
    public TestimonialAdapter(WebsiteFragment websiteFragment, List<CommonModel> testimonialList) {
        this.context = websiteFragment;
        this.testimonialList = testimonialList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.testimonial_item_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestimonialAdapter.ViewHolder holder, int position) {
        APIClient ap = new APIClient();
        Glide.with(context).load(ap.URL+testimonialList.get(position).tPhotoPath)
                .into(holder.img_photo);
        holder.txt_monialName.setText(testimonialList.get(position).tName);
        holder.txt_monialMessage.setText(testimonialList.get(position).tMessage);
        holder.rating_bar.setNumStars(Integer.parseInt(testimonialList.get(position).tStar));
        Log.e("TEST","Get Total star :"+testimonialList.get(position).tStar);
    }

    @Override
    public int getItemCount() {
        return testimonialList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircularImageView img_photo;
        TextView txt_monialName,txt_monialMessage;
        AppCompatRatingBar rating_bar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_photo = itemView.findViewById(R.id.img_photo);
            txt_monialName = itemView.findViewById(R.id.txt_monialName);
            txt_monialMessage = itemView.findViewById(R.id.txt_monialMessage);
            rating_bar = itemView.findViewById(R.id.rating_bar);
        }
    }
}
