package com.symbiz.test.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.symbiz.test.Adapter.CallListAdapter;
import com.symbiz.test.Model.CommonModel;
import com.symbiz.test.R;
import com.symbiz.test.Retrofit.APIClient;
import com.symbiz.test.Retrofit.ApiInterface;
import com.symbiz.test.Retrofit.getWakinsData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalkInsFragment extends Fragment {
    View mView;
    String phNumber,uId;
    private static final int PHONE_LOG_PERMISSION= 1;
    RecyclerView rv_callLog;
    List<CommonModel> callList = new ArrayList<>();
    ApiInterface apiInterface;
    SharedPreferences sp;

    public WalkInsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_walk_ins, container, false);

        apiInterface = APIClient.getClient().create(ApiInterface.class);
        sp = getActivity().getSharedPreferences("userDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        uId = sp.getString("uId","");
        Log.e("TEST","Get User Id :"+uId);

        rv_callLog = mView.findViewById(R.id.rv_callLog);

        RecyclerView.LayoutManager rvDir = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_callLog.setLayoutManager(rvDir);

        /*if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG},PHONE_LOG_PERMISSION);

            getCallDetails();
        }else {
            getCallDetails();
        }*/

        getCallDetails();
        return mView;
    }



    private void getCallDetails() {

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getActivity().managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, null);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );

            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    dir = "REJECT";
                    break;
                default:
                    dir = "";
                    break;

            }
            if(dir.equalsIgnoreCase("MISSED") || dir.equalsIgnoreCase("INCOMING")){
           // if(dir.equalsIgnoreCase("MISSED")){

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String strDate = dateFormat.format(callDayTime);
                String comDate = df.format(callDayTime);

                sb.append( "\nPhone Number:--- "+phNumber +" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
                //sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
                CommonModel CM = new CommonModel();
                CM.callNumber = phNumber;
                CM.calldateTime = strDate;
               // CM.callDuetion = callDuration;
                CM.callType = dir;

                //getCallRecord(uId,phNumber,strDate,dir,"1");
                callList.add(CM);

            }

            CallListAdapter adapter = new CallListAdapter(WalkInsFragment.this,callList);
            rv_callLog.setAdapter(adapter);
        }
        managedCursor.close();
        Collections.reverse(callList);
    }

    public void  getCallRecord(String uId, String phNumber, String strDate, String dir, String i){
       Call<getWakinsData> call = apiInterface.getWakinsData(uId,phNumber,strDate,dir,i);
       call.enqueue(new Callback<getWakinsData>() {
           @Override
           public void onResponse(Call<getWakinsData> call, final Response<getWakinsData> response) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TEST","Message :"+response.body().getMessage());
                        Log.e("TEST","Status :"+response.body().getStatus());
                        if(response.body().getStatus().equalsIgnoreCase("200")){
                            Log.e("TEST","Get Message :"+response.body().getMessage());
                        }
                    }
                },30000);

           }

           @Override
           public void onFailure(Call<getWakinsData> call, Throwable t) {
                Log.e("TEST","Get Error :"+t.toString());
           }
       });
    }
}