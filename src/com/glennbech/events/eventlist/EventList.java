package com.glennbech.events.eventlist;

import com.glennbech.events.parser.VEvent;

import java.io.IOException;
import java.util.List;

/**
 * @author Glenn Bech
 */
public interface EventList {

    public List<VEvent> getEvents() throws IOException;

}
