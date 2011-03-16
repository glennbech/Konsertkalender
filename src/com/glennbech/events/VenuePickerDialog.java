package com.glennbech.events;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.glennbech.events.persistence.EventStore;
import com.glennbech.events.persistence.SQLiteEventStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Glenn Bech
 */
class VenuePickerDialog extends Dialog implements DialogInterface.OnDismissListener {


    private final Context context;
    private EventStore store;

    private List<LocationItem> allLocations = new ArrayList<LocationItem>();
    private List<String> selectedLocations = new ArrayList<String>();

    public VenuePickerDialog(Context context) {
        super(context);
        this.context = context;
    }

    public List<LocationItem> getAllLocations() {
        return allLocations;
    }

    public void setAllLocations(List<LocationItem> allLocations) {
        this.allLocations = allLocations;
    }

    public List<String> getSelectedLocations() {
        return selectedLocations;
    }

    public void setSelectedLocations(List<String> selectedLocations) {
        this.selectedLocations = selectedLocations;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venuepicker);
        setOnDismissListener(this);

        setTitle(context.getResources().getString(R.string.choseLocations));
        store = new SQLiteEventStore(context);
        List<String> locStrings = store.getLocations();
        for (String l : locStrings) {
            allLocations.add(new LocationItem(l, false));
        }

        LocationsAdapter va = new LocationsAdapter(context, R.layout.venueitem, allLocations);
        final ListView lv = (ListView) findViewById(R.id.venueList);
        lv.setAdapter(va);

        Button okButton = (Button) findViewById(R.id.venueok);
        okButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                ListAdapter listAdapter = lv.getAdapter();
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    LocationItem venue = (LocationItem) listAdapter.getItem(i);
                    if (venue.isEnabled()) {
                        selectedLocations.add(venue.getLocationName());
                    }
                }
                dismiss();
            }
        });

        Button btnOk = (Button) findViewById(R.id.venuecancel);
        btnOk.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private class LocationsAdapter extends ArrayAdapter<LocationItem> {

        private final Context context;
        private final List<LocationItem> objects;

        private LocationsAdapter(Context context, int textViewResourceId, List<LocationItem> objects) {
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

            final LocationItem item = objects.get(position);
            if (item != null) {
                TextView tv = (TextView) v.findViewById(R.id.venuename);
                tv.setText(item.getLocationName());
                final CheckBox cb = (CheckBox) v.findViewById(R.id.venueselected);
                cb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (cb.isChecked()) {
                            item.setEnabled(true);
                        } else {
                            item.setEnabled(false);
                        }
                    }
                });
            }
            return v;
        }
    }

    /**
     * If we have more than zero selected items, we do a search and displays the search result
     *
     * @param dialogInterface
     */
    public void onDismiss(DialogInterface dialogInterface) {
        Intent i = new Intent().setClass(context, GenericEventListActivity.class);
        List<String> selectedLocations = getSelectedLocations();
        if (selectedLocations.size() != 0) {
            i.putExtra(GenericEventListActivity.INTENT_EXTRA_EVENTS, (Serializable) store.searchByLocations(selectedLocations));
            i.putExtra(GenericEventListActivity.INTENT_EXTRA_CAPTION, context.getResources().getString(R.string.searchresult));
            context.startActivity(i);
        }
    }

    /**
     * Used locally to hold what location is selected.
     */
    private class LocationItem {

        private String locationName;
        private boolean enabled;

        public LocationItem(String locationName) {
            this.locationName = locationName;
        }

        public LocationItem(String string, boolean b) {
            this.locationName = string;
            this.enabled = b;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

    }


}
