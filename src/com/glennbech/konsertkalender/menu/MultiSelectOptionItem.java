package com.glennbech.konsertkalender.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Glenn Bech
 */
public class MultiSelectOptionItem extends OptionItem {

    List<Item> choices;

    public MultiSelectOptionItem(String key, String title, String description) {
        super(key, title, description);
        choices = new ArrayList<Item>();
    }

    public List<Item> getChoices() {
        return choices;
    }

    public void setChoices(List<Item> choices) {
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

    public void addChoice(Object key, String caption) {
        this.choices.add(new Item(key, caption));
    }

    public static class Item implements Serializable {

        private Object value;
        private String caption;

        public Item(Object value, String caption) {
            this.value = value;
            this.caption = caption;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        @Override
        public String toString() {
            return caption;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Item)) return false;

            Item item = (Item) o;

            if (value != null ? !value.equals(item.value) : item.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

}
