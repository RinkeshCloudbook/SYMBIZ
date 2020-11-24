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

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    DirectoryFragment context;
    List<CommonModel> cityList;
    List<CommonModel> filterCityList;

    public CityAdapter(DirectoryFragment directoryFragment, List<CommonModel> cityList) {
        this.context = directoryFragment;
        this.cityList = cityList;
        this.filterCityList = cityList;
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
        holder.txt_state_name.setText(filterCityList.get(position).cityName);
        holder.lin_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.getCityData(filterCityList.get(position).cityName, filterCityList.get(position).cityId, filterCityList.get(position).cityStateId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterCityList.size();
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    filterCityList = cityList;
                } else {

                    List<CommonModel> filteredList = new ArrayList<>();
                    for (CommonModel androidVersion : cityList) {

                        //if (androidVersion.stateName.toLowerCase().contains(charString) && androidVersion.getEmail().toLowerCase().contains(charString)) {
                        if (androidVersion.cityName.toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }
                    filterCityList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterCityList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterCityList = (ArrayList<CommonModel>) filterResults.values;
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
