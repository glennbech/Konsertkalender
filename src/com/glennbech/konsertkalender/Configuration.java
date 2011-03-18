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
    public static final String NOTIFICATION_INTERVAL = "NOTIFYINTERVAL";
    public static final String NOTIFICATION_DELIVERY_HOUR = "NOTIFYDELIVERY";

    public static final String KEY_ARE_NOTIFICATIONS_FAVORITES_ENABLED = "NOTIFYONFAVORITES";
    public static final String NOTIFICATION_FAVORITES_DAYS_IN_ADVANCE = "NOTIFICATIONFAVORITESDAYSINADVANCE";

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
     * @return the update interval i milliseconds
     */
    public long getUpdateInterval() {
        return preferences.getInt(NOTIFICATION_INTERVAL, -1);
    }

    /**
     * @return
     */
    public int getUpdateHour() {

        String hourString = preferences.getString(NOTIFICATION_DELIVERY_HOUR, null);
        return Integer.parseInt(hourString);

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
     * Sets the notification interval
     *
     * @param interval
     */
    public void setUpdateInterval(int interval) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_ARE_NOTIFICATIONS_ENABLED, interval);
        editor.commit();
    }

    /**
     * Sets the hour to notify
     *
     * @param interval
     */
    public void setUpdateHour(int interval) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_ARE_NOTIFICATIONS_ENABLED, interval);
        editor.commit();
    }

    public void setProperty(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

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
