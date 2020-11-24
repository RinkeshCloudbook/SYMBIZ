package com.symbiz.test.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;
import com.symbiz.test.Retrofit.ApiInterface;
import com.symbiz.test.Retrofit.getCountry;
import com.symbiz.test.Retrofit.getRegister;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    ApiInterface apiInterface;
    EditText edt_fname,edt_lname,edt_pass,edt_email,edt_phone;
    Button btn_signUp;
    LinearLayout lyt_progress;
    RecyclerView recycler_view;
    AutoCompleteTextView edt_country;
    List<String> countryList = new ArrayList<>();

   // List<SearchListModel> countryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        apiInterface = APIClient.getClient().create(ApiInterface.class);

        edt_fname = findViewById(R.id.edt_fname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_pass = findViewById(R.id.edt_pass);
        edt_country = findViewById(R.id.edt_country);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
        btn_signUp = findViewById(R.id.btn_signUp);

       // recycler_view = findViewById(R.id.recycler_view);
        lyt_progress = findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.GONE);

        loadCountry();
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Submit");
                if(edt_fname.getText().toString().equals("")){
                    edt_fname.setError("Invalid Username.");
                    edt_fname.requestFocus();
                }
                if(edt_lname.getText().toString().equals("")){
                    edt_lname.setError("Invalid Username.");
                    edt_lname.requestFocus();
                }
                if(edt_pass.getText().toString().equals("")){
                    edt_pass.setError("Invalid Username.");
                    edt_pass.requestFocus();
                }
                if(edt_country.getText().toString().equals("")){
                    edt_country.setError("Invalid Username.");
                    edt_country.requestFocus();
                }
                if(edt_email.getText().toString().equals("")){
                    edt_email.setError("Invalid Username.");
                    edt_email.requestFocus();
                }
                if(edt_phone.getText().toString().equals("")){
                    edt_phone.setError("Invalid Username.");
                    edt_phone.requestFocus();
                }else {
                    userRegister(edt_fname.getText().toString(),edt_lname.getText().toString()
                            ,edt_pass.getText().toString(),edt_country.getText().toString()
                            ,edt_email.getText().toString(),edt_phone.getText().toString());
                }

            }
        });
    }


    private void userRegister(final String fName, final String lName, String pass, String country, String email, String phone) {
        lyt_progress.setVisibility(View.VISIBLE);
        Call<getRegister> call = apiInterface.getuseRegister(fName,lName,pass,"101",email,phone,"1");
        call.enqueue(new Callback<getRegister>() {
            @Override
            public void onResponse(Call<getRegister> call, Response<getRegister> response) {
                if(response.body() != null){
                    lyt_progress.setVisibility(View.GONE);
                    if(response.body().getStatus().equalsIgnoreCase("200")){
                        toastIconSuccess(200);
                        Intent intent = new Intent(getApplicationContext(),PinCode.class);
                        intent.putExtra("C",response.body().getResult());
                        intent.putExtra("FN",fName);
                        intent.putExtra("LN",lName);
                        intent.putExtra("UID",response.body().getResult());
                        startActivity(intent);

                    }else if(response.body().getStatus().equalsIgnoreCase("400")){
                        toastIconSuccess(400);
                    }
                }
            }

            @Override
            public void onFailure(Call<getRegister> call, Throwable t) {
                Log.e("TEST","Register failur :"+t.getMessage());
                lyt_progress.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadCountry() {
        lyt_progress.setVisibility(View.VISIBLE);
        Call<getCountry> call = apiInterface.getCountry("1");
        call.enqueue(new Callback<getCountry>() {
            @Override
            public void onResponse(Call<getCountry> call, final Response<getCountry> response) {
                if(response.body() != null){
                    countryList.clear();

                    lyt_progress.setVisibility(View.GONE);
                    for (int i = 0; i < response.body().getResult().size(); i++) {
                        countryList.add(response.body().getResult().get(i).getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUp.this,android.R.layout.simple_list_item_1, countryList);
                    edt_country.setThreshold(1);
                    edt_country.setAdapter(adapter);
                    edt_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            for (int i = 0; i < response.body().getResult().size(); i++) {
                                if(parent.getItemAtPosition(position).equals(response.body().getResult().get(i).getName())){
                                    Log.e("TESt","Item :"+response.body().getResult().get(position).getId());
                                    Log.e("TESt","Country Code :"+response.body().getResult().get(position).getCode());
                                    Log.e("TESt","Country Name :"+response.body().getResult().get(position).getName());
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<getCountry> call, Throwable t) {
                lyt_progress.setVisibility(View.VISIBLE);
            }
        });
    }

    private void toastIconSuccess(int code) {

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        if(code == 200){
            ((TextView) custom_view.findViewById(R.id.message)).setText("OTP sent to your register mobile no.");
            ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));
        }else if(code == 400){
            ((TextView) custom_view.findViewById(R.id.message)).setText("User already register...");
            ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_500));
        }
        //Register successfully


        toast.setView(custom_view);
        toast.show();
    }
}