package com.glennbech.konsertkalender.persistence;

import com.glennbech.konsertkalender.parser.VEvent;

import java.util.Date;
import java.util.List;

/**
 * Inteface for persistence,
 *
 * @author Glenn Bech
 */
public interface EventStore {
    void clear();

    void createEvent(VEvent event);

    List<VEvent> getEvents();

    void setFavorite(String uid, Date startdate, boolean isFavorite);

    List<String> getLocations();

    List<VEvent> search(String searchString);

    List<VEvent> searchByLocations(List<String> locations);

    List<VEvent> getFavorites();

}
