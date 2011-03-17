package com.glennbech.events.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Glenn Bech
 */
public class Options {

    protected List<OptionGroup> groups;

    public Options() {
        groups = new ArrayList<OptionGroup>();
    }

    public List<OptionGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<OptionGroup> groups) {
        this.groups = groups;
    }

    public List<OptionItem> getAllItems() {

        List<OptionItem> allItems = new ArrayList<OptionItem>();
        for (OptionGroup s : groups) {
            allItems.addAll(s.getItems());
        }
        return allItems;
    }

}
