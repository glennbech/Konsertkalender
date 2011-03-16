package com.glennbech.events;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.glennbech.events.persistence.SQLiteEventStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Glenn Bech
 */
class VenuePickerDialog extends Dialog {

    private final Context context;

    public VenuePickerDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venuepicker);

        SQLiteEventStore store= new SQLiteEventStore(context);

        List<VenueItem> venues = new ArrayList<VenueItem>();

        VenueItem e = new VenueItem("Rockefeller");
        e.setEnabled(true);
        venues.add(e);

        e = new VenueItem("John Dee");
        e.setEnabled(true);
        venues.add(e);

        e = new VenueItem("Sentrum Scene");
        e.setEnabled(true);
        venues.add(e);

        VenueAdapter va = new VenueAdapter(context, R.layout.venueitem, venues);
        ListView lv = (ListView) findViewById(R.id.venueList);
        lv.setAdapter(va);

        Button b = (Button) findViewById(R.id.venuecancel);
        b.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                dismiss();
            }
        });

        Button btnOk = (Button) findViewById(R.id.venueok);
        btnOk.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                dismiss();
            }
        });

    }


    private class VenueAdapter extends ArrayAdapter<VenueItem> {

        private final Context context;
        private final List<VenueItem> objects;

        private VenueAdapter(Context context, int textViewResourceId, List<VenueItem> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.venueitem, null);
            }

            final VenueItem item = objects.get(position);
            if (item != null) {
                TextView tv = (TextView) v.findViewById(R.id.venuename);
                tv.setText(item.getVenue());
                CheckBox cb = (CheckBox) v.findViewById(R.id.venueselected);

                if (item.isEnabled()) {
                    cb.setChecked(true);
                } else {
                    cb.setChecked(false);
                }


            }
            return v;
        }
    }


}
