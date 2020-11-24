package com.symbiz.test.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;
import com.symbiz.test.Retrofit.ApiInterface;
import com.symbiz.test.Retrofit.getOTP;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinCode extends AppCompatActivity {

    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);

        apiInterface = APIClient.getClient().create(ApiInterface.class);

        Intent intent = getIntent();
        String getId = intent.getStringExtra("C");
        String fn = intent.getStringExtra("FN");
        String ln = intent.getStringExtra("LN");
        final String userId = intent.getStringExtra("UID");

        Log.e("TEST","PinCode User Id :"+userId);
        String cahrFirst = fn.substring(0,1);
        String cahrsecond = ln.substring(0,1);

        ((TextView) findViewById(R.id.txt_headerName)).setText(fn+" "+ln);
        ((TextView) findViewById(R.id.txt_iconName)).setText(cahrFirst+cahrsecond);

        Log.e("TEST","Get Intent Id Result :"+getId);
        ((EditText) findViewById(R.id.edt_pincode)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String otp = s.toString();
                if(s.length() == 4){
                    /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);*/
                    checkOTP(otp,userId);
                }
            }
        });
    }

    private void checkOTP(String otp, String uId) {
        Log.e("TEST","Get OTP :"+otp);
        Log.e("TEST","Get UserId :"+uId);
        Call<getOTP> call = apiInterface.getcheckOTP(uId,otp,"1");
        call.enqueue(new Callback<getOTP>() {
            @Override
            public void onResponse(Call<getOTP> call, Response<getOTP> response) {
                if(response.body() != null){
                    Log.e("TEST","PinCode Get Status :"+response.body().getStatus());
                    if(response.body().getStatus().equalsIgnoreCase("200")){
                        toastIconSuccess();
                        Intent intent = new Intent(getApplicationContext(), Login_user.class);
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onFailure(Call<getOTP> call, Throwable t) {

            }
        });
    }

    private void toastIconSuccess() {

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);

            ((TextView) custom_view.findViewById(R.id.message)).setText("User register successfully...");
            ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

            toast.setView(custom_view);
            toast.show();
    }
}