package com.ltg.ltgfresh.SharedPrefrences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    static public String ID = "id";
    static public String NAME = "name";
    static public String EMAIL = "email";
    static public String PHONE = "number";
    static public String ROLE = "role";
    static public String VALUE = "value";


    public SessionManager(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setValue(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getValue(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void setValueBoolean(String key , Boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();

    }

    public boolean getValueBoolean(String key){
       return sharedPreferences.getBoolean(key,false);

    }

    public void setslider(String key, Boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public Boolean getSlider(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public String getUserData(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void logout() {
        sharedPreferences.edit().clear().apply();
    }

}

