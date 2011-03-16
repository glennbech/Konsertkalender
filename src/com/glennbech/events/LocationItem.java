package com.glennbech.events;

/**
 * @author Glenn Bech
 */
class LocationItem {

    private String locationName;
    private boolean enabled;

    public LocationItem(String locationName) {
        this.locationName = locationName;
    }

    public LocationItem(String string, boolean b) {
        this.locationName = string;
        this.enabled = b;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
