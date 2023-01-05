package com.shamsaha.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Global SharedPreferences for the app
 * in order to save user session.
 */

public class SharedPreferencesUtils {

    private static SharedPreferences sharedPreferences;

    private SharedPreferencesUtils() {
    }

    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
    }

    public static void writeToSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static void clearSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static String readFromSharedPreferences(String key) {
        return sharedPreferences.getString(key, null);
    }

    public static void saveAccessToken(String accessToken) {
        writeToSharedPreferences(Constants.ACCESS_TOKEN, accessToken);
    }

    public static String getAccessToken() {
        return readFromSharedPreferences(Constants.ACCESS_TOKEN);
    }

    public static String getFCMToken() {
        return readFromSharedPreferences(Constants.FCM_TOKEN);
    }

    public static void saveFCMToken(String fcmToken) {
        writeToSharedPreferences(Constants.FCM_TOKEN, fcmToken);
    }

    public static Boolean getIsLoggedIn() {
        return sharedPreferences.getBoolean(Constants.LOGGED_IN, false);
    }

    public static void saveIsLoggedIn(Boolean isLoggedIn) {
        sharedPreferences.edit().putBoolean(Constants.LOGGED_IN, isLoggedIn).apply();
    }

    public static void saveVolunteerID(String volunteerId){
        sharedPreferences.edit().putString(Constants.VOLUNTEER_ID,volunteerId).apply();
    }

    public static void saveVolunteerName(String volunteerName){
        sharedPreferences.edit().putString(Constants.VOLUNTEER_NAME,volunteerName).apply();
    }

    public static void saveVolunteerProfile(String volunteerProfile){
        sharedPreferences.edit().putString(Constants.VOLUNTEER_PROFILE,volunteerProfile).apply();
    }

    public static  String getVolunteerID(){
        return readFromSharedPreferences(Constants.VOLUNTEER_ID);
    }
    public static  String getVolunteerName(){
        return readFromSharedPreferences(Constants.VOLUNTEER_NAME);
    }
    public static  String getVolunteerProfile(){
        return readFromSharedPreferences(Constants.VOLUNTEER_PROFILE);
    }

    public static void saveIdentityToInviteForVideoCall(String targetIdentity){
        sharedPreferences.edit().putString(Constants.INVITED_ID,targetIdentity).apply();
    }

    public static String getIdentityToInviteForVideoCall(){
        return readFromSharedPreferences(Constants.INVITED_ID);
    }

    public static String getVolunteerLanguage(){
        return readFromSharedPreferences(Constants.VOLUNTEER_LANGUAGE);
    }

    public static void saveVolunteerLanguage(String language){
        sharedPreferences.edit().putString(Constants.VOLUNTEER_LANGUAGE,language).apply();
    }
}
