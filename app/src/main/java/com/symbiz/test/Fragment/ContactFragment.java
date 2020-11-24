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

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.symbiz.test.Common.CommomDataset;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.Model.SearchListModel;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ContactFragment extends Fragment {

    View mView;
    SharedPreferences sp;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    String phone;
    EditText edit_fullName,edit_email,edit_message;
    APIClient ac;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_contact, container, false);

        edit_fullName = mView.findViewById(R.id.edit_fullName);
        edit_email = mView.findViewById(R.id.edit_email);
        edit_message = mView.findViewById(R.id.edit_message);

        sp = getActivity().getSharedPreferences("userDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        final String uId = sp.getString("uId","");
        Log.e("TEST","Get Share Prefrence :"+uId);
        ac = new APIClient();
        String base_url = ac.URL;
        loadVcardGallaryData(uId,base_url);
        ((Button) mView.findViewById(R.id.btn_contectsubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInquiry(uId,edit_fullName.getText().toString(),edit_email.getText().toString(),edit_message.getText().toString());
                toastIconSuccess(0);
            }
        });
        loadSiteSettings(uId);
        return mView;
    }

    private void addInquiry(String uId, String fullName, String email, String message) {

        String addInqUrl = ac.URL+"/api/sendmail?CustomerID="+uId+"&name="+fullName+"&email="+email+"&message="+message+"&token=1";
        Log.e("TEST","Add Inquiry :"+addInqUrl);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, addInqUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String sendResponse = response.toString();
                Log.e("TEST","Send Mail :"+sendResponse);
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
        request.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    private void loadVcardGallaryData(String uId, String base_Url) {
        String url = base_Url+"/api/getcardinfo?CustomerID="+uId+"&token=1";

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String strResponse = response.toString();
                    Log.e("TEST","Get New Response :"+strResponse);
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
                                SM.SMBgImage = obj.getString("BgImage");

                                phone = SM.SMmobileNo;

                                Glide.with(getActivity()).load(SM.SMBgImage)
                                        .into((ImageView) mView.findViewById(R.id.img_contactbanner));
                                Glide.with(getActivity()).load(SM.SMlogoPath)
                                        .into((CircularImageView) mView.findViewById(R.id.image_contact));

                                ((TextView) mView.findViewById(R.id.txt_contactCompanyName)).setText(SM.SMcName);


                                ((ImageView) mView.findViewById(R.id.img_facebook)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.e("TEST","Click Facebook"+SM.SMFacebookLink);
                                        if(SM.SMFacebookLink != null){
                                            Intent intent = new Intent(Intent.ACTION_SEND);
                                            intent.setType("text/plain");
                                            intent.putExtra(Intent.EXTRA_TEXT, SM.SMFacebookLink);
                                            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
                                            startActivity(Intent.createChooser(intent, "Share"));
                                            //  t2.setMovementMethod(LinkMovementMethod.getInstance());
                                        }else {
                                            toastIconSuccess(1);
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
                                            toastIconSuccess(1);
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
                                            toastIconSuccess(1);
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
                                            toastIconSuccess(1);
                                        }
                                    }
                                });

                                ((ImageView) mView.findViewById(R.id.img_contactcalling)).setOnClickListener(new View.OnClickListener() {
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

                                ((ImageView) mView.findViewById(R.id.img_whatsapp)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openWhatsApp(SM.SMwhatsappNo);
                                    }
                                });

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

    private void toastIconSuccess(int data) {

        Toast toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        if(data == 1){
            ((TextView) custom_view.findViewById(R.id.message)).setText("There is no any link");
            ((ImageView) custom_view.findViewById(R.id.icon)).setVisibility(View.GONE);
            //((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_500));
        }else if(data == 0){
            ((TextView) custom_view.findViewById(R.id.message)).setText("Sent Inquiry");
            ((ImageView) custom_view.findViewById(R.id.icon)).setVisibility(View.GONE);
            //((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));
        }


        toast.setView(custom_view);
        toast.show();
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

    public void loadSiteSettings(String uId) {
        String faq_url = ac.URL+"/api/web/getsitesetting?CustomerID="+uId+"&token=1";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, faq_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String getSiteSettingResponse = response.toString();
                Log.e("TEST","SiteSetting Response :"+getSiteSettingResponse);

                if(getSiteSettingResponse != null){
                    try {
                        JSONObject jsonObject = new JSONObject(getSiteSettingResponse);
                        CommonModel cm = new CommonModel();

                        cm.ssId = jsonObject.getString("ID");
                        cm.cId = jsonObject.getString("CustomerID");

                        CommomDataset.companyName = cm.ssCompanyName;

                        cm.ssGoolgeMap = jsonObject.getString("GoolgeMap");
                        String[] values = cm.ssGoolgeMap.split(",");
                        final String lat = values[0];
                        final String longitude = values[1];
                        ((ImageView) mView.findViewById(R.id.contact_map)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                findGmap(lat,longitude);
                            }
                        });

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

    private void findGmap(String lat, String longitude) {
        Log.e("Test","LLLL :"+lat);
        Log.e("Test","LOng :"+longitude);
        double ll = Double.parseDouble(lat);
        double lt = Double.parseDouble(longitude);
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", ll, lt);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        getActivity().startActivity(intent);
    }


}