package com.glennbech.events.menu;

/**
 * @author Glenn Bech
 */
public class StringOptionItem extends OptionItem {



    public StringOptionItem(String key, String title, String description) {
        super(key, title, description);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
