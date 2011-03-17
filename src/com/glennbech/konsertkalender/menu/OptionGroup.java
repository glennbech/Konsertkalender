package com.glennbech.konsertkalender.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Glenn Bech
 */
public class OptionGroup {

    private String caption;
    private List<OptionItem> items;


    public OptionGroup(String caption) {
        this.caption = caption;
        this.items = new ArrayList<OptionItem>();
    }

    public OptionGroup(String caption, List<OptionItem> items) {
        this.caption = caption;
        this.items = new ArrayList<OptionItem>();
    }

    public void add(OptionItem newItem) {
        items.add(newItem);
    }

    public List<OptionItem> getItems() {
        return items;
    }

    public void setItems(List<OptionItem> items) {
        this.items = items;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
