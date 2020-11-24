package com.symbiz.test.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.symbiz.test.Adapter.BannerAdapter;
import com.symbiz.test.Adapter.FaqAdapter;
import com.symbiz.test.Adapter.PartnerAdapter;
import com.symbiz.test.Adapter.ProductAdapter;
import com.symbiz.test.Adapter.TestimonialAdapter;
import com.symbiz.test.Adapter.WebSearviceAdapter;
import com.symbiz.test.Common.CommomDataset;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;
import com.symbiz.test.utils.ViewAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WebsiteFragment extends Fragment {

    View mView;
    private static final int MAX_STEP = 4;
    private ViewFlipper truitonFlipper;
    private float initialX;
    private ViewPager viewPager;
    private Button btnNext;
    private MyViewPagerAdapter myViewPagerAdapter;
    LinearLayout lyt_expand_info,lyt_expand_info_two,lyt_expand_info_three,lyt_expand_info_four;

    private SharedPreferences sp;
    List<CommonModel> serviceList = new ArrayList<>();
    List<CommonModel> bannerList = new ArrayList<>();
    List<CommonModel> faqList = new ArrayList<>();
    List<CommonModel> PartneList = new ArrayList<>();
    List<CommonModel> testimonialList = new ArrayList<>();
    List<CommonModel> productList = new ArrayList<>();

    RecyclerView rv_webService,rv_webBanner,rv_webFaq,rv_webPartner,rv_webMonials,rv_webProduct;
    APIClient ac;

    private String about_title_array[] = {
            "Ready to Travel",
            "Pick the Ticket",
            "Flight to Destination",
            "Enjoy Holiday"
    };

    private int about_images_array[] = {
            R.drawable.backimage,
            R.drawable.backimage,
            R.drawable.backimage,
            R.drawable.backimage
    };


    public WebsiteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_website, container, false);

        viewPager = mView.findViewById(R.id.view_pager);
        rv_webService = mView.findViewById(R.id.rv_webService);
        rv_webBanner = mView.findViewById(R.id.rv_webBanner);
        rv_webFaq = mView.findViewById(R.id.rv_webFaq);
        rv_webPartner = mView.findViewById(R.id.rv_webPartner);
        rv_webMonials = mView.findViewById(R.id.rv_webMonials);
        rv_webProduct = mView.findViewById(R.id.rv_webProduct);

        bottomProgressDots(0);
        RecyclerView.LayoutManager recyce = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_webService.setLayoutManager(recyce);
        RecyclerView.LayoutManager recyBanner = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_webBanner.setLayoutManager(recyBanner);
        RecyclerView.LayoutManager recyfaq = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_webFaq.setLayoutManager(recyfaq);
        RecyclerView.LayoutManager recyPartner = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_webPartner.setLayoutManager(recyPartner);
        RecyclerView.LayoutManager recyMonials = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_webMonials.setLayoutManager(recyMonials);
        RecyclerView.LayoutManager recyProduct = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_webProduct.setLayoutManager(recyProduct);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(0, 0, 0, 0);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen._60sdp));
        viewPager.setOffscreenPageLimit(4);

       /* bt_toggle_info_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSectionInfoOne(bt_toggle_info_one);
            }
        });
*/

        sp = getActivity().getSharedPreferences("userDetail",Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        ac = new APIClient();

        loadService(sp.getString("uId",""),ac.URL);
        loadBanner(sp.getString("uId",""));
        loadFaq(sp.getString("uId",""));
        loadPartner(sp.getString("uId",""));
        loadTestimonia(sp.getString("uId",""));
        loadSiteSettings(sp.getString("uId",""));
        loadProduct(sp.getString("uId",""));

        return mView;
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
                        cm.ssColorID = jsonObject.getString("ColorID");
                        cm.ssCompanyName = jsonObject.getString("CompanyName");
                        Log.e("TEST","Company Name :"+cm.ssCompanyName);
                        CommomDataset.companyName = cm.ssCompanyName;
                        cm.ssAddress = jsonObject.getString("Address");
                        cm.ssAboutUs = jsonObject.getString("AboutUs");
                        cm.ssPhoneNo = jsonObject.getString("PhoneNo");
                        cm.ssEmailID = jsonObject.getString("EmailID");
                        cm.ssSlug = jsonObject.getString("Slug");
                        cm.ssLogoPath = jsonObject.getString("LogoPath");
                        cm.ssFeviconPath = jsonObject.getString("FeviconPath");
                        cm.ssGoolgeMap = jsonObject.getString("GoolgeMap");
                        cm.ssFacebookLink = jsonObject.getString("FacebookLink");
                        cm.ssTwitterLink = jsonObject.getString("TwitterLink");
                        cm.ssInstagramLink = jsonObject.getString("InstagramLink");
                        cm.ssPinterestLink = jsonObject.getString("PinterestLink");
                        cm.ssLinkedInLink = jsonObject.getString("LinkedInLink");
                        cm.ssYouTubeLink = jsonObject.getString("YouTubeLink");

                        Log.e("TEST","Google Map :"+cm.ssGoolgeMap);
                        String[] values = cm.ssGoolgeMap.split(",");
                        final String lat = values[0];
                        final String longitude = values[1];

                        Log.e("TEST","Latitude :"+lat);
                        Log.e("TEST","Longitude :"+longitude);
                        ((ImageView) mView.findViewById(R.id.map)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                findGmap(lat,longitude);
                            }
                        });
                        APIClient ap = new APIClient();

                        Glide.with(getActivity()).load(ap.URL+cm.ssLogoPath).into(((ImageView) mView.findViewById(R.id.img_aboutLogo)));
                        ((TextView) mView.findViewById(R.id.txt_aboutCompanyName)).setText(cm.ssCompanyName);
                        ((TextView) mView.findViewById(R.id.txt_aboutUs)).setText(Html.fromHtml(cm.ssAboutUs));
                        ((TextView) mView.findViewById(R.id.txt_about_address)).setText(cm.ssAddress);
                        ((TextView) mView.findViewById(R.id.txt_about_phone)).setText(cm.ssPhoneNo);
                        ((TextView) mView.findViewById(R.id.txt_about_email)).setText(cm.ssEmailID);
                        ((TextView) mView.findViewById(R.id.txt_aboutUsData)).setText(Html.fromHtml(cm.ssAboutUs));


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

    private void loadProduct(String uId) {
        String faq_url = ac.URL+"/api/web/getsiteproduct?CustomerID="+uId+"&token=1";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, faq_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String getProductResponse = response.toString();

                if(getProductResponse != null){
                    try {
                        JSONArray array = new JSONArray(getProductResponse);
                        for (int i = 0; i < array.length(); i++) {
                            CommonModel cm = new CommonModel();
                            JSONObject jsonObject = array.getJSONObject(i);

                            cm.proId = jsonObject.getString("ID");
                            cm.cId = jsonObject.getString("CustomerID");
                            cm.proName = jsonObject.getString("Name");
                            cm.proDec = jsonObject.getString("Description");
                            cm.proPhotoPath = jsonObject.getString("PhotoPath");
                            cm.proIsertedData = jsonObject.getString("InsertedDate");

                            productList.add(cm);
                        }

                        ProductAdapter productAdapter = new ProductAdapter(WebsiteFragment.this,productList);
                        rv_webProduct.setAdapter(productAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
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
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

    private void loadTestimonia(String uId) {
        String faq_url = ac.URL+"/api/web/getsitetestimonial?CustomerID="+uId+"&token=1";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, faq_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String getBannerResponse = response.toString();

                if(getBannerResponse != null){
                    try {
                        JSONArray array = new JSONArray(getBannerResponse);
                        for (int i = 0; i < array.length(); i++) {
                            CommonModel cm = new CommonModel();
                            JSONObject jsonObject = array.getJSONObject(i);

                            cm.tId = jsonObject.getString("ID");
                            cm.cId = jsonObject.getString("CustomerID");
                            cm.tName = jsonObject.getString("Name");
                            cm.tMessage = jsonObject.getString("Message");
                            cm.tPhotoPath = jsonObject.getString("PhotoPath");
                            cm.tStar = jsonObject.getString("Star");
                            cm.tIsertedData = jsonObject.getString("InsertedDate");

                            testimonialList.add(cm);
                        }

                        TestimonialAdapter testimonialAdapter = new TestimonialAdapter(WebsiteFragment.this,testimonialList);
                        rv_webMonials.setAdapter(testimonialAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
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
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

    private void loadPartner(String uId) {
        String faq_url = ac.URL+"/api/web/getsitepartner?CustomerID="+uId+"&token=1";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, faq_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String getBannerResponse = response.toString();
                Log.e("TEST","Get Faq. Response :"+getBannerResponse);
                if(getBannerResponse != null){
                    try {
                        JSONArray array = new JSONArray(getBannerResponse);
                        for (int i = 0; i < array.length(); i++) {
                            CommonModel cm = new CommonModel();
                            JSONObject jsonObject = array.getJSONObject(i);

                            cm.pId = jsonObject.getString("ID");
                            cm.cId = jsonObject.getString("CustomerID");
                            cm.pName = jsonObject.getString("Name");
                            cm.pLogopath = jsonObject.getString("LogoPath");
                            cm.pWebLink = jsonObject.getString("InsertedDate");
                            cm.pIsertedData = jsonObject.getString("InsertedDate");

                            PartneList.add(cm);
                        }

                        PartnerAdapter partneListAdapter = new PartnerAdapter(WebsiteFragment.this,PartneList);
                        rv_webPartner.setAdapter(partneListAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
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
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

    private void loadFaq(String uId) {
            String faq_url = ac.URL+"/api/web/getsitefaqs?CustomerID="+uId+"&token=1";
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, faq_url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    String getBannerResponse = response.toString();
                    Log.e("TEST","Get Faq. Response :"+getBannerResponse);
                    if(getBannerResponse != null){
                        try {
                            JSONArray array = new JSONArray(getBannerResponse);
                            for (int i = 0; i < array.length(); i++) {
                                CommonModel cm = new CommonModel();
                                JSONObject jsonObject = array.getJSONObject(i);

                                cm.fId = jsonObject.getString("ID");
                                cm.cId = jsonObject.getString("CustomerID");
                                cm.fTitle = jsonObject.getString("Title");
                                cm.fDec = jsonObject.getString("Description");
                                cm.fIsertedData = jsonObject.getString("InsertedDate");

                                faqList.add(cm);
                            }

                            FaqAdapter bannerAdapter = new FaqAdapter(WebsiteFragment.this,faqList);
                            rv_webFaq.setAdapter(bannerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
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
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonArrayRequest);
    }

    private void loadBanner(String uId) {
        String banner_url = ac.URL+"/api/web/getsitebanner?CustomerID="+uId+"&token=1";
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, banner_url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    String getBannerResponse = response.toString();
                    Log.e("TEST","Get Banner Response :"+getBannerResponse);
                    if(getBannerResponse != null){
                        try {
                            JSONArray array = new JSONArray(getBannerResponse);
                            for (int i = 0; i < array.length(); i++) {
                                CommonModel cm = new CommonModel();
                                JSONObject jsonObject = array.getJSONObject(i);

                                cm.bId = jsonObject.getString("ID");
                                cm.cId = jsonObject.getString("CustomerID");
                                cm.bTitle = jsonObject.getString("Title");
                                cm.bSubTitle = jsonObject.getString("SubTitle");
                                cm.bPhotoPath = jsonObject.getString("PhotoPath");
                                cm.bIsertedData = jsonObject.getString("InsertedDate");

                                bannerList.add(cm);
                            }

                            BannerAdapter bannerAdapter = new BannerAdapter(WebsiteFragment.this,bannerList);
                            rv_webBanner.setAdapter(bannerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
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
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonArrayRequest);
    }

    private void loadService(String uId, String baseUrl) {
        String url = baseUrl+"/api/web/getsiteservice?CustomerID="+uId+"&token=1";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String getResponse = response.toString();
                Log.e("TEST","GetResponse :"+getResponse);
                if(getResponse != null){
                    try {
                        JSONArray array = new JSONArray(getResponse);

                       // serviceList.add(getResponse);
                        Log.e("TEST","Service Lenght :"+array.length());
                        for (int i = 0; i < array.length(); i++) {
                            CommonModel cm = new CommonModel();
                            JSONObject jsonObject = array.getJSONObject(i);

                            cm.sId = jsonObject.getString("ID");
                            cm.cId = jsonObject.getString("CustomerID");
                            cm.sTitle = jsonObject.getString("Title");
                            cm.sDec = jsonObject.getString("Description");
                            cm.sIconPath = jsonObject.getString("IconPath");
                            cm.sIsertedData = jsonObject.getString("InsertedDate");

                            serviceList.add(cm);
                        }

                        WebSearviceAdapter webSearviceAdapter = new WebSearviceAdapter(WebsiteFragment.this,serviceList);
                        rv_webService.setAdapter(webSearviceAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
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
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

    private void toggleSectionInfoTwo(View view) {
        boolean show = toggleArrow(view);
        if(show){
            ViewAnimation.expand(lyt_expand_info_two, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    //Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info);
                }
            });
        }else {
            ViewAnimation.collapse(lyt_expand_info_two);
        }
    }

    private void toggleSectionInfoOne(View view) {
        boolean show = toggleArrow(view);
        if(show){
            ViewAnimation.expand(lyt_expand_info, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    //Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info);
                }
            });
        }else {
            ViewAnimation.collapse(lyt_expand_info);
        }
    }

    private void toggleSectionInfoThree(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    //Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_info);
        }
    }

    private void toggleSectionInfoFour(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    //Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_info);
        }
    }

    private boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = mView.findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getActivity());
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.light_green_600), PorterDuff.Mode.SRC_IN);
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            bottomProgressDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_card_wizard, container, false);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(about_images_array[position]);

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return about_title_array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}