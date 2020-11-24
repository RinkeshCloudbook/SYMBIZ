package com.symbiz.test.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.symbiz.test.Fragment.DirectoryFragment;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.Model.Tools;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;
import com.symbiz.test.utils.ViewAnimation;

import java.util.List;

public class DirectotyAdapter extends RecyclerView.Adapter<DirectotyAdapter.ViewHolder> {

    DirectoryFragment context;
    List<CommonModel> directoryList;

    public DirectotyAdapter(DirectoryFragment directoryFragment, List<CommonModel> directoryList) {
        this.context = directoryFragment;
        this.directoryList = directoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.directory_item_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final CommonModel p = directoryList.get(position);
        holder.txt_companyName.setText(directoryList.get(position).dCompanyName);


        APIClient ac = new APIClient();
        Glide.with(context).load(ac.URL+directoryList.get(position).dLogoPath).into(holder.img_dirLogo);
        holder.txt_catName.setText(directoryList.get(position).dCatName);
        holder.txt_expandAddress.setText(directoryList.get(position).dAddress);
        holder.txt_expandEmail.setText(directoryList.get(position).dEmail);
        holder.txt_expandPhone.setText(directoryList.get(position).dPhone);
        holder.txt_expandWebSite.setText(directoryList.get(position).dWebUrl);

        holder.bt_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Data Expand");
                boolean show = toggleLayoutExpand(!p.expanded, v, holder.lyt_expand);
                directoryList.get(position).expanded = show;
            }
        });
    }

    private boolean toggleLayoutExpand(boolean show, View view, LinearLayout lyt_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(lyt_expand);
        } else {
            ViewAnimation.collapse(lyt_expand);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return directoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton bt_expand;
        LinearLayout lyt_expand;
        TextView txt_companyName,txt_expandWebSite,txt_expandPhone,txt_expandEmail,txt_expandAddress,txt_catName;
        ImageView img_dirLogo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bt_expand = itemView.findViewById(R.id.bt_expand);
            lyt_expand = itemView.findViewById(R.id.lyt_expand);
            txt_companyName = itemView.findViewById(R.id.txt_companyName);
            img_dirLogo = itemView.findViewById(R.id.img_dirLogo);
            txt_expandWebSite = itemView.findViewById(R.id.txt_expandWebSite);
            txt_expandPhone = itemView.findViewById(R.id.txt_expandPhone);
            txt_expandEmail = itemView.findViewById(R.id.txt_expandEmail);
            txt_expandAddress = itemView.findViewById(R.id.txt_expandAddress);
            txt_catName = itemView.findViewById(R.id.txt_catName);
        }
    }
}
