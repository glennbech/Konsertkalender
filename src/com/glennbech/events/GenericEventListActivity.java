package com.glennbech.events;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.glennbech.events.parser.VEvent;
import com.glennbech.events.persistence.SQLiteEventStore;

import java.util.List;

/**
 * Reusable activity that will pick up a title and a list og events from the intent.
 * Used by favorites and search.
 *
 * @author Glenn Bech
 */
public class GenericEventListActivity extends Activity {

    static final String ESTRA_EVENTS = "events";
    static final String EXTRA_CAPTION = "caption";
    private SectionedAdapter adapter;
    private SQLiteEventStore store;
    private List<VEvent> favorites;
    private String caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);
        store = new SQLiteEventStore(this);
        adapter = new EventSectionedAdapter(this);

        ImageButton ib = (ImageButton) findViewById(R.id.back);
        ib.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ReloadDatabaseTask.MY_NOTIFICATION_ID);

        this.favorites = (List<VEvent>) getIntent().getExtras().getSerializable("events");
        this.caption = (String) getIntent().getExtras().getSerializable("caption");

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
