package com.glennbech.events.eventlist;

import android.content.Context;
import com.glennbech.events.parser.CalendarParser;
import com.glennbech.events.R;
import com.glennbech.events.parser.VEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Used for debugging. I can put a calendar file in the res/raw folder and load it here.
 * @author Glenn Bech
 */
class BundledInRawFolderEventList implements EventList {

    private final Context context;

    public BundledInRawFolderEventList(Context context) {
        this.context = context;
    }


    public List<VEvent> getEvents() throws IOException {

        final CalendarParser cp = new CalendarParser();
        final InputStream inputStream = context.getResources().openRawResource(R.raw.kalender);
        return cp.parse(inputStream);


    }
}
