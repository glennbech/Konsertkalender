package com.glennbech.konsertkalender.quickbar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.glennbech.konsertkalender.R;
import com.glennbech.konsertkalender.parser.VEvent;


/**
 * @author Glenn Bech
 */
public class DeferActionItem extends ActionItem implements View.OnClickListener {

    private Context context;
    private QuickAction quickAction;
    private static String TAG = DeferActionItem.class.getName();

    public DeferActionItem(Context context, QuickAction action) {
        this.context = context;
        this.quickAction = action;
        setIcon(context.getResources().getDrawable(R.drawable.calendar));
        setOnClickListener(this);
        setTitle("Kalender");
    }

    public void onClick(View v) {
        quickAction.dismiss();
        VEvent event = (VEvent) getArgument();
        Intent oIntent = new Intent(Intent.ACTION_EDIT);
        oIntent.setType("vnd.android.cursor.item/event");

        oIntent.putExtra("title", event.getSummary());
        oIntent.putExtra("description", event.getUrl());
        oIntent.putExtra("eventLocation", event.getLocation());
        oIntent.putExtra("beginTime", event.getStartDate().getTime() + 1000 * 60 * 60 * 5 );
        oIntent.putExtra("endTime", event.getStartDate().getTime()  + 1000 * 60 * 60 * 6);


        oIntent.putExtra("allDay", true);

        Log.d(TAG, "starting intent to add calendar item " + event);
        context.startActivity(oIntent);

    }
}

