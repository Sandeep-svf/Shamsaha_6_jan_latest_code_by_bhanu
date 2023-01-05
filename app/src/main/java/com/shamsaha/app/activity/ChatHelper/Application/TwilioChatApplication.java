package com.shamsaha.app.activity.ChatHelper.Application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import com.shamsaha.app.activity.ChatHelper.Message.ChatClientManager;
import com.shamsaha.app.utils.SharedPreferencesUtils;

import java.util.Locale;
import java.util.UUID;


public class TwilioChatApplication extends Application {
    private static TwilioChatApplication instance;
    private ChatClientManager basicClient;
    private SharedPreferences.Editor editor;
    private SharedPreferences settings;

    public static TwilioChatApplication get() {
        return instance;
    }

    public ChatClientManager getChatClientManager() {
        return this.basicClient;
    }

    public static final String TAG = "TwilioChat";
    public static final String NOTIFICATION_CHANNEL = "Shamsaha";

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        SharedPreferencesUtils.init(getApplicationContext());
        TwilioChatApplication.instance = this;
        basicClient = new ChatClientManager(getApplicationContext());

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();
        //Create the deviceId once
        setUniqueDeviceId(UUID.randomUUID().toString());
    }

    public static void setLocale(Activity activity, String languageCode, boolean isNeedToSave) {
        if (isNeedToSave) {
            setSelectedLanguage(languageCode);
        }
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public SharedPreferences getSettings() {
        return settings;
    }

    public static void setSelectedLanguage(String language) {
        SharedPreferences.Editor editor = get().getEditor();
        editor.putString("localeLanguage", language);
        editor.commit();
    }

    public static String getSelectedLanguage() {
        return get().getSettings().getString("localeLanguage", "");
    }

    public void setChatClient() {
        basicClient = new ChatClientManager(getApplicationContext());
    }

    public void setUniqueDeviceId(String uniqueDeviceId) {
        settings.edit().putString("uniqueDeviceId", uniqueDeviceId).apply();
    }


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel reminders = new NotificationChannel(
                    NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_HIGH
            );
            reminders.setDescription("Notifications from Server.");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(reminders);
            }
        }
    }


}
