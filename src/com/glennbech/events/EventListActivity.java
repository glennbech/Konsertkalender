package com.glennbech.events;

import android.app.*;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.glennbech.events.eventlist.EventList;
import com.glennbech.events.eventlist.URLBasedEventList;
import com.glennbech.events.parser.VEvent;
import com.glennbech.events.persistence.SQLiteEventStore;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.glennbech.events.TimerConsts.DAY;

public class EventListActivity extends Activity {

    private static final int EVENT_OK = 1;
    private static final int EVENT_ERROR = 99;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("E dd.MM.yyyy hh:mm");
    private ProgressDialog progress;
    private SectionedAdapter adapter;
    private SQLiteEventStore store;
    private MonitorEventListService service;
    private static String TAG = EventListActivity.class.getName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // register the service, and create it if it is not running.
        bindService(new Intent(this, MonitorEventListService.class), onServiceConntection, Context.BIND_AUTO_CREATE);

        adapter = new EventSectionedAdapter(this);

        // Initialize the data in the application.
        Log.d(EventListActivity.class.getName(), "Loading database.");
        store = new SQLiteEventStore(this);

        List<VEvent> events = store.getEvents();
        if (events.size() == 0) {
            Log.d(EventListActivity.class.getName(), "No events in database. Loading from net.");
            loadFromNet();
        }

        ImageButton b = (ImageButton) findViewById(R.id.gotofavoritebutton);
        b.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent().setClass(EventListActivity.this, FavoritesActivity.class);
                i.putExtra(FavoritesActivity.ESTRA_EVENTS, (Serializable) store.getFavorites());
                i.putExtra(FavoritesActivity.EXTRA_CAPTION, getResources().getString(R.string.favoritter));
                startActivity(i);
            }
        });

        ImageButton searhButton = (ImageButton) findViewById(R.id.searchbutton);
        searhButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.search);
                String query = et.getText().toString();
                Intent i = new Intent().setClass(EventListActivity.this, FavoritesActivity.class);
                i.putExtra(FavoritesActivity.ESTRA_EVENTS, (Serializable) store.search(query.trim()));
                i.putExtra(FavoritesActivity.EXTRA_CAPTION, getResources().getString(R.string.searchresult));
                startActivity(i);
            }
        });

        ListView listview = (ListView) findViewById(R.id.messageList);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                VEvent clickedEvent = (VEvent) adapterView.getItemAtPosition(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedEvent.getUrl())));
            }
        });
        events = store.getEvents();
        redrawList(events);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ReloadDatabaseTask.DATABASE_READY);
        this.registerReceiver(this.broadcastReceiver, filter);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ReloadDatabaseTask.MY_NOTIFICATION_ID);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(onServiceConntection);
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Kan ikke hente program. Nettverksfeil?")
                .setCancelable(false)
                .setPositiveButton("Avbryt", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                }).setNegativeButton("Forsøk igjen", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                loadFromNet();
            }
        });
        return builder.create();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.itemmenu, menu);
    }

    private void loadFromNet() {
        progress = ProgressDialog.show(this, "Vennligst vent", "Laster program", true);
        Runnable r = new Runnable() {
            public void run() {
                EventList el = new URLBasedEventList();
                List<VEvent> allEvents = null;
                try {
                    store.clear();
                    allEvents = el.getEvents();
                    for (VEvent e : allEvents) {
                        store.createEvent(e);
                    }
                    handler.sendEmptyMessage(EVENT_OK);
                } catch (IOException e) {
                    handler.sendEmptyMessage(EVENT_ERROR);
                }

            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
            if (msg.what == EVENT_OK) {
                Log.d(TAG, "OK Event, dismissing progress & redraw");
                // Not sure about this.... get it again?
                SQLiteEventStore store = new SQLiteEventStore(EventListActivity.this);
                redrawList(store.getEvents());
            } else if (msg.what == EVENT_ERROR) {
                Log.d(TAG, "ERROR event, dismissing progrss & redraw");
                showDialog(0);
            }
        }
    };

    private void redrawList(List<VEvent> events) {
        adapter.clear();

        List<VEvent> newEvents = new ArrayList<VEvent>();
        List<VEvent> upComingEvents = new ArrayList<VEvent>();
        List<VEvent> otherEvents = new ArrayList<VEvent>();
        SQLiteEventStore store = new SQLiteEventStore(this);

        final Date aWeekAhed = new Date(System.currentTimeMillis() + DAY * 7);
        newEvents = store.getNewItems();

        for (VEvent e : events) {
            if (e.getStartDate() != null && e.getStartDate().before(aWeekAhed)) {
                upComingEvents.add(e);
            } else {
                otherEvents.add(e);
            }
        }

        if (newEvents.size() != 0) {
            Log.d(EventListActivity.class.getName(), "New" + FavoritesActivity.ESTRA_EVENTS + " Since last update has values " + newEvents);
            EventListAdapter newItemsAdapter = new EventListAdapter(this, adapter, R.layout.itemrow, newEvents);
            adapter.addSection("Nyheter", newItemsAdapter);
        }

        if (upComingEvents.size() != 0) {
            Log.d(EventListActivity.class.getName(), "ComingEvents List has values " + upComingEvents.size());
            adapter.addSection("Neste 7 dager", new EventListAdapter(this, adapter, R.layout.itemrow, upComingEvents));
        }

        if (otherEvents.size() != 0) {
            Log.d(EventListActivity.class.getName(), "Older" + FavoritesActivity.ESTRA_EVENTS + " List has values " + otherEvents.size());
            adapter.addSection(" ", new EventListAdapter(this, adapter, R.layout.itemrow, otherEvents));
        }

        ListView lv = (ListView) findViewById(R.id.messageList);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.share:
                ListView lv = (ListView) findViewById(R.id.messageList);
                VEvent event = (VEvent) lv.getItemAtPosition(info.position);
                share(event);
                return true;
        }
        return true;
    }

    public void share(VEvent event) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        StringBuffer sb = new StringBuffer();
        sb.append(event.getSummary());
        sb.append("(" + dateFormat.format(event.getStartDate()) + ")");
        sb.append("- " + event.getUrl());
        sb.append("- " + "Send via \"KonsertKalender for Android\"");

        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, sb.toString());
        startActivity(Intent.createChooser(shareIntent, event.getSummary()));
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(EventListActivity.this, "Programmet oppdatert", Toast.LENGTH_LONG).show();


        }
    };

    /**
     * Callback for the service.
     */
    public ServiceConnection onServiceConntection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = ((MonitorEventListService.LocalBinder) iBinder).getService();
            Log.d(EventListActivity.class.getName(), "Service connected.");
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(EventListActivity.class.getName(), "Service disconnected.");
        }
    };
}