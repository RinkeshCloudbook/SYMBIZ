package com.symbiz.test.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class getDirectoryResult {
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("WebsiteURL")
    @Expose
    private String websiteURL;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("IsMember")
    @Expose
    private Boolean isMember;
    @SerializedName("CountryCode")
    @Expose
    private String countryCode;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("LogoPath")
    @Expose
    private String logoPath;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("AreaName")
    @Expose
    private String areaName;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("CityName")
    @Expose
    private String cityName;
    @SerializedName("StateName")
    @Expose
    private String stateName;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsMember() {
        return isMember;
    }

    public void setIsMember(Boolean isMember) {
        this.isMember = isMember;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

}
