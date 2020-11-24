package com.symbiz.test.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/api/login?")
    Call<getLogin> getUserLogin(@Query("username") String uname, @Query("password") String pass, @Query("token") String token);

    @GET("/api/getcountry?")
    Call<getCountry> getCountry(@Query("token") String token);

    @GET("/api/register?")
    Call<getRegister> getuseRegister(@Query("FirstName") String fName
            , @Query("LastName") String lName
            ,@Query("Password") String pass
            , @Query("CountryID") String countryId
            , @Query("Email") String email
            , @Query("MobileNo") String phone
            , @Query("Token") String token
    );

    @GET("/api/CheckOTP?")
    Call<getOTP> getcheckOTP(@Query("UserID") String userId, @Query("OTP") String otp, @Query("token") String token);

    @GET("/api/GetInquiry?")
    Call<getInquiry> getInquiryData(@Query("UserID") String userId, @Query("OTP") String otp, @Query("token") String token);

    @GET("/api/SendMessage?")
    Call<getSMS> sendMessage(@Query("UserID") String userId, @Query("mobileno") String mobile, @Query("message") String sms, @Query("token") String token);


    @GET("/api/web/getdirectorysearch?")
    Call<getDirectoryExapmle> getDirectory(@Query("CompanyName") String cName
                                            , @Query("AreaName") String areaName
                                            , @Query("CityID") String cId
                                            , @Query("StateID") String sId
                                            , @Query("CategoryName") String catName
                                            , @Query("token") String token);
    @GET("/api/InsertWakinsData?")
    Call<getWakinsData> getWakinsData(@Query("UserID") String userId, @Query("MobileNo") String mobileNo, @Query("CallDate") String callDate, @Query("CallType") String CallType, @Query("token") String token);
}
