package com.symbiz.test.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.symbiz.test.Fragment.InquiryFragment;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.R;

import java.util.List;

public class InquiryAdapter extends RecyclerView.Adapter<InquiryAdapter.ViewHolder> {
    InquiryFragment inquiryFragment;
    List<CommonModel> inquiryList;

    public InquiryAdapter(InquiryFragment inquiryFragment, List<CommonModel> inquiryList) {
        this.inquiryFragment = inquiryFragment;
        this.inquiryList = inquiryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inquiry_item_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_fullName.setText(inquiryList.get(position).inFullname);
        holder.txt_mobile.setText(inquiryList.get(position).inMobile);
        holder.txt_date.setText(inquiryList.get(position).inSendDate);
    }

    @Override
    public int getItemCount() {
        return inquiryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_fullName,txt_mobile,txt_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_fullName = itemView.findViewById(R.id.txt_fullName);
            txt_mobile = itemView.findViewById(R.id.txt_mobile);
            txt_date = itemView.findViewById(R.id.txt_date);

        }
    }
}
