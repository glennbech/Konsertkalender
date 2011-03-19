package com.glennbech.konsertkalender.menu;

import java.util.List;

/**
 * @author Glenn Bech
 */
public class HourOptionItem extends MultiSelectOptionItem {


    public HourOptionItem(String key, String title, String description) {
        super(key, title, description);
    }

    @Override
    public List<Item> getChoices() {
        choices.add(new Item("8", "08:00"));
        choices.add(new Item("9", "09:00"));
        choices.add(new Item("10", "10:00"));
        choices.add(new Item("11", "11:00"));
        choices.add(new Item("12", "12:00"));
        choices.add(new Item("13", "13:00"));
        choices.add(new Item("14", "14:00"));
        choices.add(new Item("15", "15:00"));
        choices.add(new Item("16", "16:00"));
        choices.add(new Item("17", "17:00"));
        choices.add(new Item("18", "18:00"));
        choices.add(new Item("19", "19:00"));
        choices.add(new Item("20", "20:00"));
        choices.add(new Item("21", "21:00"));
        choices.add(new Item("22", "22:00"));
        choices.add(new Item("23", "23:00"));
        choices.add(new Item("00", "00:00"));
        choices.add(new Item("01", "01:00"));
        choices.add(new Item("02", "02:00"));
        choices.add(new Item("03", "03:00"));
        choices.add(new Item("04", "04:00"));
        choices.add(new Item("05", "05:00"));
        choices.add(new Item("06", "06:00"));
        choices.add(new Item("07", "07:00"));
        return choices;
    }
}
