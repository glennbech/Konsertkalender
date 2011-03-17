package com.glennbech.konsertkalender.eventlist;

import com.glennbech.konsertkalender.parser.VEvent;

import java.io.IOException;
import java.util.List;

/**
 * Interface to get new calendar konsertkalender
 *
 * @author Glenn Bech
 */
public interface EventList {

    List<VEvent> getEvents() throws IOException;

}
