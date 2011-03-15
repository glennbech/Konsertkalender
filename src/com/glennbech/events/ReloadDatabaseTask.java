package com.glennbech.events;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.glennbech.events.eventlist.EventList;
import com.glennbech.events.eventlist.URLBasedEventList;
import com.glennbech.events.parser.VEvent;
import com.glennbech.events.persistence.SQLiteEventStore;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TimerTask;

/**
 * Timer task that is responsible for reload of the the database.
 *
 * @author Glenn Bech
 */
public class ReloadDatabaseTask extends TimerTask {

    private Context context;
    private int count = 0;

    private static String TAG = ReloadDatabaseTask.class.getName();
    public static String DATABASE_READY = "com.glennbech.events.DATABASE_READY";
    public static final int MY_NOTIFICATION_ID = 666;

    public ReloadDatabaseTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Log.d(TAG, "run - updating database");
        final SQLiteEventStore eventStore = new SQLiteEventStore(context);

        try {
            // load database
            final List<VEvent> oldList = eventStore.getEvents();
            Log.d(TAG, "Checking if this is the first time the application is run.");
            eventStore.clear();

            EventList eventList = new URLBasedEventList();
            List<VEvent> latestList = eventList.getEvents();

            for (VEvent e : latestList) {
                eventStore.createEvent(e);
            }
            if (!oldList.equals(latestList)) {
                Log.d(TAG, "The old and the new List is not the same. Notify the user of change.");
                context.sendBroadcast(new Intent(DATABASE_READY));
                HashSet set = new HashSet(latestList);
                set.removeAll(oldList);
                Log.d(TAG, "The new list has a diff of " + set.size() + " events.");
                notify("Konsertprogram", "Konsertprogrammet er oppdatert.", new ArrayList(set));
            }

        } catch (IOException e) {
            Log.e("Feil under henting av konsertprogram, vi venter til neste gang", e.getMessage(), e);
        }
    }

    private void notify(String title, String message, List<VEvent> newEvents) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i = new Intent().setClass(context, FavoritesActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("events", (Serializable) newEvents);
        i.putExtra("caption", "nyheter");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new Notification(R.drawable.icon, title, System.currentTimeMillis());
        notification.setLatestEventInfo(context, title, message, pendingIntent);
        notificationManager.notify(MY_NOTIFICATION_ID, notification);

    }

}

