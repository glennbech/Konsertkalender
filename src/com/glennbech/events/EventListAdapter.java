package com.glennbech.events;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.glennbech.events.parser.VEvent;
import com.glennbech.events.persistence.SQLiteEventStore;

import java.text.SimpleDateFormat;
import java.util.List;

class EventListAdapter extends ArrayAdapter<VEvent> {

    private final List<VEvent> items;
    private final Context context;
    private final BaseAdapter parentAdapter;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("E dd.MM.yyyy");

    public EventListAdapter(Context context, BaseAdapter parentAdapter, int textViewResourceId, List<VEvent> items) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.context = context;
        this.parentAdapter = parentAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.itemrow, null);
        }

        final VEvent event = items.get(position);
        if (event != null) {

            TextView titleView = (TextView) v.findViewById(R.id.title);
            TextView locationView = (TextView) v.findViewById(R.id.location);
            TextView dateView = (TextView) v.findViewById(R.id.date);

            if (event.getSummary() != null) {
                titleView.setText(event.getSummary().toUpperCase());
            }

            locationView.setText(event.getLocation());
            if (event.getStartDate() != null) {
                dateView.setText(dateFormat.format(event.getStartDate()));
            }

            final ImageButton ib = (ImageButton) v.findViewById(R.id.starbutton);
            ib.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View view) {
                    SQLiteEventStore s = new SQLiteEventStore(context);
                    if (event.isFavorite()) {
                        s.setFavorite(event.getUid(), event.getStartDate(), false);
                        event.setFavorite(false);
                    } else {
                        s.setFavorite(event.getUid(), event.getStartDate(), true);
                        event.setFavorite(true);
                    }
                    parentAdapter.notifyDataSetChanged();
                }
            });

            final ImageButton hb = (ImageButton) v.findViewById(R.id.homebutton);
            hb.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View view) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(event.getUrl())));
                }
            });

            if (event.isFavorite()) {
                ib.setImageDrawable(context.getResources().getDrawable(R.drawable.starchecked));
            } else {
                ib.setImageDrawable(context.getResources().getDrawable(R.drawable.starunchecked));
            }
        }
        return v;
    }
}