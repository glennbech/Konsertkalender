package com.glennbech.konsertkalender.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.glennbech.konsertkalender.Configuration;
import com.glennbech.konsertkalender.GenericEventListActivity;
import com.glennbech.konsertkalender.R;
import com.glennbech.konsertkalender.eventlist.EventList;
import com.glennbech.konsertkalender.eventlist.EventListFactory;
import com.glennbech.konsertkalender.parser.VEvent;
import com.glennbech.konsertkalender.persistence.EventStore;
import com.glennbech.konsertkalender.persistence.SQLiteEventStore;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Timer task that is responsible for reload of the the database.
 *
 * @author Glenn Bech
 */
public class ReloadDatabaseTask extends TimerTask {

    private final Context context;
    private final Configuration config;

    private static final String TAG = ReloadDatabaseTask.class.getName();
    public static final String DATABASE_READY = "com.glennbech.konsertkalender.DATABASE_READY";
    public static final int MY_NOTIFICATION_ID = 666;

    public ReloadDatabaseTask(Context context) {
        this.context = context;
        config = new Configuration(context);
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        Log.d(TAG, "run - updating database");
        final EventStore eventStore = new SQLiteEventStore(context);

        if (config.notificationsEnabled() == false) {
            Log.d(TAG, "Notifications are disabled.");
            return;
        }

        int hour = config.getUpdateHour();
        int nowHour = getHourOfDay();

        if (nowHour != hour) {
            Log.d(TAG, "Back to sleep. Hour is " + nowHour + " waiting for " + hour);
            return;
        }

        try {
            // load database. Lock it down, the app might check if the database is empty and initiate a reload
            synchronized (ReloadDatabaseTask.class) {
                final List<VEvent> oldList = eventStore.getEvents();
                eventStore.clear();

                EventList eventList = EventListFactory.getEventList(context);
                List<VEvent> latestList = eventList.getEvents();

                for (VEvent e : latestList) {
                    eventStore.createEvent(e);
                }

                if (!oldList.equals(latestList)) {
                    Log.d(TAG, "The old and the new List is not the same. Notify the user of change?");
                    context.sendBroadcast(new Intent(DATABASE_READY));
                    HashSet set = new HashSet(latestList);
                    set.removeAll(oldList);
                    if (set.size() != 0) {
                        Log.d(TAG, "The new list has a diff of " + set.size() + " konsertkalender.");
                        notify(context.getString(R.string.notification), context.getString(R.string.notification), new ArrayList<VEvent>(set));
                    } else {
                        Log.d(TAG, "List changed but no new items.");
                    }
                }
                Log.d(TAG, "Event check complete. Took " + (System.currentTimeMillis() - startTime) + " ms");

            }

        } catch (IOException e) {
            Log.e("Feil under henting av konsertprogram, vi venter til neste gang", e.getMessage(), e);
        }
    }

    private int getHourOfDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.HOUR_OF_DAY);
    }

    private void notify(String title, String message, List<VEvent> newEvents) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i = new Intent().setClass(context, GenericEventListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("caption", "nyheter");
        PendingIntent pendingIntent;
        if (newEvents != null) {
            i.putExtra("konsertkalender", (Serializable) newEvents);
            pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        } else {
            pendingIntent = null;
        }

        Notification notification = new Notification(R.drawable.icon, title, System.currentTimeMillis());
        notification.setLatestEventInfo(context, title, message, pendingIntent);
        notificationManager.notify(MY_NOTIFICATION_ID, notification);
    }

}

