package com.glennbech.events.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Glenn Bech
 */
public class MultiSelectOptionItem extends OptionItem {

    List<String> choices;

    public MultiSelectOptionItem(String key, String title, String description) {
        super(key, title, description);
        choices = new ArrayList<String>();
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    @Override
    public void fromPreferenceValue(String string) {
        this.value = string;
    }

    @Override
    public String toPrefererenceValue() {
        return value;
    }

    public void addChoice(String choice) {
        this.choices.add(choice);
    }

}
