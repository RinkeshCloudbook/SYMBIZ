package com.symbiz.test.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.symbiz.test.Fragment.WalkInsFragment;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.R;

import java.util.List;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.ViewHolder> {
    WalkInsFragment context;
    List<CommonModel> callList;
    public CallListAdapter(WalkInsFragment walkInsFragment, List<CommonModel> callList) {
        this.context = walkInsFragment;
        this.callList = callList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_item_list,parent,false);
        return new CallListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_callPhone.setText(callList.get(position).callNumber);
        holder.txt_call_Date.setText(callList.get(position).calldateTime);
        holder.txt_callType.setText(callList.get(position).callType);

        if(callList.get(position).callType.equalsIgnoreCase("MISSED")){
            holder.txt_callType.setBackgroundResource(R.drawable.textview_bg);
        }else {
            holder.txt_callType.setBackgroundResource(R.drawable.textview_bg_gray);
        }
    }

    @Override
    public int getItemCount() {
        return callList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_callPhone,txt_call_Date,txt_callType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_callPhone = itemView.findViewById(R.id.txt_callPhone);
            txt_call_Date = itemView.findViewById(R.id.txt_call_Date);
            txt_callType = itemView.findViewById(R.id.txt_callType);
        }
    }
}
