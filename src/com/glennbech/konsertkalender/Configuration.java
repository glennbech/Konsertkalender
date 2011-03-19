package com.glennbech.konsertkalender;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class to handle Configruation
 *
 * @author Glenn Bech
 */
public class Configuration {

    private static final String PREFERENCE_FILE_NAME = "config";
    public static final String KEY_ARE_NOTIFICATIONS_ENABLED = "NOTIFYNEW";
    public static final String NOTIFICATION_DELIVERY_HOUR = "NOTIFYDELIVERY";

    private SharedPreferences preferences;


    public Configuration(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

    }

    /**
     * @return
     */
    public boolean notificationsEnabled() {
        return Boolean.valueOf(preferences.getString(KEY_ARE_NOTIFICATIONS_ENABLED, null));
    }


    /**
     * @return
     */
    public String getUpdateHour() {

        String hourString = preferences.getString(NOTIFICATION_DELIVERY_HOUR, "-1");
        return hourString;

    }


    /**
     * Turns notifications on and off
     *
     * @param enabled
     */
    public void setNotificationsEnabled(boolean enabled) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(String.valueOf(KEY_ARE_NOTIFICATIONS_ENABLED), null);
        editor.commit();
    }


    /**
     * Sets the hour to notify
     */
    public void setUpdateHour(int hour) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(NOTIFICATION_DELIVERY_HOUR, hour);
        editor.commit();
    }

    /*
     *
     */
    public void setProperty(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /*
     *
     */
    public String getProperty(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);

    }


    private void putPreferenceValue(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public boolean notificationsOnFavoritesEnabled() {
        return false;  //To change body of created methods use File | Settings | File Templates.
    }
}
