package com.symbiz.test.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.symbiz.test.Fragment.DirectoryFragment;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.R;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {
    DirectoryFragment context;
    List<CommonModel> stateList;
    List<CommonModel> FiilterstateList;

    public StateAdapter(DirectoryFragment directoryFragment, List<CommonModel> stateList) {
        this.context = directoryFragment;
        this.stateList = stateList;
        this.FiilterstateList = stateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.state_item_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.e("TEST","State Size data :"+FiilterstateList.size());
        holder.txt_state_name.setText(FiilterstateList.get(position).stateName);
        holder.lin_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Click State :"+FiilterstateList.get(position).stateId);
                context.getStateData(FiilterstateList.get(position).stateName, FiilterstateList.get(position).stateId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return FiilterstateList.size();

    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    FiilterstateList = stateList;
                } else {

                    List<CommonModel> filteredList = new ArrayList<>();
                    for (CommonModel androidVersion : stateList) {

                        //if (androidVersion.stateName.toLowerCase().contains(charString) && androidVersion.getEmail().toLowerCase().contains(charString)) {
                        if (androidVersion.stateName.toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }
                    FiilterstateList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = FiilterstateList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                FiilterstateList = (ArrayList<CommonModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_state_name;
        LinearLayout lin_state;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_state_name = itemView.findViewById(R.id.txt_state_name);
            lin_state = itemView.findViewById(R.id.lin_state);
        }
    }
}
