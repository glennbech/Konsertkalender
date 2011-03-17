package com.glennbech.konsertkalender.menu;

import java.io.Serializable;

/**
 * Base class for all Option Items
 *
 * @author Glenn Bech
 */
public abstract class OptionItem implements Serializable {

    private String key;
    private String title;
    private String description;
    protected String value;

    public OptionItem(String key, String title, String description) {
        this.key = key;
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public abstract void fromPreferenceValue(String string);

    public abstract String toPrefererenceValue();


}
