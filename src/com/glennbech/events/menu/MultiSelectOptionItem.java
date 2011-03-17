package com.glennbech.events.menu;

/**
 * @author Glenn Bech
 */
public class MultiSelectOptionItem extends OptionItem {

    String[] choices;

    public MultiSelectOptionItem(String key, String title, String description) {
        super(key, title, description);
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
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
}
