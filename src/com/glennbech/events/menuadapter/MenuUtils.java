package com.glennbech.events.menuadapter;

import android.content.SharedPreferences;
import com.glennbech.events.menu.OptionGroup;
import com.glennbech.events.menu.OptionItem;
import com.glennbech.events.menu.Options;

/**
 * @author Glenn Bech
 */
public class MenuUtils {

    public static void load(Options m, SharedPreferences p) {

        for (OptionGroup s : m.getGroups()) {
            for (OptionItem i : s.getItems()) {
                i.fromPreferenceValue(p.getString(i.getKey(), null));
            }
        }
    }

    public static void save(Options m, SharedPreferences p) {
        SharedPreferences.Editor editor = p.edit();
        for (OptionGroup s : m.getGroups()) {
            for (OptionItem i : s.getItems()) {
                editor.putString(i.getKey(), i.toPrefererenceValue());
            }
        }
        editor.commit();
    }

}
