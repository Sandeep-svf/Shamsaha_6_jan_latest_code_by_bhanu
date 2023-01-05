package com.shamsaha.app.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedpreferenceConfig {
    public static final String SHARED_PREFS = "sharedPrefs";
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedpreferenceConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

    }

    public boolean T_C_check() {
        boolean check = sharedPreferences.getBoolean("T&C_Status", false);
        return check;
    }

    public boolean savePassword() {
        boolean check = sharedPreferences.getBoolean("savePassword", false);
        return check;
    }

    public boolean biometricEnable() {
        boolean check = sharedPreferences.getBoolean("Biometric", false);
        return check;
    }

    public void setUserName(String firstName) {
        sharedPreferences.edit().putString("firstname", firstName).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString("firstname", "");
    }

    public void setUserPassword(String password) {
        sharedPreferences.edit().putString("password", password).apply();
    }

    public String getUserPassword() {
        return sharedPreferences.getString("password", "");
    }



    public boolean havePIN() {
        boolean check = sharedPreferences.getBoolean("PIN_Status", false);
        return check;
    }

    public  boolean checkIN(){
        boolean check = sharedPreferences.getBoolean("CheckIN",false);
        return check;
    }

    public String language() {
        String l = sharedPreferences.getString("MyLang", "");
        return l;
    }


}
