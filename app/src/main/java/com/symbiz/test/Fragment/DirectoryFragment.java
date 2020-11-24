package com.symbiz.test.Fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.symbiz.test.Adapter.BusinessCatAdapter;
import com.symbiz.test.Adapter.CityAdapter;
import com.symbiz.test.Adapter.DirectotyAdapter;
import com.symbiz.test.Adapter.StateAdapter;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;
import com.symbiz.test.Retrofit.ApiInterface;
import com.symbiz.test.Retrofit.getDirectoryExapmle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class DirectoryFragment extends Fragment {

    View mView;
    List<CommonModel> stateList = new ArrayList<>();
    List<CommonModel> cityList = new ArrayList<>();
    List<CommonModel> catList = new ArrayList<>();
    List<CommonModel> directoryList = new ArrayList<>();
    RecyclerView rv_state,rv_city,rv_bCat,rv_directory;
    EditText edt_state,edt_city,edt_business,edt_cName,edt_areaName;
    StateAdapter stateAdapter;
    CityAdapter cityAdapter;
    BusinessCatAdapter catAdapter;
    String getCityId,getStateId;
    ApiInterface apiInterface;
    FloatingActionButton fab_Search;
    boolean flag = true;

    public DirectoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_directory, container, false);
        apiInterface = APIClient.getClient().create(ApiInterface.class);

        /*edt_cName = mView.findViewById(R.id.edt_cName);
        edt_areaName = mView.findViewById(R.id.edt_areaName );
        edt_state = mView.findViewById(R.id.edt_state);
        edt_city = mView.findViewById(R.id.edt_city);
        edt_business = mView.findViewById(R.id.edt_business);
        rv_state = mView.findViewById(R.id.rv_state);
        rv_city = mView.findViewById(R.id.rv_city);
        rv_bCat = mView.findViewById(R.id.rv_bCat);*/
        rv_directory = mView.findViewById(R.id.rv_directory);
        fab_Search = mView.findViewById(R.id.fab_Search);

        /*RecyclerView.LayoutManager rvState = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_state.setLayoutManager(rvState);
        RecyclerView.LayoutManager rvCity = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_city.setLayoutManager(rvCity);
        RecyclerView.LayoutManager rvCat = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_bCat.setLayoutManager(rvCat);*/
        RecyclerView.LayoutManager rvDir = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_directory.setLayoutManager(rvDir);




        fab_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Fab Icone Click");
                if(flag == false){
                    getDirectorySearch("","","","","");
                    fab_Search.setImageResource(R.drawable.ic_baseline_search_24);
                    flag = true;
                }else if(flag == true){
                    showCustomDialog();
                }

            }
        });
        /*String s = "";
        int b = Integer.parseInt(s);*/

        getDirectorySearch("","","","","");

        /*((Button) mView.findViewById(R.id.btn_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDirectorySearch(edt_cName.getText().toString(),edt_areaName.getText().toString(),getCityId,getStateId,edt_business.getText().toString());
            }
        });*/
        return mView;
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_event);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        searchState();
        searchBusiness();

        final EditText edt_cName = dialog.findViewById(R.id.edt_cName);
        final EditText edt_areaName = dialog.findViewById(R.id.edt_areaName);
         edt_state = dialog.findViewById(R.id.edt_state);
         edt_city = dialog.findViewById(R.id.edt_city);
         edt_business = dialog.findViewById(R.id.edt_business);

        rv_state = dialog.findViewById(R.id.rv_state);
        rv_city = dialog.findViewById(R.id.rv_city);
        rv_bCat = dialog.findViewById(R.id.rv_bCat);

        RecyclerView.LayoutManager rvState = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_state.setLayoutManager(rvState);
        RecyclerView.LayoutManager rvCity = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_city.setLayoutManager(rvCity);
        RecyclerView.LayoutManager rvCat = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_bCat.setLayoutManager(rvCat);

        edt_state.addTextChangedListener(new TextWatcher() {
            long lastChange=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                rv_state.setVisibility(View.VISIBLE);
                stateAdapter.getFilter().filter(s.toString());
                /*if(s.length() >= 2){

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(System.currentTimeMillis() - lastChange >= 300){
                                rv_state.setVisibility(View.VISIBLE);
                                //searchCompany();

                            }
                        }
                    },300);
                }*/
            }
        });
        edt_city.addTextChangedListener(new TextWatcher() {
            long lastChange=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                rv_city.setVisibility(View.VISIBLE);
                cityAdapter.getFilter().filter(s.toString());
            }
        });
        edt_business.addTextChangedListener(new TextWatcher() {
            long lastChange=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                rv_bCat.setVisibility(View.VISIBLE);
                catAdapter.getFilter().filter(s.toString());
            }
        });

        ((Button) dialog.findViewById(R.id.btn_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getCityId == null && getStateId == null){
                    Log.e("TEST","Condition Null");
                    getDirectorySearch(edt_cName.getText().toString(),edt_areaName.getText().toString(),"","",edt_business.getText().toString());
                    fab_Search.setImageResource(R.drawable.ic_refresh);
                    flag = false;
                    dialog.dismiss();

                }else if(getCityId == null){
                    getDirectorySearch(edt_cName.getText().toString(),edt_areaName.getText().toString(),"",getStateId,edt_business.getText().toString());
                    fab_Search.setImageResource(R.drawable.ic_refresh);
                    flag = false;
                    dialog.dismiss();
                }else if(getStateId == null){
                    getDirectorySearch(edt_cName.getText().toString(),edt_areaName.getText().toString(),getCityId,"",edt_business.getText().toString());
                    fab_Search.setImageResource(R.drawable.ic_refresh);
                    flag = false;
                    dialog.dismiss();
                }else{
                    getDirectorySearch(edt_cName.getText().toString(),edt_areaName.getText().toString(),getCityId,getStateId,edt_business.getText().toString());
                    fab_Search.setImageResource(R.drawable.ic_refresh);
                    flag = false;
                    dialog.dismiss();
                }

            }
        });


        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        /*((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = new Event();
                event.email = tv_email.getText().toString();
                event.name = et_name.getText().toString();
                event.location = et_location.getText().toString();
                event.from = spn_from_date.getText().toString() + " (" + spn_from_time.getText().toString() + ")";
                event.to = spn_to_date.getText().toString() + " (" + spn_to_time.getText().toString() + ")";
                event.is_allday = cb_allday.isChecked();
                event.timezone = spn_timezone.getSelectedItem().toString();
                displayDataResult(event);

                dialog.dismiss();
            }
        });
*/
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void getDirectorySearch(String cName, String areaName, String getCityId, String getStateId, String catName) {

        Call<getDirectoryExapmle> call = apiInterface.getDirectory(cName,areaName,getCityId,getStateId,catName,"1");
        call.enqueue(new Callback<getDirectoryExapmle>() {
            @Override
            public void onResponse(Call<getDirectoryExapmle> call, retrofit2.Response<getDirectoryExapmle> response) {
                //Log.e("TEST"," Dir Message :"+response.body().getMessage());
                Log.e("TEST","Dir Status :"+response.body().getStatus());
                if(response.body().getStatus().equalsIgnoreCase("200")){
                    Log.e("TEST","Dir True");
                    directoryList.clear();
                    for (int i = 0; i < response.body().getResult().size(); i++) {
                        CommonModel cm = new CommonModel();
                        cm.dCid = response.body().getResult().get(i).getCustomerID();
                        cm.dCompanyName = response.body().getResult().get(i).getCompanyName();
                        cm.dWebUrl = response.body().getResult().get(i).getWebsiteURL();
                        cm.dEmail = response.body().getResult().get(i).getEmail();
                        cm.dIsMember = response.body().getResult().get(i).getIsMember().toString();
                        cm.dCountryCode = response.body().getResult().get(i).getCountryCode();
                        cm.dPhone = response.body().getResult().get(i).getMobileNo();
                        cm.dLogoPath = response.body().getResult().get(i).getLogoPath();
                        cm.dDec = response.body().getResult().get(i).getDescription();
                        cm.dCatName = response.body().getResult().get(i).getCategoryName();
                        cm.dAreaName = response.body().getResult().get(i).getAreaName();
                        cm.dAddress = response.body().getResult().get(i).getAddress();
                        cm.dCityName = response.body().getResult().get(i).getCityName();
                        cm.dStateName = response.body().getResult().get(i).getStateName();

                        directoryList.add(cm);
                    }

                    DirectotyAdapter directotyAdapter = new DirectotyAdapter(DirectoryFragment.this,directoryList);
                    rv_directory.setAdapter(directotyAdapter);
                }
            }

            @Override
            public void onFailure(Call<getDirectoryExapmle> call, Throwable t) {

            }
        });
    }

    private void searchCity(String sId) {
            APIClient ac = new APIClient();

            String city_url = ac.URL+"/api/GetCity?StateID="+sId+"&token=1";
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, city_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String getSiteSettingResponse = response.toString();
                    Log.e("TEST","City Search Response :"+getSiteSettingResponse);

                    if(getSiteSettingResponse != null){
                        try {
                            JSONObject jsonObject = new JSONObject(getSiteSettingResponse);
                            JSONArray stateArray = jsonObject.getJSONArray("result");
                            cityList.clear();
                            for (int i = 0; i < stateArray.length(); i++) {
                                JSONObject obj_state = stateArray.getJSONObject(i);
                                CommonModel cm = new CommonModel();
                                cm.cityId = obj_state.getString("id");
                                cm.cityName = obj_state.getString("name");
                                cm.cityStateId = obj_state.getString("stateID");

                                cityList.add(cm);
                            }

                            Log.e("TEST","City Size Class :"+cityList.size());
                            cityAdapter = new CityAdapter(DirectoryFragment.this,cityList);
                            rv_city.setAdapter(cityAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    Log.e("TEST","Site Setting Error :"+error);
                }
            }){
                protected JSONObject getParam(){
                    return  null;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
    }

    private void searchState() {
        APIClient ac = new APIClient();

            String state_url = ac.URL+"/api/GetState?CountryID="+""+"&token=1";
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, state_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String getSiteSettingResponse = response.toString();
                    Log.e("TEST","State Search Response :"+getSiteSettingResponse);

                    if(getSiteSettingResponse != null){
                        try {
                            JSONObject jsonObject = new JSONObject(getSiteSettingResponse);
                            JSONArray stateArray = jsonObject.getJSONArray("result");
                            stateList.clear();
                            for (int i = 0; i < stateArray.length(); i++) {
                                JSONObject obj_state = stateArray.getJSONObject(i);
                                CommonModel cm = new CommonModel();
                                cm.stateId = obj_state.getString("id");
                                cm.stateName = obj_state.getString("name");

                                stateList.add(cm);
                            }

                            Log.e("TEST","State Size Class :"+stateList.size());
                            //((TextView) mView.findViewById(R.id.txt_about_email)).setText(cm.ssEmailID);
                            //((TextView) mView.findViewById(R.id.txt_aboutUsData)).setText(Html.fromHtml(cm.ssAboutUs));
                            stateAdapter = new StateAdapter(DirectoryFragment.this,stateList);
                            rv_state.setAdapter(stateAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    Log.e("TEST","Site Setting Error :"+error);
                }
            }){
                protected JSONObject getParam(){
                    return  null;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        }

    private void searchBusiness() {
        APIClient ac = new APIClient();

        String city_url = ac.URL+"/api/GetCategory?token=1";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, city_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String getSiteSettingResponse = response.toString();
                Log.e("TEST","Category Search Response :"+getSiteSettingResponse);

                if(getSiteSettingResponse != null){
                    try {
                        JSONObject catObj = new JSONObject(getSiteSettingResponse);
                        String status = catObj.getString("status");
                        if(status.equalsIgnoreCase("200")){
                            Log.e("TEST","Condition True");
                            catList.clear();
                            JSONArray catArray = catObj.getJSONArray("result");
                            for (int i = 0; i < catArray.length(); i++) {
                                JSONObject obj_cat = catArray.getJSONObject(i);
                                CommonModel cm = new CommonModel();
                                cm.catId = Integer.parseInt(obj_cat.getString("id"));
                                cm.catName = obj_cat.getString("name");

                                catList.add(cm);
                            }
                        }
                        Log.e("TEST","Category Size Class :"+catList.size());
                        catAdapter = new BusinessCatAdapter(DirectoryFragment.this,catList);
                        rv_bCat.setAdapter(catAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                Log.e("TEST","Site Setting Error :"+error);
            }
        }){
            protected JSONObject getParam(){
                return  null;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void getStateData(String stateName, String stateId){
        edt_state.setText(stateName);
        searchCity(stateId);
        getStateId = stateId;

        rv_state.setVisibility(View.GONE);
    }

    public void getCityData(String cityName, String cityId, String cityStateId){
        edt_city.setText(cityName);
        getCityId = cityId;

        rv_city.setVisibility(View.GONE);
    }

    public void getCatData(String catName, int catId){
        edt_business.setText(catName);
        rv_bCat.setVisibility(View.GONE);
    }

}