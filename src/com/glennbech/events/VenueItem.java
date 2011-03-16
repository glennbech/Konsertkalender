package com.glennbech.events;

/**
 * @author Glenn Bech
 */
class VenueItem {

    private String venue;
    private boolean enabled;

    public VenueItem(String venue) {
        this.venue = venue;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
