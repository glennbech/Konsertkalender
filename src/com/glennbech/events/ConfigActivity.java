package com.glennbech.events;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.glennbech.events.menu.CheckBoxOptionItem;
import com.glennbech.events.menu.MultiSelectOptionItem;
import com.glennbech.events.menu.OptionGroup;
import com.glennbech.events.menu.Options;
import com.glennbech.events.menuadapter.MenuAdapter;

/**
 * @author Glenn Bech
 */
public class ConfigActivity extends Activity {

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

        MenuAdapter menuAdapter = new MenuAdapter(ConfigActivity.this, options);
        ListView lv = (ListView) findViewById(R.id.menuItems);
        lv.setAdapter(menuAdapter);

    }
}
