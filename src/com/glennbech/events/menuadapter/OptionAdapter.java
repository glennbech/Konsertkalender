package com.glennbech.events.menuadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.glennbech.events.R;
import com.glennbech.events.menu.CheckBoxOptionItem;
import com.glennbech.events.menu.MultiSelectOptionItem;
import com.glennbech.events.menu.OptionItem;
import com.glennbech.events.menu.StringOptionItem;

import java.util.List;

/**
 * @author Glenn Bech
 */
public class OptionAdapter extends ArrayAdapter<OptionItem> {

    private Context context;

    public OptionAdapter(Context context, int textViewResourceId, List<OptionItem> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    public View getView(int index, View convertView, ViewGroup parent) {

        View result;
        OptionItem item = getItem(index);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (item instanceof StringOptionItem || item instanceof MultiSelectOptionItem) {
            result = inflater.inflate(R.layout.menuitemstring, null);
            TextView tvTitle = (TextView) result.findViewById(R.id.menuitemtitle);
            TextView tvDescription = (TextView) result.findViewById(R.id.menuitemdescription);
            tvTitle.setText(item.getTitle());
            tvDescription.setText(item.getDescription());

        } else if (item instanceof CheckBoxOptionItem) {
            result = inflater.inflate(R.layout.menuitemcheckbox, null);
            TextView tvTitle = (TextView) result.findViewById(R.id.menuitemtitle);
            TextView tvDescription = (TextView) result.findViewById(R.id.menuitemdescription);
            CheckBox checkBox = (CheckBox) result.findViewById(R.id.menuitemcheckbox);
            checkBox.setChecked(((CheckBoxOptionItem) item).isChecked());
            tvTitle.setText(item.getTitle());
            tvDescription.setText(item.getDescription());

        } else {
            throw new RuntimeException();
        }
        return result;
    }
}
