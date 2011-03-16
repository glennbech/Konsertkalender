package com.glennbech.events.eventlist;

import android.content.Context;

/**
 * Creates an Event list object. Hides concrete classes.
 *
 * @author Glenn Bech
 */
public class EventListFactory {

    /**
     * Creates an Event List
     *
     * @param context The context (Acitivty, Service etc)
     * @return A event list object that can be used ot get new events.
     */
    public static EventList getEventList(Context context) {
        return new URLBasedEventList();
    }

}
