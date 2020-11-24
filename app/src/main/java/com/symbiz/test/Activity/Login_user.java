package com.symbiz.test.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.symbiz.test.Common.CommomDataset;
import com.symbiz.test.MainActivity;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;
import com.symbiz.test.Retrofit.ApiInterface;
import com.symbiz.test.Retrofit.getLogin;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_user extends AppCompatActivity {
    EditText editTextEmail,editTextPassword;
    Button btn_login;
    LinearLayout progressBar;
    ApiInterface apiInterface;
    CheckBox chk_remember;
    SharedPreferences sp,spLogin,spGetData;
    APIClient ac;
    private static final int PHONE_LOG_PERMISSION= 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        apiInterface = APIClient.getClient().create(ApiInterface.class);

        ac = new APIClient();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btn_login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.ll_progress);
        chk_remember = findViewById(R.id.chk_remember);
        progressBar.setVisibility(View.GONE);

        ((TextView) findViewById(R.id.txt_signUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
            }
        });

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login_user.this,
                    new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG}, PHONE_LOG_PERMISSION);
        }

        spGetData = getSharedPreferences("loginDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = spGetData.edit();
        if(spGetData.getString("luser","").length() > 0 && spGetData.getString("lPass","").length() > 0){
            chk_remember.setChecked(true);
            editTextEmail.setText(spGetData.getString("luser",""));
            editTextPassword.setText(spGetData.getString("lPass",""));
        }



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextEmail.getText().toString().equals("")){
                    editTextEmail.setError("Invalid Username.");
                    editTextEmail.requestFocus();
                }
                if(editTextPassword.getText().toString().equals("")){
                    editTextPassword.setError("Invalid Password");
                    editTextPassword.requestFocus();
                }else {
                    /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);*/
                    LoginUser(editTextEmail.getText().toString(),editTextPassword.getText().toString());
                }


            }
        });

        ((TextView) findViewById(R.id.txt_signUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","SignUp User");
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void LoginUser(String uname, String pass) {
        progressBar.setVisibility(View.VISIBLE);
        Call<getLogin> call = apiInterface.getUserLogin(uname,pass,"1");
        call.enqueue(new Callback<getLogin>() {
            @Override
            public void onResponse(Call<getLogin> call, Response<getLogin> response) {
                Log.e("TEST","Login Response :"+response.body().getResult());
                if(response.body() != null){
                    progressBar.setVisibility(View.GONE);
                    if(response.body().getStatus().equalsIgnoreCase("200")){
                        Log.e("TEST","user Id :"+response.body().getUserID());

                        /*WebsiteFragment websiteFragment = new WebsiteFragment();
                        websiteFragment.loadSiteSettings(response.body().getUserID());*/

                        sp =getSharedPreferences("userDetail", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor =sp.edit();
                        editor.putString("uId",response.body().getUserID());
                        editor.putString("N",response.body().getName());
                        editor.putString("E",response.body().getEmail());
                        editor.apply();

                        loadSiteSettings(response.body().getUserID());

                        spLogin =getSharedPreferences("loginDetail", Context.MODE_PRIVATE);
                        SharedPreferences.Editor logineditor =spLogin.edit();
                        if(chk_remember.isChecked()){
                            logineditor.putString("luser",editTextEmail.getText().toString());
                            logineditor.putString("lPass",editTextPassword.getText().toString());
                            logineditor.apply();
                        }else{
                            logineditor.putString("luser","");
                            logineditor.putString("lPass","");
                            logineditor.apply();
                        }

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }else if(response.body().getStatus().equalsIgnoreCase("400")){

                        toastIconSuccess();
                    }
                }
            }

            @Override
            public void onFailure(Call<getLogin> call, Throwable t) {
                Log.e("TEST","Login : "+t.getMessage());
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    public void loadSiteSettings(String uId) {
        String faq_url = ac.URL+"/api/web/getsitesetting?CustomerID="+uId+"&token=1";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, faq_url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String getSiteSettingResponse = response.toString();
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


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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

    private void toastIconSuccess() {

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText("Invalid UserName and Password");
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_500));

        toast.setView(custom_view);
        toast.show();
    }
}