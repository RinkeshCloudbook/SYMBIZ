package com.symbiz.test.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.symbiz.test.Fragment.WebsiteFragment;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.R;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {
    WebsiteFragment context;
    List<CommonModel> faqList;

    public FaqAdapter(WebsiteFragment websiteFragment, List<CommonModel> faqList) {
        this.context = websiteFragment;
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faq_item_view,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FaqAdapter.ViewHolder holder, final int position) {
        holder.txt_faqQuestion.setText(faqList.get(position).fTitle);
        holder.txt_faq_dec.setText(faqList.get(position).fDec);
        holder.bt_toggle_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Position :"+position);
                //toggleSectionInfoOne(holder.bt_toggle_faq);
            }
        });
    }

    /*private void toggleSectionInfoOne(ImageButton bt_toggle_faq) {
        FaqAdapter.ViewHolder viewHolder = new ViewHolder();
        boolean show = toggleArrow(bt_toggle_faq);
        if(show){
            ViewAnimation.expand(lyt_expand_info_two, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    //Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info);
                }
            });
        }else {
            ViewAnimation.collapse(lyt_expand_info_two);
        }
    }*/

    private boolean toggleArrow(ImageButton bt_toggle_faq) {
        return false;
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton bt_toggle_faq;
        TextView txt_faqQuestion,txt_faq_dec;
        LinearLayout lyt_expand_info;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bt_toggle_faq = itemView.findViewById(R.id.bt_toggle_faq);
            txt_faqQuestion = itemView.findViewById(R.id.txt_faqQuestion);
            txt_faq_dec = itemView.findViewById(R.id.txt_faq_dec);
            lyt_expand_info = itemView.findViewById(R.id.lyt_expand_info);
        }
    }
}
