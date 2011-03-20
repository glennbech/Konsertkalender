package com.glennbech.konsertkalender;

import android.app.*;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.glennbech.konsertkalender.eventlist.EventList;
import com.glennbech.konsertkalender.eventlist.EventListFactory;
import com.glennbech.konsertkalender.parser.VEvent;
import com.glennbech.konsertkalender.persistence.EventStore;
import com.glennbech.konsertkalender.persistence.SQLiteEventStore;
import com.glennbech.konsertkalender.service.EventReloadService;
import com.glennbech.konsertkalender.service.ReloadDatabaseTask;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("E dd.MM.yyyy hh:mm");

    private static final int EVENT_OK = 1;
    private static final int EVENT_ERROR = 99;
    private static final int DIALOG_VENUE = 2;
    protected static final int SECOND = 1000;
    protected static final int MINUTE = SECOND * 60;
    protected static final int HOUR = MINUTE * 60;
    protected static final int DAY = HOUR * 24;

    private ProgressDialog progress;
    private SectionedAdapter adapter;
    private EventStore store;

    private static final String TAG = MainActivity.class.getName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        store = new SQLiteEventStore(this);

        // register the service, and create it if it is not running.
        bindService(new Intent(this, EventReloadService.class), onServiceConntection, Context.BIND_AUTO_CREATE);

        adapter = new EventSectionedAdapter(this);

        // Initialize the data in the application.
        Log.d(MainActivity.class.getName(), "Loading database.");

        // don't run it at the same time the Reload Database task is running. The ReloadAtabasetask also requests
        // a lock on the class object.
        synchronized (ReloadDatabaseTask.class) {
            List<VEvent> events = store.getEvents();
            if (events.size() == 0) {
                Log.d(MainActivity.class.getName(), "No konsertkalender in database. Loading from net.");
                loadFromNet();
            }
        }

        ImageButton b = (ImageButton) findViewById(R.id.gotofavoritebutton);
        b.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent().setClass(MainActivity.this, GenericEventListActivity.class);
                i.putExtra(GenericEventListActivity.INTENT_EXTRA_EVENTS, (Serializable) store.getFavorites());
                i.putExtra(GenericEventListActivity.INTENT_EXTRA_CAPTION, getResources().getString(R.string.favoritter));
                startActivity(i);
            }
        });

        ImageButton searhButton = (ImageButton) findViewById(R.id.searchbutton);
        searhButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.search);
                String query = et.getText().toString();
                Intent i = new Intent().setClass(MainActivity.this, GenericEventListActivity.class);
                i.putExtra(GenericEventListActivity.INTENT_EXTRA_EVENTS, (Serializable) store.search(query.trim()));
                i.putExtra(GenericEventListActivity.INTENT_EXTRA_CAPTION, getResources().getString(R.string.searchresult));
                startActivity(i);
            }
        });

        ImageButton venueFilterButton = (ImageButton) findViewById(R.id.filterbutton);
        venueFilterButton.setOnClickListener(new Button.OnClickListener() {
            final List<String> selectedLocations = new ArrayList<String>();

            public void onClick(View view) {
                final VenuePickerDialog newDialog = new VenuePickerDialog(MainActivity.this);

                newDialog.show();

            }
        });

        ListView listview = (ListView) findViewById(R.id.messageList);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                VEvent clickedEvent = (VEvent) adapterView.getItemAtPosition(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedEvent.getUrl())));
            }
        });

        redrawList(store.getEvents());
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

        if (id == DIALOG_VENUE) {
            return new VenuePickerDialog(this);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.networkerror))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                loadFromNet();
            }
        });
        return builder.create();
    }

    private void loadFromNet() {
        progress = ProgressDialog.show(this, "Vennligst vent", "Laster program", true);
        Runnable r = new Runnable() {
            public void run() {
                // the database task may be updating the datbas. Queue'm up!
                synchronized (ReloadDatabaseTask.class) {
                    EventList el = EventListFactory.getEventList(MainActivity.this);
                    List<VEvent> allEvents;
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
            }

        };

        Thread t = new Thread(r);
        t.start();
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
            if (msg.what == EVENT_OK) {
                Log.d(TAG, "OK Event, dismissing progress & redraw");
                // Not sure about this.... get it again?
                redrawList(store.getEvents());
            } else if (msg.what == EVENT_ERROR) {
                Log.d(TAG, "ERROR event, dismissing progrss & redraw");
                showDialog(0);
            }
        }
    };

    private void redrawList(List<VEvent> events) {
        adapter.clear();

        List<VEvent> upComingEvents = new ArrayList<VEvent>();
        List<VEvent> otherEvents = new ArrayList<VEvent>();

        final Date aWeekAhed = new Date(System.currentTimeMillis() + DAY * 7);

        for (VEvent e : events) {
            if (e.getStartDate() != null && e.getStartDate().before(aWeekAhed)) {
                upComingEvents.add(e);
            } else {
                otherEvents.add(e);
            }
        }

        if (upComingEvents.size() != 0) {
            Log.d(MainActivity.class.getName(), "ComingEvents List has values " + upComingEvents.size());
            adapter.addSection("Neste 7 dager", new EventListAdapter(this, adapter, R.layout.itemrow, upComingEvents));
        }

        if (otherEvents.size() != 0) {
            Log.d(MainActivity.class.getName(), "Older" + GenericEventListActivity.INTENT_EXTRA_EVENTS + " List has values " + otherEvents.size());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuitemfeedback) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.glennbech.konsertkalender"));
            startActivity(i);
        } else if (item.getItemId() == R.id.menuitemconfig) {
            Intent i = new Intent().setClass(this, ConfigActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.menuitemrefresh) {
            loadFromNet();
        }
        return super.onOptionsItemSelected(item);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, "Programmet oppdatert", Toast.LENGTH_LONG).show();
            redrawList(store.getEvents());
        }
    };

    /**
     * Callback for the service.
     */
    public final ServiceConnection onServiceConntection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            EventReloadService reloadService = ((EventReloadService.LocalBinder) iBinder).getService();
            Log.d(MainActivity.class.getName(), "Service connected.");
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(MainActivity.class.getName(), "Service disconnected.");
        }
    };
}