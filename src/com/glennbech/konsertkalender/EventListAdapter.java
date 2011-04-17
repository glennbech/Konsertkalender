package com.glennbech.konsertkalender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.glennbech.konsertkalender.parser.VEvent;

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


            final ImageView ib = (ImageView) v.findViewById(R.id.starbutton);
            if (event.isFavorite()) {
                ib.setImageDrawable(context.getResources().getDrawable(R.drawable.starchecked));
            } else {
                ib.setImageDrawable(null);
            }
        }
        return v;
    }
}