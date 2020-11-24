package com.symbiz.test.Adapter;

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

public class BusinessCatAdapter extends RecyclerView.Adapter<BusinessCatAdapter.ViewHolder> {

    DirectoryFragment context;
    List<CommonModel> catList;
    List<CommonModel> FilterCatList;
    public BusinessCatAdapter(DirectoryFragment directoryFragment, List<CommonModel> catList) {
        this.context = directoryFragment;
        this.catList = catList;
        this.FilterCatList = catList;
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
        holder.txt_state_name.setText(FilterCatList.get(position).catName);
        holder.lin_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getCatData(FilterCatList.get(position).catName, FilterCatList.get(position).catId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return FilterCatList.size();
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    FilterCatList = catList;
                } else {

                    List<CommonModel> filteredList = new ArrayList<>();
                    for (CommonModel androidVersion : catList) {

                        //if (androidVersion.stateName.toLowerCase().contains(charString) && androidVersion.getEmail().toLowerCase().contains(charString)) {
                        if (androidVersion.catName.toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }
                    FilterCatList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = FilterCatList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                FilterCatList = (ArrayList<CommonModel>) filterResults.values;
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
