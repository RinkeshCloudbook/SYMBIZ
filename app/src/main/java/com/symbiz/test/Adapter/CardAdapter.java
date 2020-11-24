package com.symbiz.test.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.symbiz.test.Model.SearchListModel;
import com.symbiz.test.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    FragmentActivity context;
    List<SearchListModel> cardList;

    public CardAdapter(FragmentActivity activity, List<SearchListModel> cardList) {
        this.context = activity;
        this.cardList = cardList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_cardCat.setText(cardList.get(position).SMwebName);
        holder.txt_cardCatDec.setText(cardList.get(position).SMwebDec);
        //Glide.with(context).load(cardList.get(position).SMlogoPath).into(holder.circular_image_card);

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       // CircularImageView circular_image_card;
        TextView txt_cardCat,txt_cardCatDec;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           // circular_image_card = itemView.findViewById(R.id.circular_image_card);
            txt_cardCat = itemView.findViewById(R.id.txt_cardCat);
            txt_cardCatDec = itemView.findViewById(R.id.txt_cardCatDec);
        }
    }
}
