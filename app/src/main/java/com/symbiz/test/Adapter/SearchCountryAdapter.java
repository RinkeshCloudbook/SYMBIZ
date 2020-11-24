package com.symbiz.test.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.symbiz.test.Activity.SignUp;
import com.symbiz.test.Model.SearchListModel;
import com.symbiz.test.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchCountryAdapter extends BaseAdapter {

    Context context;
    List<SearchListModel> countryList;
    LayoutInflater inflater;
    List<SearchListModel> arraylist;

    public SearchCountryAdapter(SignUp signUp, List<SearchListModel> countryList) {
        this.context = signUp;
        this.countryList = countryList;
        inflater =LayoutInflater.from(signUp);
        this.arraylist = new ArrayList<SearchListModel>();
        this.arraylist.addAll(countryList);
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Object getItem(int position) {
        return countryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v =view;
        Holder holder;
        if(v == null){
            v = inflater.inflate(R.layout.search_item_list,null);
            holder = new Holder();
            v.setTag(holder);
            holder.txtcName = v.findViewById(R.id.txtcName);

        }else {
            holder = (Holder) v.getTag();
        }
        holder.txtcName.setText(countryList.get(position).cName);

        return v;
    }

    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        countryList.clear();
        if(text.length() == 0){
            countryList.addAll(arraylist);
        }else {
            for (SearchListModel  wp : arraylist) {
                if (wp.cName.toLowerCase(Locale.getDefault()).contains(text)) {
                    countryList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class Holder {
        TextView txtcName;
    }
}
