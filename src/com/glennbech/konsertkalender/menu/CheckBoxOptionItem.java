package com.glennbech.konsertkalender.menu;

/**
 * @author Glenn Bech
 */
public class CheckBoxOptionItem extends OptionItem {

    private boolean checked;

    public CheckBoxOptionItem(String key, String title, String description) {
        super(key, title, description);
        this.checked = false;
    }

    public CheckBoxOptionItem(String key, String title, String description, boolean checked) {
        super(key, title, description);
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        this.value = String.valueOf(checked);
    }

    public void fromPreferenceValue(String string) {
        if (string == null) {
            checked = false;
        } else {
            this.checked = Boolean.valueOf(string);
        }
    }

    @Override
    public String toPrefererenceValue() {
        if (getValue() == null) {
            return null;
        } else {
            return String.valueOf(checked);
        }


    }
}
