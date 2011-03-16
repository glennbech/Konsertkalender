package com.glennbech.events.eventlist;

import com.glennbech.events.parser.CalendarParser;
import com.glennbech.events.parser.VEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Retrieves a list of events from an .ics file located on the net somewhere.
 *
 * @author Glenn Bech
 */
class URLBasedEventList implements EventList {
    private static final String FEED_URL = "http://www.rockefeller.no/kalender.ics";
    // private static final String FEED_URL = "http://bechonjava.squarespace.com/storage/android/kalender.ics";

    public List<VEvent> getEvents() throws IOException {
        // Fetch feed
        final URL feedUrl = new URL(FEED_URL);
        final CalendarParser cp = new CalendarParser();
        final URLConnection urlConnection = feedUrl.openConnection();
        final InputStream inputStream = urlConnection.getInputStream();
        return cp.parse(inputStream);
    }
}
