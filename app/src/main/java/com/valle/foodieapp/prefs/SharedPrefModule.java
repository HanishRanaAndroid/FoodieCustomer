package com.valle.foodieapp.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefModule {

    private SharedPreferences prefs;

    private Context context;


    public SharedPrefModule(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUserLoginResponse(String response) {
        prefs.edit().putString("UserLoginResponse", response).commit();
    }


    public String getUserId() {
        return prefs.getString("userID", "");
    }

    public void setUserId(String userID) {
        prefs.edit().putString("userID", userID).commit();
    }

    public String getUserRememberData() {
        return prefs.getString("setUserRemember", "");
    }

    public void setUserRemember(String response) {
        prefs.edit().putString("setUserRemember", response).commit();
    }

    public String getLocationPrefrences() {
        return prefs.getString("location", "");
    }

    public void setLocationPrefrence(String location) {
        prefs.edit().putString("location", location).commit();
    }

    public void setLocationCity(String location) {
        prefs.edit().putString("city", location).commit();
    }

    public void setLocationLattitude(String Lattitude) {
        prefs.edit().putString("Lattitude", Lattitude).commit();
    }

    public void setLocationLongitude(String Longitude) {
        prefs.edit().putString("Longitude", Longitude).commit();
    }

    public void setNotification(String notification) {
        prefs.edit().putString("notification", notification).commit();
    }

    public void setCustomerSupportNumber(String number){
        prefs.edit().putString("CustomerSupportNumber", number).commit();
    }

    public String getCustomerSupportNumber() {
        return prefs.getString("CustomerSupportNumber", "");
    }

    public String getlocationAddress() {
        return prefs.getString("location", "");
    }

    public String getnotification() {
        return prefs.getString("notification", "");
    }

    public String getLocationCity() {
        return prefs.getString("city", "");
    }

    public String getLocationLattitude() {
        return prefs.getString("Lattitude", "");
    }

    public String getLocationLongitude() {
        return prefs.getString("Longitude", "");
    }

    public String getUserLoginResponseData() {
        return prefs.getString("UserLoginResponse", "");
    }

}