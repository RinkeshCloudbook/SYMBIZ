package com.symbiz.test.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InquryResult {
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("SendDate")
    @Expose
    private String sendDate;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
