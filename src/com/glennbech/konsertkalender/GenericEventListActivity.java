package com.glennbech.konsertkalender;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.glennbech.konsertkalender.parser.VEvent;
import com.glennbech.konsertkalender.persistence.EventStore;
import com.glennbech.konsertkalender.persistence.SQLiteEventStore;
import com.glennbech.konsertkalender.service.ReloadDatabaseTask;

import java.util.List;

/**
 * Reusable activity that will pick up a title and a list og konsertkalender from the intent.
 * Used by favorites and search.
 *
 * @author Glenn Bech
 */
public class GenericEventListActivity extends Activity {

    static final String INTENT_EXTRA_EVENTS = "konsertkalender";
    static final String INTENT_EXTRA_CAPTION = "caption";
    private SectionedAdapter adapter;
    private EventStore store;
    private List<VEvent> favorites;
    private String caption;
    private boolean fromNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.favorites);
        store = new SQLiteEventStore(this);
        adapter = new EventSectionedAdapter(this);

        ImageButton ib = (ImageButton) findViewById(R.id.back);
        ib.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (fromNotification == true) {
                    Intent i = new Intent().setClass(GenericEventListActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ReloadDatabaseTask.MY_NOTIFICATION_ID);

        this.favorites = (List<VEvent>) getIntent().getExtras().getSerializable("konsertkalender");
        this.caption = (String) getIntent().getExtras().getSerializable("caption");
        this.fromNotification = getIntent().getExtras().getBoolean("fromnotification", false);

        TextView caption = (TextView) findViewById(R.id.caption);
        caption.setText(this.caption);
        redrawList(favorites);
    }

    private void redrawList(List<VEvent> events) {
        adapter.clear();

        EventListAdapter newItemsAdapter = new EventListAdapter(this, adapter, R.layout.itemrow, events);
        adapter.addSection("", newItemsAdapter);

        ListView lv = (ListView) findViewById(R.id.favoritelist);
        lv.setAdapter(adapter);
    }

}
