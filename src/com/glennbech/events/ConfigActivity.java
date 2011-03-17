package com.glennbech.events;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.glennbech.events.menu.*;
import com.glennbech.events.menuadapter.MenuAdapter;
import com.glennbech.events.menuadapter.MultiSelectDialog;

/**
 * @author Glenn Bech
 */
public class ConfigActivity extends Activity {

    private Bundle args;
    public static final String PREFERENCE_FILE_NAME = "config";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        Options options = new Options();
        OptionGroup notifyUpdateGroup = new OptionGroup("Notifikasjoner");

        CheckBoxOptionItem notifyOnNew = new CheckBoxOptionItem("NOTIFYNEW", getResources().getString(R.string.notifynew), getResources().getString(R.string.notifynewdesc));
        notifyUpdateGroup.add(notifyOnNew);

        MultiSelectOptionItem notoifyInterval = new MultiSelectOptionItem("NOTIFYINTERVAL", getResources().getString(R.string.notifyinterval), getResources().getString(R.string.notifyintervaldesc));
        notoifyInterval.addChoice("Hver 4 time");
        notoifyInterval.addChoice("Hver 8 time");
        notoifyInterval.addChoice("Hver 24 time");

        notifyUpdateGroup.add(notoifyInterval);
        options.add(notifyUpdateGroup);

        MultiSelectOptionItem notifyDelivery = new MultiSelectOptionItem("NOTIFYDELIVERY", getResources().getString(R.string.notifydelivery), getResources().getString(R.string.notifydeliverydesc));

        notifyDelivery.addChoice("08:00");
        notifyDelivery.addChoice("10:00");
        notifyDelivery.addChoice("12:00");
        notifyDelivery.addChoice("14:00");
        notifyDelivery.addChoice("16:00");
        notifyDelivery.addChoice("18:00");
        notifyDelivery.addChoice("20:00");
        notifyDelivery.addChoice("22:00");

        notifyUpdateGroup.add(notifyDelivery);

        final MenuAdapter menuAdapter = new MenuAdapter(ConfigActivity.this, options);
        ListView lv = (ListView) findViewById(R.id.menuItems);
        lv.setAdapter(menuAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                OptionItem i = (OptionItem) adapterView.getItemAtPosition(pos);
                if (i instanceof MultiSelectOptionItem) {
                    args = new Bundle();
                    args.putSerializable(MultiSelectOptionItem.class.getName(), i);
                    showDialog(1);
                } else if (i instanceof CheckBoxOptionItem) {
                    ((CheckBoxOptionItem) i).setChecked(!((CheckBoxOptionItem) i).isChecked());
                    menuAdapter.notifyDataSetChanged();
                    putPreferenceValue(i.getKey(), i.toPrefererenceValue());
                }
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            MultiSelectOptionItem item = (MultiSelectOptionItem) args.getSerializable(MultiSelectOptionItem.class.getName());
            return new MultiSelectDialog(this, item.getTitle(), (MultiSelectOptionItem) item);
        }
        return super.onCreateDialog(id);
    }

    private void putPreferenceValue(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(ConfigActivity.PREFERENCE_FILE_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }
}
