package com.symbiz.test.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.symbiz.test.Adapter.InquiryAdapter;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;
import com.symbiz.test.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InquiryFragment extends Fragment {

    View mView;
    SharedPreferences sp;
    ApiInterface apiInterface;
    APIClient ac;
    List<CommonModel> inquiryList = new ArrayList<>();
    RecyclerView rv_inquiryList;

    public InquiryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_inquiry, container, false);

        apiInterface = APIClient.getClient().create(ApiInterface.class);
        rv_inquiryList = mView.findViewById(R.id.rv_inquiryList);
        sp = getActivity().getSharedPreferences("userDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        String uId = sp.getString("uId","");

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_inquiryList.setLayoutManager(manager);

        ac = new APIClient();
        String base_url = ac.URL;

        getInquiry(uId,"0","1");
        return mView;
    }

    private void getInquiry(String uId, String otp, String token) {
        String url = ac.URL+"/api/GetInquiry?CustomerID="+uId+"&OTP=0&token=1";

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String inquiryResponse = response.toString();

                if(inquiryResponse != null){
                    try {
                        JSONObject object = new JSONObject(inquiryResponse);
                        String msg= object.getString("message");
                        JSONArray array = object.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            CommonModel cm = new CommonModel();
                            JSONObject obj_inq = array.getJSONObject(i);
                            cm.inFullname = obj_inq.getString("FullName");
                            cm.inMobile = obj_inq.getString("MobileNo");
                            cm.inSendDate = obj_inq.getString("SendDate");

                            inquiryList.add(cm);
                        }
                        InquiryAdapter adapter = new InquiryAdapter(InquiryFragment.this,inquiryList);
                        rv_inquiryList.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                Log.e("TEST","Error :"+error);
            }
        }){
            protected JSONObject getParam(){

                return  null;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }
}