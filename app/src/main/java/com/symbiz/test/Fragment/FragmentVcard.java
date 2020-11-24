package com.symbiz.test.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.symbiz.test.Adapter.CardAdapter;
import com.symbiz.test.Model.SearchListModel;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentVcard extends Fragment {

    View mView;
    SharedPreferences sp;
    RecyclerView rv_vcardList;
    List<SearchListModel> cardList = new ArrayList();
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    String phone;
    public FragmentVcard() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_vcard, container, false);

        rv_vcardList = mView.findViewById(R.id.rv_vcardList);
        RecyclerView.LayoutManager recyMonials = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_vcardList.setLayoutManager(recyMonials);

        sp = getActivity().getSharedPreferences("userDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        String uId = sp.getString("uId","");
        Log.e("TEST","Get Share Prefrence :"+uId);
        APIClient ac = new APIClient();
        String base_url = ac.URL;
        loadVcardData(uId,base_url);

        return mView;
    }

    private void loadVcardData(String uId, String base_Url) {
        String url = base_Url+"/api/getcardinfo?CustomerID="+uId+"&token=1";

            final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            try {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String strResponse = response.toString();

                        if(strResponse != null){
                            try {
                                JSONObject object = new JSONObject(strResponse);
                                final SearchListModel SM = new SearchListModel();

                                SM.SMmsg = object.getString("message");
                                SM.SMstatus = object.getString("status");

                                if(object.getString("status").equalsIgnoreCase("200")){

                                    JSONObject obj = object.getJSONObject("result");
                                    SM.SMcID = obj.getString("CustomerID");
                                    SM.SMCardID = obj.getString("CardID");
                                    SM.SMcName = obj.getString("CompanyName");
                                    SM.SMFacebookLink = obj.getString("FacebookLink");
                                    SM.SMInstagramLink = obj.getString("InstagramLink");
                                    SM.SMYouTubeLink = obj.getString("YouTubeLink");
                                    SM.SMTwitterLink = obj.getString("TwitterLink");
                                    SM.SMLinkedInLink = obj.getString("LinkedInLink");
                                    SM.SMPinterestLink = obj.getString("PinterestLink");
                                    SM.SMemail = obj.getString("Email");
                                    SM.SMwhatsappNo = obj.getString("WhatsappNo");
                                    SM.SMCountryCode = obj.getString("CountryCode");
                                    SM.SMmobileNo = obj.getString("MobileNo");
                                    SM.SMwebsiteURL = obj.getString("WebsiteURL");
                                    SM.SMaddress = obj.getString("Address");
                                    SM.SMlogoPath = obj.getString("LogoPath");
                                    SM.SMAboutCompany = obj.getString("AboutCompany");
                                    SM.SMEstabishmentYear = obj.getString("EstabishmentYear");
                                    SM.SMBusinessType = obj.getString("BusinessType");
                                    SM.SMBgImage = obj.getString("BgImage");

                                    phone = SM.SMmobileNo;

                                    Glide.with(getActivity()).load(SM.SMBgImage)
                                            .into((ImageView) mView.findViewById(R.id.img_banner));

                                    Glide.with(getActivity()).load(SM.SMlogoPath)
                                            .into((CircularImageView) mView.findViewById(R.id.circular_image));

                                    ((TextView) mView.findViewById(R.id.txt_companyName)).setText(SM.SMcName);
                                    ((TextView) mView.findViewById(R.id.txt_email)).setText(SM.SMemail);
                                    ((TextView) mView.findViewById(R.id.txt_cCode)).setText(SM.SMCountryCode);
                                    ((TextView) mView.findViewById(R.id.txt_mobileNumber)).setText(SM.SMmobileNo);
                                    ((TextView) mView.findViewById(R.id.txt_website)).setText(SM.SMwebsiteURL);
                                    ((TextView) mView.findViewById(R.id.txt_address)).setText(SM.SMaddress);
                                    ((TextView) mView.findViewById(R.id.txt_cardAboutMe)).setText(SM.SMAboutCompany);
                                    ((TextView) mView.findViewById(R.id.txt_Estabishment_year)).setText(SM.SMEstabishmentYear);
                                    ((TextView) mView.findViewById(R.id.txt_Neture_Of)).setText(SM.SMBusinessType);


                                    ((ImageView) mView.findViewById(R.id.img_facebook)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.e("TEST","Click Facebook :"+SM.SMFacebookLink);
                                            if(SM.SMFacebookLink != null){
                                                Intent intent = new Intent(Intent.ACTION_SEND);
                                                intent.setType("text/plain");
                                                intent.putExtra(Intent.EXTRA_TEXT, SM.SMFacebookLink);
                                                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
                                                startActivity(Intent.createChooser(intent, "Share"));
                                            }else {
                                                toastIconSuccess();
                                            }
                                        }
                                    });

                                    ((ImageView) mView.findViewById(R.id.img_youtube)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(SM.SMYouTubeLink != null){
                                                Intent intent = new Intent(Intent.ACTION_SEND);
                                                intent.setType("text/plain");
                                                intent.putExtra(Intent.EXTRA_TEXT, SM.SMYouTubeLink);
                                                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
                                                startActivity(Intent.createChooser(intent, "Share"));
                                            }else {
                                                toastIconSuccess();
                                            }
                                        }
                                    });
                                    ((ImageView) mView.findViewById(R.id.img_instagram)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(SM.SMInstagramLink != null){
                                                Intent intent = new Intent(Intent.ACTION_SEND);
                                                intent.setType("text/plain");
                                                intent.putExtra(Intent.EXTRA_TEXT, SM.SMInstagramLink);
                                                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
                                                startActivity(Intent.createChooser(intent, "Share"));
                                            }else {
                                                toastIconSuccess();
                                            }
                                        }
                                    });

                                    ((ImageView) mView.findViewById(R.id.img_twitter)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(SM.SMTwitterLink != null){
                                                Intent intent = new Intent(Intent.ACTION_SEND);
                                                intent.setType("text/plain");
                                                intent.putExtra(Intent.EXTRA_TEXT, SM.SMTwitterLink);
                                                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
                                                startActivity(Intent.createChooser(intent, "Share"));
                                            }else {
                                                toastIconSuccess();
                                            }
                                        }
                                    });

                                    ((ImageView) mView.findViewById(R.id.img_whatsapp)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openWhatsApp(SM.SMwhatsappNo);
                                        }
                                    });
                                    ((ImageView) mView.findViewById(R.id.img_calling)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.e("TEST","Calling");
                                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                ActivityCompat.requestPermissions(getActivity(),
                                                        new String[]{Manifest.permission.CALL_PHONE},
                                                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                            } else {
                                                CallPhoneNumber();
                                            }
                                        }
                                    });

                                    JSONArray array = obj.getJSONArray("ServiceList");
                                    Log.e("TEST","Service List :"+array.length());
                                    for (int i = 0; i < array.length(); i++) {
                                        SearchListModel ssm = new SearchListModel();
                                        JSONObject arObj = array.getJSONObject(i);
                                        ssm.SMwebName = arObj.getString("Name");
                                        ssm.SMwebDec = arObj.getString("Description");
                                        ssm.SMwebPrice = arObj.getString("Price");
                                        ssm.SMwebCardId = arObj.getString("CardID");
                                        //SM.SMlogoPath = arObj.getString("logoPath");

                                        cardList.add(ssm);
                                    }

                                    CardAdapter cardadapter = new CardAdapter(getActivity(),cardList);
                                    rv_vcardList.setAdapter(cardadapter);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("TEST","Array Exception :"+e);
                            }

                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                        Log.e("TEST","Error :"+error);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    private void CallPhoneNumber() {
        if (phone != null && !TextUtils.isEmpty(phone)) {
            DialNumber(phone);
        } else {
            //SnakebarCustom.danger(getApplicationContext(), _thisFragment.getView(), "No Valid Phone Number found to Call", 1000);
        }
    }

    private void DialNumber(String phone) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            //SnakebarCustom.success(getApplication(), ContectProfile.this.getCurrentFocus(), "Calling" + "(" + phone + ")", 1000);
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
            //CallPhoneMessage = null;
        }
    }


    private void openWhatsApp(String number) {
        String smsNumber = number; // E164 format without '+' sign
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");
        if (getActivity().getIntent().resolveActivity(getActivity().getPackageManager()) == null) {
            //Toast.makeText(this, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(sendIntent);
    }

    private void toastIconSuccess() {

        Toast toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText("There is no any link");
        ((ImageView) custom_view.findViewById(R.id.icon)).setVisibility(View.GONE);
        //((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_500));

        toast.setView(custom_view);
        toast.show();
    }
}