package com.glennbech.konsertkalender.eventlist;

import android.util.Log;
import com.glennbech.konsertkalender.parser.CalendarParser;
import com.glennbech.konsertkalender.parser.VEvent;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * Retrieves a list of konsertkalender from an .ics file located on the net somewhere.
 *
 * @author Glenn Bech
 */
class HTTPEventList implements EventList {

    private static String TAG = HTTPEventList.class.getName();
    private static final String FEED_URL = "http://50.17.205.191/kalender.ics";
    // private static final String FEED_URL = "http://bechonjava.squarespace.com/storage/android/kalender.ics";

    public List<VEvent> getEvents() throws IOException {
        // Fetch feed
        final URL feedUrl = new URL(FEED_URL);
        final CalendarParser cp = new CalendarParser();
        final URLConnection urlConnection = feedUrl.openConnection();

        List<VEvent> events = cp.parse(urlConnection.getInputStream());
        Log.d(TAG, events.size() + " events loaded.");
        return events;
    }
}
