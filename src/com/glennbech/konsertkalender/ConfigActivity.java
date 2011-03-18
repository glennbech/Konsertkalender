package com.glennbech.konsertkalender;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.glennbech.konsertkalender.menu.*;
import com.glennbech.konsertkalender.menuadapter.MenuAdapter;
import com.glennbech.konsertkalender.menuadapter.MultiSelectDialog;

/**
 * @author Glenn Bech
 */
public class ConfigActivity extends Activity {

    private static String TAG = ConfigActivity.class.getName();
    private Bundle args;
    private Configuration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        config = new Configuration(this);

        Options options = new Options();
        OptionGroup notifyUpdateGroup = new OptionGroup("Notifikasjoner");

        CheckBoxOptionItem notifyOnNew = new CheckBoxOptionItem(Configuration.KEY_ARE_NOTIFICATIONS_ENABLED, getResources().getString(R.string.notifynew), getResources().getString(R.string.notifynewdesc));
        notifyOnNew.setChecked(config.notificationsEnabled());
        notifyUpdateGroup.add(notifyOnNew);

        MultiSelectOptionItem notifyDelivery = new MultiSelectOptionItem(Configuration.NOTIFICATION_DELIVERY_HOUR, getResources().getString(R.string.notifydelivery), getResources().getString(R.string.notifydeliverydesc));
        notifyDelivery.addChoice(8, "08:00");
        notifyDelivery.addChoice(9, "09:00");
        notifyDelivery.addChoice(10, "10:00");
        notifyDelivery.addChoice(12, "12:00");
        notifyDelivery.addChoice(14, "14:00");
        notifyDelivery.addChoice(16, "16:00");
        notifyDelivery.addChoice(18, "18:00");
        notifyDelivery.addChoice(21, "21:00");
        notifyDelivery.addChoice(22, "22:00");
        notifyDelivery.addChoice(23, "23:00");
        notifyUpdateGroup.add(notifyDelivery);

        options.add(notifyUpdateGroup);
        final MenuAdapter menuAdapter = new MenuAdapter(ConfigActivity.this, options);
        ListView lv = (ListView) findViewById(R.id.menuItems);
        lv.setAdapter(menuAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                OptionItem i = (OptionItem) adapterView.getItemAtPosition(pos);

                String value = config.getProperty(i.getKey(), null);
                i.setValue(value);

                if (i instanceof MultiSelectOptionItem) {
                    args = new Bundle();
                    args.putSerializable(MultiSelectOptionItem.class.getName(), i);
                    showDialog(1);
                } else if (i instanceof CheckBoxOptionItem) {
                    ((CheckBoxOptionItem) i).setChecked(!((CheckBoxOptionItem) i).isChecked());
                    menuAdapter.notifyDataSetChanged();
                    config.setProperty(i.getKey(), i.toPrefererenceValue());
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Log.d(TAG, "onCreateDialog " + id);
        if (id == 1) {
            return new MultiSelectDialog(this);
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        if (id == 1) {
            MultiSelectOptionItem item = (MultiSelectOptionItem) args.getSerializable(MultiSelectOptionItem.class.getName());
            MultiSelectDialog multiSelectDialog = (MultiSelectDialog) dialog;
            multiSelectDialog.setTitle(item.getTitle());
            multiSelectDialog.setItem(item);
        }
    }


}
