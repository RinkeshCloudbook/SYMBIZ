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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    WebsiteFragment context;
    List<CommonModel> productList;
    public ProductAdapter(WebsiteFragment websiteFragment, List<CommonModel> productList) {
        this.context = websiteFragment;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context.getContext())
                .inflate(R.layout.product_item_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        APIClient ap = new APIClient();
        Glide.with(context.getContext()).load(ap.URL+productList.get(position).proPhotoPath)
                .into(holder.img_product);
        holder.txt_productDec.setText(productList.get(position).proDec);
        holder.txt_productName.setText(productList.get(position).proName);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_product;
        TextView txt_productName,txt_productDec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            txt_productName = itemView.findViewById(R.id.txt_productName);
            txt_productDec = itemView.findViewById(R.id.txt_productDec);
        }
    }
}
