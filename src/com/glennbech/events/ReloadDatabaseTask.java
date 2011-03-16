package com.glennbech.events;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.glennbech.events.eventlist.EventList;
import com.glennbech.events.eventlist.EventListFactory;
import com.glennbech.events.parser.VEvent;
import com.glennbech.events.persistence.EventStore;
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
class ReloadDatabaseTask extends TimerTask {

    private final Context context;

    private static final String TAG = ReloadDatabaseTask.class.getName();
    public static final String DATABASE_READY = "com.glennbech.events.DATABASE_READY";
    public static final int MY_NOTIFICATION_ID = 666;

    public ReloadDatabaseTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Log.d(TAG, "run - updating database");
        final EventStore eventStore = new SQLiteEventStore(context);

        try {
            // load database
            final List<VEvent> oldList = eventStore.getEvents();
            Log.d(TAG, "Checking if this is the first time the application is run.");
            eventStore.clear();

            EventList eventList = EventListFactory.getEventList(context);
            List<VEvent> latestList = eventList.getEvents();

            for (VEvent e : latestList) {
                eventStore.createEvent(e);
            }


            if (!oldList.equals(latestList)) {
                Log.d(TAG, "The old and the new List is not the same. Notify the user of change.");
                context.sendBroadcast(new Intent(DATABASE_READY));
                HashSet set = new HashSet(latestList);
                set.removeAll(oldList);
                if (set.size() != 0) {
                    Log.d(TAG, "The new list has a diff of " + set.size() + " events.");
                    notify(context.getString(R.string.notificationHeader), context.getString(R.string.notification), new ArrayList<VEvent>(set));
                } else {
                    Log.d(TAG, "List changed but no new items" + set.size() + " events.");
                }
            }

        } catch (IOException e) {
            notify(context.getString(R.string.notificationOnErrorHeader), context.getString(R.string.notificationOnError), null);
            Log.e("Feil under henting av konsertprogram, vi venter til neste gang", e.getMessage(), e);
        }
    }

    private void notify(String title, String message, List<VEvent> newEvents) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i = new Intent().setClass(context, GenericEventListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("caption", "nyheter");
        PendingIntent pendingIntent;
        if (newEvents != null) {
            i.putExtra("events", (Serializable) newEvents);
            pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        } else {
            pendingIntent = null;
        }

        Notification notification = new Notification(R.drawable.icon, title, System.currentTimeMillis());
        notification.setLatestEventInfo(context, title, message, pendingIntent);
        notificationManager.notify(MY_NOTIFICATION_ID, notification);
    }

}

