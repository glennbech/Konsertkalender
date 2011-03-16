package com.glennbech.events.eventlist;

import com.glennbech.events.parser.VEvent;

import java.io.IOException;
import java.util.List;

/**
 * Interface to get new calendar events
 *
 * @author Glenn Bech
 */
public interface EventList {

    List<VEvent> getEvents() throws IOException;

}
