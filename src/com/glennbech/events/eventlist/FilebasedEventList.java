package com.glennbech.events.eventlist;

import android.content.Context;
import com.glennbech.events.parser.CalendarParser;
import com.glennbech.events.R;
import com.glennbech.events.parser.VEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Glenn Bech
 */
public class FilebasedEventList implements EventList {

    private Context context;

    public FilebasedEventList(Context context) {
        this.context = context;
    }


    public List<VEvent> getEvents() throws IOException {

        final CalendarParser cp = new CalendarParser();
        final InputStream inputStream = context.getResources().openRawResource(R.raw.kalender);
        final List<VEvent> latestList = cp.parse(inputStream);
        return latestList;


    }
}
